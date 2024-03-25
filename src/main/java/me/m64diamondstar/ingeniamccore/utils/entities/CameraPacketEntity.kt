package me.m64diamondstar.ingeniamccore.utils.entities

import net.minecraft.network.protocol.game.*
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.RelativeMovement
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import kotlin.random.Random

class CameraPacketEntity(world: World?, loc: Location, private val player: Player) : net.minecraft.world.entity.decoration.ArmorStand(
    EntityType.ARMOR_STAND, (world as CraftWorld).handle) {

    private val craftPlayer = player as CraftPlayer
    private var defaultGamemode = player.gameMode

    init{
        this.setPos(loc.x, loc.y, loc.z)
        this.moveTo(loc.x, loc.y, loc.z, loc.yaw, loc.pitch)

        (this.bukkitEntity as ArmorStand).isInvisible = true
        this.bukkitEntity.setGravity(false)
    }

    fun spawn(){
        val entityData = this.getEntityData().packDirty()

        craftPlayer.handle.connection.send(ClientboundAddEntityPacket(this))
        craftPlayer.handle.connection.send(ClientboundSetEntityDataPacket(this.id /*ID*/, entityData /*Data Watcher*/))
    }

    fun watch(){
        defaultGamemode = player.gameMode
        craftPlayer.handle.connection.send(ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, 3.0f))
        craftPlayer.handle.connection.send(ClientboundSetCameraPacket(this))
    }

    fun setLocation(location: Location){
        setLocation(location.x, location.y, location.z, location.yaw, location.pitch)
    }

    private fun setLocation(x: Double, y: Double, z: Double, yaw: Float, pitch: Float){
        this.moveTo(x, y, z, yaw, pitch)
        craftPlayer.handle.connection.send(ClientboundTeleportEntityPacket(this))
        craftPlayer.handle.connection.send(ClientboundRotateHeadPacket(this, (yaw * 256.0F / 360.0F).toInt().toByte()))
    }

    fun despawn(){
        craftPlayer.handle.connection.send(ClientboundRemoveEntitiesPacket(this.bukkitEntity.entityId))
        craftPlayer.handle.connection.send(ClientboundSetCameraPacket(craftPlayer.handle))
        craftPlayer.handle.connection.send(ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE,
            when(defaultGamemode){
                GameMode.SURVIVAL -> 0.0f
                GameMode.CREATIVE -> 1.0f
                GameMode.ADVENTURE -> 2.0f
                GameMode.SPECTATOR -> 3.0f
            }
        ))
    }

}