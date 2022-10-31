package me.m64diamondstar.ingeniamccore.utils.entities

import me.m64diamondstar.ingeniamccore.utils.leaderboard.Leaderboard
import me.m64diamondstar.ingeniamccore.utils.leaderboard.LeaderboardRenderer
import net.minecraft.core.BlockPosition
import net.minecraft.core.EnumDirection
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.decoration.EntityItemFrame
import net.minecraft.world.level.World
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta

class LeaderboardPacketEntity(leaderboard: Leaderboard, world: World?, blockPosition: BlockPosition, direction: EnumDirection)
    : EntityItemFrame(EntityTypes.U, world, blockPosition, direction) {

    private val leaderboard: Leaderboard
    private val world: World?
    private val direction: EnumDirection
    private var directionInt: Int

    init {
        this.leaderboard = leaderboard
        this.world = world
        this.direction = direction
        this.directionInt = 2

        if(direction == EnumDirection.d)
            directionInt = 3
        if(direction == EnumDirection.e)
            directionInt = 4
        if(direction == EnumDirection.f)
            directionInt = 5

        val mapView = world?.let { Bukkit.createMap(it.world) }
        mapView?.renderers?.clear()

        Bukkit.getOnlinePlayers().forEach {

            val renderer = LeaderboardRenderer(leaderboard, it)
            mapView!!.addRenderer(renderer)

            it.sendMap(mapView)

        }

    }

    fun spawn(){

        Bukkit.getOnlinePlayers().forEach {

            val mapView = world?.let { Bukkit.createMap(it.world) }
            mapView?.renderers?.clear()

            val renderer = LeaderboardRenderer(leaderboard, it)
            mapView!!.addRenderer(renderer)

            it.sendMap(mapView)

            val map = ItemStack(Material.FILLED_MAP)
            val meta = (map.itemMeta as MapMeta?)!!
            meta.mapView = mapView
            map.itemMeta = meta

            this.a(CraftItemStack.asNMSCopy(map))

            (it as CraftPlayer).handle.b.a(PacketPlayOutSpawnEntity(this, directionInt))
            it.handle.b.a(PacketPlayOutEntityMetadata(this.ae() /*ID*/, this.ai() /*Data Watcher*/, true /*Clean*/))

            EntityRegistry.addEntity(this.ae(), this.javaClass)
        }

    }

    fun despawn(){
        Bukkit.getOnlinePlayers().forEach {
            (it as CraftPlayer).handle.b.a(PacketPlayOutEntityDestroy(this.ae() /*ID*/))
        }
    }

}