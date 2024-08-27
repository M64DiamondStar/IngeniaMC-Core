package me.m64diamondstar.ingeniamccore.utils.entities

import gg.flyte.twilight.event.TwilightListener
import gg.flyte.twilight.event.event
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.npc.utils.DialoguePlayerRegistry
import me.m64diamondstar.ingeniamccore.utils.PlayerSelectors
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket
import net.minecraft.server.level.ServerEntity
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

class NametagEntity(private val world: World, loc: Location, private val player: Player) : Display.TextDisplay(
    EntityType.TEXT_DISPLAY, (world as CraftWorld).handle) {

    object Registry{

        private val map = HashMap<UUID, NametagEntity>()

        fun add(uuid: UUID, nametagEntity: NametagEntity){
            map[uuid] = nametagEntity
        }

        fun get(uuid: UUID): NametagEntity? {
            return map[uuid]
        }

        fun remove(uuid: UUID){
            map.remove(uuid)
        }

        fun contains(uuid: UUID): Boolean {
            return map.containsKey(uuid)
        }

    }


    private var isOpaque = false
    private var isRiding = false
    private var title: net.kyori.adventure.text.Component = net.kyori.adventure.text.Component.text(player.name)

    private val listener: TwilightListener = event<PlayerJoinEvent>{
        spawn(this.player)
    }

    private val leaveListener: TwilightListener = event<PlayerQuitEvent>{
        if(this@NametagEntity.player.uniqueId == this.player.uniqueId) {
            remove()
            unregister()
        }
    }

    private val runnable = object: BukkitRunnable(){
        override fun run(){
            if((DialoguePlayerRegistry.contains(player) || player.gameMode == GameMode.SPECTATOR) && isRiding){
                remove()
                isRiding = false
            }

            else if(!DialoguePlayerRegistry.contains(player) && player.gameMode != GameMode.SPECTATOR && !isRiding){
                spawn()
                setOpaque(false)
                isRiding = true
            }

            else if(player.isSneaking && isRiding){
                setOpaque(true)
            }

            else if(!player.isSneaking && isRiding){
                setOpaque(false)
            }
        }
    }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)

    init{
        this.setPos(loc.x, loc.y, loc.z)
        this.moveTo(loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
        this.text = Component.Serializer.fromJson(
            JSONComponentSerializer.json().serialize(title),
            this.level().registryAccess()
        ) as Component
        this.billboardConstraints = BillboardConstraints.CENTER
        val transformation = (this.bukkitEntity as org.bukkit.entity.TextDisplay).transformation
        transformation.scale.mul(0.85f)
        transformation.translation.add(0f, 0.2f, 0f)
        (this.bukkitEntity as org.bukkit.entity.TextDisplay).transformation = transformation
        (this.bukkitEntity as org.bukkit.entity.TextDisplay).backgroundColor = Color.fromARGB(0, 0, 0, 0)
        (this.bukkitEntity as org.bukkit.entity.TextDisplay).isShadowed = true

        player.addPassenger(this.bukkitEntity)
        isRiding = true
    }

    fun spawn(forPlayer: Player){
        this.setPos(player.location.x, player.location.y, player.location.z)
        this.moveTo(player.location.x, player.location.y, player.location.z, player.location.yaw, player.location.pitch)
        val serverEntity = ServerEntity((world as CraftWorld).handle.level, this, 0, false, {}, emptySet())
        val entityData = this.getEntityData().nonDefaultValues
        (forPlayer as CraftPlayer).handle.connection.send(ClientboundAddEntityPacket(this, serverEntity))
        forPlayer.handle.connection.send(ClientboundSetPassengersPacket((player as CraftPlayer).handle))
        if (entityData != null) {
            if(entityData.isNotEmpty())
                forPlayer.handle.connection.send(ClientboundSetEntityDataPacket(this.id /*ID*/, entityData /*Data Watcher*/))
        }
    }

    fun spawn(){
        player.addPassenger(this.bukkitEntity)
        Bukkit.getOnlinePlayers().forEach {
            when(IngeniaPlayer(it).playerConfig.getShowNametags()){
                PlayerSelectors.ALL -> {
                    spawn(it)
                }
                PlayerSelectors.STAFF -> {
                    if(player.hasPermission("ingenia.team") || player.hasPermission("ingenia.team-trial") || player.isOp)
                        spawn(it)
                }
                PlayerSelectors.NONE -> {}
            }
        }
    }

    private fun updateEntityData(){
        val entityData = this.getEntityData().nonDefaultValues
        if (entityData != null) {
            if(entityData.isNotEmpty())
                for(onlinePlayer in Bukkit.getOnlinePlayers()) {
                    (onlinePlayer as CraftPlayer).handle.connection.send(ClientboundSetEntityDataPacket(this.id /*ID*/, entityData /*Data Watcher*/))
                }
        }
    }

    fun setOpaque(isOpaque: Boolean){
        this.textOpacity = if(isOpaque) 110 else 0
        this.isOpaque = isOpaque
        updateEntityData()
    }

    fun setTitle(component: net.kyori.adventure.text.Component){
        title = component
        this.text = Component.Serializer.fromJson(
            JSONComponentSerializer.json().serialize(title),
            this.level().registryAccess()
        ) as Component
        updateEntityData()
    }

    fun remove(){
        //this.remove(RemovalReason.DISCARDED)
        for(onlinePlayer in Bukkit.getOnlinePlayers()){
            (onlinePlayer as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(this.id))
        }
    }

    fun remove(forPlayer: Player){
        //this.remove(RemovalReason.DISCARDED)
        (forPlayer as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(this.id))
    }

    fun unregister(){
        leaveListener.unregister()
        listener.unregister()
        runnable.cancel()
    }

}