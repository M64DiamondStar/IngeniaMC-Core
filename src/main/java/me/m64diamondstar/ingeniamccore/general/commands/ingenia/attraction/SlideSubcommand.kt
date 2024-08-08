package me.m64diamondstar.ingeniamccore.general.commands.ingenia.attraction

import me.m64diamondstar.ingeniamccore.attractions.custom.Slide
import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.Subcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player

class SlideSubcommand(private val args: Array<String>, private val player: Player): Subcommand {

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

            if(rawAttraction.getType() != AttractionType.SLIDE){
                player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}is not a Slide, so the settings cannot be modified!"))
                return
            }

            val slide = Slide(args[2], args[3])

            if((args[4].equals("setspawn", ignoreCase = true) ||
                    args[4].equals("setdespawn", ignoreCase = true)) && args.size == 5){
                val block = player.getTargetBlockExact(5)
                if(block == null || block.type == Material.AIR){
                    player.sendMessage(Colors.format(MessageType.ERROR + "Please look at a block to set the location."))
                    return
                }
                val location = block.location
                player.spawnParticle(
                    Particle.SMOKE, location.add(0.5, 0.5, 0.5),
                    100, 0.2, 0.2, 0.2, 0.0)
                location.block.type = Material.AIR

                if(args[4].equals("setspawn", ignoreCase = true))
                    slide.setSpawn(location)
                else
                    slide.setDespawn(location)
            }
        }
    }
}