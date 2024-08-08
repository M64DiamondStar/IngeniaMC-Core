package me.m64diamondstar.ingeniamccore.utils.entities

import me.m64diamondstar.ingeniamccore.utils.leaderboard.Leaderboard
import me.m64diamondstar.ingeniamccore.utils.leaderboard.LeaderboardRenderer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.server.level.ServerEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.level.Level
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta

class LeaderboardPacketEntity(private val leaderboard: Leaderboard, private val world: Level, blockPosition: BlockPos, private val frameDirection: Direction)
    : ItemFrame(EntityType.ITEM_FRAME, world, blockPosition, frameDirection) {

    fun spawn(player: Player, title: String){

        val mapView = Bukkit.createMap(world.world)
        mapView.renderers.clear()

        val renderer = LeaderboardRenderer(leaderboard, player, title)
        mapView.addRenderer(renderer)

        player.sendMap(mapView)

        this.isSilent = true
        this.setDirection(frameDirection)

        val map = ItemStack(Material.FILLED_MAP)
        val meta = (map.itemMeta as MapMeta?)!!
        meta.mapView = mapView
        map.itemMeta = meta

        this.item = CraftItemStack.asNMSCopy(map)

        val serverEntity = ServerEntity(world.world.handle.level, this, 1, false, {}, emptySet())
        (player as CraftPlayer).handle.connection.send(ClientboundAddEntityPacket(this, serverEntity, direction.get3DDataValue()))
        player.handle.connection.send(ClientboundSetEntityDataPacket(this.id, this.entityData.nonDefaultValues!!))

        EntityRegistry.addEntity(this.id, this.javaClass)
    }
}