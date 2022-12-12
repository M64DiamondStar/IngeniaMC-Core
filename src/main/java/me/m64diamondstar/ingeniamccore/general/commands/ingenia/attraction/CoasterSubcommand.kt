package me.m64diamondstar.ingeniamccore.general.commands.ingenia.attraction

import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.Subcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import java.lang.NumberFormatException

class CoasterSubcommand(private val args: Array<String>, private val player: Player): Subcommand {

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

            if(rawAttraction.getType() != AttractionType.COASTER){
                player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}is not a Coaster, so the settings cannot be modified!"))
                return
            }

            val coaster = Coaster(args[2], args[3])

            if(args[4].equals("row", ignoreCase = true)){
                if(args.size == 7 && (args[6].equals("setstation", ignoreCase = true) ||
                            args[6].equals("setspawn", ignoreCase = true) ||
                            args[6].equals("setdespawn", ignoreCase = true))){
                    val block = player.getTargetBlockExact(5)
                    if(block == null || block.type == Material.AIR){
                        player.sendMessage(Colors.format(MessageType.ERROR + "Please look at a block to set the location."))
                        return
                    }

                    try{args[5].toInt()}
                    catch (e: NumberFormatException) {
                        player.sendMessage(Messages.invalidNumber())
                        return
                    }

                    val location = block.location
                    if(args[6].equals("setstation", ignoreCase = true)) {
                        coaster.setRowStation(args[5].toInt(), location)
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Row Station has been set."))
                    }else if(args[6].equals("setspawn", ignoreCase = true)){
                        coaster.setRowSpawn(args[5].toInt(), location)
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Row Spawn has been set."))
                    }else{
                        coaster.setRowDespawn(args[5].toInt(), location)
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Row Despawn has been set."))
                    }
                    player.spawnParticle(Particle.SMOKE_NORMAL, location.add(0.5, 0.5, 0.5),
                        100, 0.2, 0.2, 0.2, 0.0)
                    location.block.type = Material.AIR
                }
            }
        }
    }
}