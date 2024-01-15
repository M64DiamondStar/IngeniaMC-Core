package me.m64diamondstar.ingeniamccore.general.commands.ingenia.attraction

import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.custom.Frisbee
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.Subcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Particle
import org.bukkit.entity.Player

class FrisbeeSubcommand(private val args: Array<String>, private val player: Player): Subcommand {

    override fun execute(){
        if(args.size >= 5){
            if(!AttractionUtils.existsCategory(args[2])){
                player.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[2]}&r ${MessageType.ERROR}doesn't exist!"))
                return
            }
            if(!AttractionUtils.existsAttraction(args[2], args[3])){
                player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}doesn't exist!"))
                return
            }

            val rawAttraction = Attraction(args[2], args[3])

            if(rawAttraction.getType() != AttractionType.FRISBEE){
                player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}is not a Frisbee, so the settings cannot be modified!"))
                return
            }

            val frisbee = Frisbee(args[2], args[3])

            if(args.size == 5 && args[4].equals("setspawn", ignoreCase = true)){

                val location = player.location.clone()
                location.x = location.blockX + 0.5
                location.y = location.y - 1.0
                location.z = location.blockZ + 0.5

                frisbee.setSpawnLocation(location)

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Spawn location has been set."))
                player.spawnParticle(Particle.SMOKE_NORMAL, location, 100, 0.0, 0.5, 0.0, 0.0)
            }

            if(args.size == 5 && args[4].equals("setaxis", ignoreCase = true)){

                if(player.location.yaw in -45F..45F || player.location.yaw in 135F..180F || player.location.yaw in -180F..-135F){
                    frisbee.setAxis('z')
                    player.spawnParticle(Particle.SMOKE_NORMAL, player.location.clone().add(0.0, 1.0, 0.0), 150, 0.0, 0.2, 2.0, 0.0)
                }else if(player.location.yaw in 45F..135F || player.location.yaw in 225F..315F){
                    frisbee.setAxis('x')
                    player.spawnParticle(Particle.SMOKE_NORMAL, player.location.clone().add(0.0, 1.0, 0.0), 150, 2.0, 0.2, 0.0, 0.0)
                }

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Axis has been set"))
            }

            if(args.size == 6 && args[4].equals("setarmlength", ignoreCase = true)){
                if(args[5].toDoubleOrNull() == null){
                    player.sendMessage(Messages.invalidNumber())
                    return
                }
                frisbee.setArmLength(args[5].toDouble())
                player.sendMessage(Colors.format(MessageType.SUCCESS + "Arm length has been set to &r${args[5]}"))
            }

            if(args.size == 5 && args[4].equals("spawn", ignoreCase = true)){

                frisbee.spawn()

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Ride has been spawned."))
            }

            if(args.size == 5 && args[4].equals("despawn", ignoreCase = true)){

                frisbee.despawn()

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Ride has been despawned."))
            }
        }
    }
}