package me.m64diamondstar.ingeniamccore.utils.entities

import me.m64diamondstar.ingeniamccore.utils.leaderboard.Leaderboard
import me.m64diamondstar.ingeniamccore.utils.leaderboard.LeaderboardRenderer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.level.Level
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta

class LeaderboardPacketEntity(private val leaderboard: Leaderboard, private val world: Level?, blockPosition: BlockPos, direction: Direction)
    : ItemFrame(EntityType.ITEM_FRAME, world!!, blockPosition, direction) {

    fun spawn(player: Player, title: String){
        val mapView = world?.let { Bukkit.createMap(it.world) }
        mapView?.renderers?.clear()

        val renderer = LeaderboardRenderer(leaderboard, player, title)
        mapView!!.addRenderer(renderer)

        player.sendMap(mapView)

        this.isSilent = true

        val map = ItemStack(Material.FILLED_MAP)
        val meta = (map.itemMeta as MapMeta?)!!
        meta.mapView = mapView
        map.itemMeta = meta

        this.item = CraftItemStack.asNMSCopy(map)

        (player as CraftPlayer).handle.connection.send(ClientboundAddEntityPacket(this, direction.get3DDataValue()))
        player.handle.connection.send(ClientboundSetEntityDataPacket(this.id, this.entityData.packDirty()))

        EntityRegistry.addEntity(this.id, this.javaClass)
    }

}