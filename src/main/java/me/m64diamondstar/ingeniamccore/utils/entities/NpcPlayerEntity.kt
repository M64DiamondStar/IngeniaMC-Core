package me.m64diamondstar.ingeniamccore.utils.entities

import gg.flyte.twilight.scheduler.delay
import me.m64diamondstar.ingeniamccore.utils.PlayerGameProfile
import me.m64diamondstar.ingeniamccore.utils.entities.connection.EmptyConnection
import net.minecraft.network.protocol.game.*
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player


class NpcPlayerEntity(world: World, private val loc: Location, private val owningPlayer: Player) :
    ServerPlayer((owningPlayer as CraftPlayer).handle.server, (world as CraftWorld).handle, PlayerGameProfile.getTextureProfile(owningPlayer, owningPlayer.name), ClientInformation.createDefault()) {

    init {
        this.setPos(loc.x, loc.y, loc.z)
        this.moveTo(loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
    }

    /**
     * @param alsoSelf whether the fake player should also be visible to the owner
     */
    fun spawn(alsoSelf: Boolean) {

        val serverGamePacketListener = ServerGamePacketListenerImpl(
            (Bukkit.getServer() as CraftServer).server,
            EmptyConnection(null),
            this,
            CommonListenerCookie.createInitial(gameProfile, true)
        )

        val entityData = this@NpcPlayerEntity.getEntityData()
        val bitmask = (0x01 or 0x04 or 0x08 or 0x10 or 0x20 or 0x40 or 127).toByte()
        entityData.set(EntityDataAccessor(17, EntityDataSerializers.BYTE), bitmask)
        val packedData = entityData.packDirty()

        this.connection = serverGamePacketListener

        for(player in Bukkit.getOnlinePlayers()) {
            if(player.uniqueId == this.owningPlayer.uniqueId && !alsoSelf) continue

            val serverEntity = ServerEntity(this.serverLevel(), this, 0, false, {}, emptySet())

            player as CraftPlayer
            player.handle.connection.send(ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, this))
            player.handle.connection.send(ClientboundAddEntityPacket(this, serverEntity))

            player.handle.connection.send(ClientboundSetEntityDataPacket(this@NpcPlayerEntity.id, packedData))

            player.handle.connection.send(ClientboundRotateHeadPacket(this, ((loc.yaw % 360) * 256 / 360).toInt().toByte()))
            player.handle.connection.send(ClientboundMoveEntityPacket.Rot(this.id, ((loc.yaw % 360) * 256 / 360).toInt().toByte(),
                ((loc.pitch % 360) * 256 / 360).toInt().toByte(),
                true))
        }

        delay(20) {
            for(player in Bukkit.getOnlinePlayers()) {
                (player as CraftPlayer).handle.connection.send(ClientboundPlayerInfoRemovePacket(listOf(this@NpcPlayerEntity.getUUID())))
            }
        }

    }

    fun despawn() {
        for(player in Bukkit.getOnlinePlayers()) {
            player as CraftPlayer
            player.handle.connection.send(ClientboundRemoveEntitiesPacket(this.id))
        }
    }

}