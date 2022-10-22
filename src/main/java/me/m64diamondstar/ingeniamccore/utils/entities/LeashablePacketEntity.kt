package me.m64diamondstar.ingeniamccore.utils.entities

import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.monster.EntitySlime
import net.minecraft.world.level.World
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Slime

class LeashablePacketEntity(world: World?, loc: Location, leashHolder: Entity) : EntitySlime(EntityTypes.aG, world) {

    private val leashHolder: Entity

    init{
        this.a(loc.x, loc.y, loc.z)

        this.bukkitEntity.isSilent = true
        this.bukkitEntity.isInvulnerable = true
        (this.bukkitEntity as Slime).isInvisible = true
        this.bukkitEntity.setGravity(false)
        (this.bukkitEntity as Slime).setAI(false)

        this.leashHolder = leashHolder
    }

    fun spawn(){
        for(player in Bukkit.getOnlinePlayers()){
            (player as CraftPlayer).handle.b.a(PacketPlayOutSpawnEntity(this))
            player.handle.b.a(PacketPlayOutAttachEntity(this, (leashHolder as CraftEntity).handle))
            player.handle.b.a(PacketPlayOutEntityMetadata(this.ae() /*ID*/, this.ai() /*Data Watcher*/, true /*Clean*/))
        }
    }

    fun despawn(){
        for(player in Bukkit.getOnlinePlayers()){
            (player as CraftPlayer).handle.b.a(PacketPlayOutEntityDestroy(this.bukkitEntity.entityId))
        }
    }

}