package me.m64diamondstar.ingeniamccore.utils.entities

import net.minecraft.network.protocol.game.*
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class CameraPacketEntity(world: World?, loc: Location, private val player: Player) : Display.ItemDisplay(
    EntityType.ITEM_DISPLAY, (world as CraftWorld).handle) {

    private val craftPlayer = player as CraftPlayer
    private var defaultGamemode = player.gameMode
    private var isAlive = false

    init{
        this.entityData.set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, 20)
        this.setPos(loc.x, loc.y, loc.z)
        this.moveTo(loc.x, loc.y, loc.z, loc.yaw, loc.pitch)

    }

    fun spawn(){
        val entityData = this.getEntityData().nonDefaultValues

        craftPlayer.handle.connection.send(ClientboundAddEntityPacket(this))
        if (entityData != null) {
            if(entityData.isNotEmpty())
                craftPlayer.handle.connection.send(ClientboundSetEntityDataPacket(this.id /*ID*/, entityData /*Data Watcher*/))
        }
        isAlive = true
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
        isAlive = false
    }

}