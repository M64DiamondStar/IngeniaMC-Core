package me.m64diamondstar.ingeniamccore.utils.entities

import com.mojang.datafixers.util.Pair
import gg.flyte.twilight.event.TwilightListener
import gg.flyte.twilight.event.event
import gg.flyte.twilight.scheduler.delay
import gg.flyte.twilight.scheduler.sync
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.protect.listeners.EntityDismountListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket
import net.minecraft.server.level.ServerEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.decoration.ArmorStand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class BodyWearEntity(private val world: World, loc: Location, private val player: Player) : ArmorStand(
    EntityType.ARMOR_STAND, (world as CraftWorld).handle) {

    private var isDisabled: Boolean = false
    private var itemStack: ItemStack? = null

    private var spawned = false

    private val dismountListener: TwilightListener = event<EntityDismountEvent>{
        if(this.entity != player) return@event
        delay(1){
            spawn()
            isDisabled = false
        }
    }

    private val moveListener: TwilightListener = event<PlayerMoveEvent>{
         if(!spawned){
             spawn(this.player)
             spawned = true
         }
    }

    private val runnable = object: BukkitRunnable(){
        override fun run(){
            //this@BodyWearEntity.yRot = player.yaw
            (this@BodyWearEntity.bukkitEntity as org.bukkit.entity.ArmorStand).bodyYaw = player.yaw
            updateEntityData()

            if(player.isInsideVehicle){
                remove()
                isDisabled = true
            }
            else if(!player.isInsideVehicle && isDisabled){
                spawn()
                isDisabled = false
            }
        }
    }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)

    init{
        this.isInvisible = true
        this.isInvulnerable = true
        this.isMarker = true
        this.setPos(loc.x, loc.y, loc.z)
        this.moveTo(loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
        this.startRiding((player as CraftPlayer).handle, true)
        for(onlinePlayer in Bukkit.getOnlinePlayers()) {
            val serverEntity = ServerEntity((world as CraftWorld).handle.level, this, 0, false, {}, emptySet())
            (onlinePlayer as CraftPlayer).handle.connection.send(ClientboundAddEntityPacket(this, serverEntity))
        }
        updateEntityData()
        sync { stopRiding() }
    }

    fun spawn(forPlayer: Player){
        this.setPos(player.location.x, player.location.y, player.location.z)
        this.moveTo(player.location.x, player.location.y, player.location.z, player.location.yaw, player.location.pitch)
        val serverEntity = ServerEntity((world as CraftWorld).handle.level, this, 0, false, {}, emptySet())
        this.startRiding((player as CraftPlayer).handle, true)
        (forPlayer as CraftPlayer).handle.connection.send(ClientboundAddEntityPacket(this, serverEntity))
        forPlayer.handle.connection.send(ClientboundSetPassengersPacket((player).handle))
        setItem(itemStack)
        sync { stopRiding() }
    }

    fun spawn(){
        Bukkit.getOnlinePlayers().forEach { spawn(it) }
    }

    private fun updateEntityData(){
        val entityData = this.getEntityData().nonDefaultValues
        if (entityData != null) {
            if(entityData.isNotEmpty())
                for(onlinePlayer in Bukkit.getOnlinePlayers()) {
                    (onlinePlayer as CraftPlayer).handle.connection.send(ClientboundSetEntityDataPacket(this.id /*ID*/, entityData /*Data Watcher*/))
                    onlinePlayer.handle.connection.send(ClientboundRotateHeadPacket(this, ((player.location.yaw % 360) * 256 / 360).toInt().toByte()))
                }
        }
    }

    fun setItem(itemStack: ItemStack?){
        val item = CraftItemStack.asNMSCopy(itemStack ?: ItemStack(Material.AIR))
        this.setItemSlot(EquipmentSlot.HEAD, item)
        for(onlinePlayer in Bukkit.getOnlinePlayers()){
            (onlinePlayer as CraftPlayer).handle.connection.send(ClientboundSetEquipmentPacket(this.id, listOf(Pair(
                EquipmentSlot.HEAD, item
            ))))
        }
        updateEntityData()
        this.itemStack = itemStack
    }

    fun getItem(): ItemStack?{
        return itemStack
    }

    fun remove(){
        for(onlinePlayer in Bukkit.getOnlinePlayers()){
            (onlinePlayer as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(this.id))
        }
    }

    fun remove(forPlayer: Player){
        //this.remove(RemovalReason.DISCARDED)
        (forPlayer as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(this.id))
    }

    fun unregister(){
        dismountListener.unregister()
        moveListener.unregister()
        runnable.cancel()
    }

}