package me.m64diamondstar.ingeniamccore.general.commands.ingenia.attraction

import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.Subcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Particle
import org.bukkit.entity.Player

class FreefallSubcommand(private val args: Array<String>, private val player: Player): Subcommand {

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

            if(rawAttraction.getType() != AttractionType.FREEFALL){
                player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}is not a FreeFall, so the settings cannot be modified!"))
                return
            }

            val freeFall = FreeFall(args[2], args[3])

            if(args.size == 5 && args[4].equals("setspawn", ignoreCase = true)){

                val location = player.location.clone()
                location.x = location.blockX + 0.5
                location.y = location.y - 1.0
                location.z = location.blockZ + 0.5

                freeFall.setSpawnLocation(location)

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Spawn location has been set."))
                player.spawnParticle(Particle.SMOKE_NORMAL, location, 100, 0.0, 0.5, 0.0, 0.0)
            }

            if(args.size == 5 && args[4].equals("spawn", ignoreCase = true)){

                freeFall.spawn()

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Ride has been spawned."))
            }

            if(args.size == 5 && args[4].equals("despawn", ignoreCase = true)){

                freeFall.despawn()

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Ride has been despawned."))
            }
        }
    }
}