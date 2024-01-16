package me.m64diamondstar.ingeniamccore.utils.entities

import net.minecraft.network.protocol.game.*
import net.minecraft.world.entity.EntityType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Slime

class LeashablePacketEntity(world: World?, loc: Location, private val leashHolder: Entity) : net.minecraft.world.entity.monster.Slime(
    EntityType.SLIME, (world as CraftWorld).handle) {

    init{
        this.moveTo(loc.x, loc.y, loc.z)

        this.bukkitEntity.isSilent = true
        this.bukkitEntity.isInvulnerable = true
        (this.bukkitEntity as Slime).isInvisible = true
        this.bukkitEntity.setGravity(false)
        (this.bukkitEntity as Slime).setAI(false)
    }

    fun spawn(){
        val entityData = this.getEntityData().nonDefaultValues

        for(player in Bukkit.getOnlinePlayers()){
            (player as CraftPlayer).handle.connection.send(ClientboundAddEntityPacket(this))
            player.handle.connection.send(ClientboundSetEntityLinkPacket(this, (leashHolder as CraftEntity).handle))
            player.handle.connection.send(ClientboundSetEntityDataPacket(this.id /*ID*/, entityData /*Data Watcher*/))
        }
    }

    fun despawn(){
        for(player in Bukkit.getOnlinePlayers()){
            (player as CraftPlayer).handle.connection.send(ClientboundRemoveEntitiesPacket(this.bukkitEntity.entityId))
        }
    }

}