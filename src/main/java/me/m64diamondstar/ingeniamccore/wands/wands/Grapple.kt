package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.entities.LeashablePacketEntity
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.abs


class Grapple: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#bfc1fb&lG#adafe2&lr#9b9cca&la#898ab1&lp#777799&lp#656580&ll" +
                "#777799&le #898ab1&lW#9b9cca&la#adafe2&ln#bfc1fb&ld")
    }

    override fun getCustomModelData(): Int {
        return 20
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.grapple")
    }

    override fun run(player: Player) {

        (player as Audience).sendActionBar(
            Component.text("Press shift while looking at a block to start.").color(
                TextColor
                    .fromHexString(MessageType.PLAYER_UPDATE)))

        object: BukkitRunnable(){

            var lastVelocity: Vector = player.velocity
            lateinit var hookLocation: Location
            var isHooked: Boolean = false
            var leashableEntity = LeashablePacketEntity((player.world as CraftWorld).handle, player.location, player)

            var c: Int = 0

            override fun run(){

                if(c == 400 || IngeniaMC.isDisabling || IngeniaPlayer(player).isInGame){
                    Bukkit.getScheduler().callSyncMethod(IngeniaMC.plugin) {
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

                        Bukkit.getScheduler().callSyncMethod(IngeniaMC.plugin) {
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
                    Bukkit.getScheduler().callSyncMethod(IngeniaMC.plugin) {
                        leashableEntity.despawn()
                    }
                }

                c += 1
            }

        }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)

        Cooldowns.addPlayer(player, 20500L, 22000L, 25000L, 30000L)

    }

}