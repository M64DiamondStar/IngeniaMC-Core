package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.entities.LeashablePacketEntity
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import net.minecraft.BlockUtil
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
import net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3D
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pig
import org.bukkit.entity.Player
import org.bukkit.entity.Slime
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.awt.TrayIcon
import kotlin.math.abs


class Grapple(player: Player): Wand {
    private var player: Player

    init {
        this.player = player
    }

    override fun getDisplayName(): String{
        return Colors.format("#bfc1fb&lG#adafe2&lr#9b9cca&la#898ab1&lp#777799&lp#656580&ll" +
                "#777799&le #898ab1&lW#9b9cca&la#adafe2&ln#bfc1fb&ld")
    }

    override fun getCustomModelData(): Int {
        return 20
    }

    override fun hasPermission(): Boolean {
        return player.hasPermission("ingeniawands.grapple")
    }

    override fun run() {

        val ingeniaPlayer = IngeniaPlayer(player)
        ingeniaPlayer.sendMessage("&8Press &7shift &8while looking at a block to start!", MessageLocation.HOTBAR)

        object: BukkitRunnable(){

            var lastVelocity: Vector = player.velocity
            lateinit var hookLocation: Location
            var isHooked: Boolean = false
            var leashableEntity = LeashablePacketEntity((player.world as CraftWorld).handle, player.location, player)

            var c: Int = 0

            override fun run(){

                if(c == 400 || Main.isDisabling){
                    Bukkit.getScheduler().callSyncMethod(Main.plugin) {
                        leashableEntity.despawn()
                    }
                    cancel()
                    return
                }

                if(player.isSneaking){

                    if(!isHooked) {
                        hookLocation = player.getTargetBlock(null, 40).location.add(0.5, 0.5, 0.5)
                        if(hookLocation.block.type == Material.AIR)
                            return

                        isHooked = true

                        Bukkit.getScheduler().callSyncMethod(Main.plugin) {
                            leashableEntity = LeashablePacketEntity((player.world as CraftWorld).handle, hookLocation, player)
                            leashableEntity.spawn()
                        }

                    }

                    else {

                        var y = 0.4
                        if(player.location.y > hookLocation.y)
                            y = 0.0

                        val ver = Vector(lastVelocity.x, y, lastVelocity.z).multiply(abs(lastVelocity.y) * 15)
                        val hor = hookLocation.clone().toVector().subtract(player.location.toVector())

                        player.velocity = player.velocity.add(hor.add(ver).normalize().multiply(0.2))
                    }

                }else{
                    isHooked = false
                    Bukkit.getScheduler().callSyncMethod(Main.plugin) {
                        leashableEntity.despawn()
                    }
                }

                c += 1
            }

        }.runTaskTimerAsynchronously(Main.plugin, 0L, 1L)

        Cooldowns.addPlayer(player, 20000L, 22000L, 25000L, 30000L)

    }

}