package me.m64diamondstar.ingeniamccore.npc.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.npc.Npc
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.entities.CameraPacketEntity
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.time.Duration
import kotlin.math.abs
import kotlin.math.max

enum class DialogueAction {

    CINEMATIC_SHOT {
        override fun execute(npc: Npc, player: Player, data: String): BukkitTask? {
            val args = data.split("|")
            val fromLocation = LocationUtils.getLocationFromString(args[0]) ?: return null
            val toLocation = LocationUtils.getLocationFromString(args[1]) ?: return null
            val speed = args[2].toDoubleOrNull() ?: return null

            npc.getDialogue(player).setNormalView(false)
            val times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(500), Duration.ofMillis(500))
            (player as Audience).showTitle(
                Title.title(
                    Component.text(Font.Characters.COLOR_SCREEN).color(TextColor.color(0, 0, 0)),
                    Component.empty(),
                    times
                )
            )

            val movementTask = object : BukkitRunnable() {
                private var progress = 0.0
                private val maxDelta = max(max(abs(toLocation.x - fromLocation.x), abs(toLocation.y - fromLocation.y)), abs(toLocation.z - fromLocation.z))
                val cinematicEntity = CameraPacketEntity(fromLocation.world, fromLocation, player)

                override fun run() {
                    if(progress == 0.0){
                        cinematicEntity.spawn()
                        cinematicEntity.watch()

                        npc.getDialogue(player).setCamera(cinematicEntity)
                    }

                    if (progress >= 100.0) {
                        val timesNormal = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(500), Duration.ofMillis(500))
                        (player as Audience).showTitle(Title.title(Component.text(Font.Characters.COLOR_SCREEN).color(TextColor.color(0, 0, 0)), Component.empty(), timesNormal))
                        Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
                            cinematicEntity.despawn()
                            npc.getDialogue(player).setNormalView(true)
                        }, 10L)
                        cancel()
                        return
                    }

                    val interpolatedLocation = interpolateLocations(fromLocation, toLocation, progress / 100.0)
                    cinematicEntity.setLocation(interpolatedLocation)
                    progress += speed / maxDelta
                }

                private fun interpolateLocations(from: Location, to: Location, t: Double): Location {
                    val x = interpolate(from.x, to.x, t)
                    val y = interpolate(from.y, to.y, t)
                    val z = interpolate(from.z, to.z, t)
                    val yaw = interpolateAngles(from.yaw, to.yaw, t)
                    val pitch = interpolateAngles(from.pitch, to.pitch, t)
                    return Location(from.world, x, y, z, yaw.toFloat(), pitch.toFloat())
                }

                private fun interpolate(start: Double, end: Double, t: Double): Double {
                    return start + (end - start) * t
                }

                private fun interpolateAngles(start: Float, end: Float, t: Double): Double {
                    var delta = end - start
                    while (delta < -180) delta += 360f
                    while (delta >= 180) delta -= 360f
                    return (start + delta * t) % 360f
                }
            }

            return movementTask.runTaskTimer(IngeniaMC.plugin, 10L, 1L)
        }

        override fun isCorrectFormat(data: String): Boolean {
            val args = data.split("|")
            return !(LocationUtils.getLocationFromString(args[0]) == null || LocationUtils.getLocationFromString(args[1]) == null || args[2].toDoubleOrNull() == null)
        }

        override fun getExampleFormat(): String {
            return "world, x1, y1, z1, yaw1, pitch1|world, x2, y2, z2, yaw2, pitch2|speed"
        }
    };

    abstract fun execute(npc: Npc, player: Player, data: String): BukkitTask?
    abstract fun isCorrectFormat(data: String): Boolean
    abstract fun getExampleFormat(): String

}