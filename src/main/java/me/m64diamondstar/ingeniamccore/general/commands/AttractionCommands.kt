package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.operate.OperateInventory
import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.custom.Frisbee
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AttractionCommands: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        when (string){

            // Open operate menu of a ride
            "operate" -> {
                if(sender !is Player){
                    sender.sendMessage(Messages.noPlayer())
                    return false
                }

                if(args.size != 2){ // Check command
                    sender.sendMessage(Messages.commandUsage("/operate <category> <name>"))
                    return false
                }

                // Check if valid ride
                if (!AttractionUtils.existsCategory(args[0])) {
                    sender.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[0]}&r ${MessageType.ERROR}doesn't exist!"))
                    return false
                }
                if (!AttractionUtils.existsAttraction(args[0], args[1])) {
                    sender.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[1]}&r ${MessageType.ERROR}doesn't exist!"))
                    return false
                }

                val operateInventory = OperateInventory(IngeniaPlayer(sender), Attraction(args[0], args[1]))
                operateInventory.open()
            }

            // Start a ride
            "dispatch" -> {
                if(args.size != 2){
                    sender.sendMessage(Messages.commandUsage("/dispatch <category> <name>"))
                    return false
                }

                // Check if valid ride
                if (!AttractionUtils.existsCategory(args[0])) {
                    sender.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[0]}&r ${MessageType.ERROR}doesn't exist!"))
                    return false
                }
                if (!AttractionUtils.existsAttraction(args[0], args[1])) {
                    sender.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[1]}&r ${MessageType.ERROR}doesn't exist!"))
                    return false
                }

                val attraction = Attraction(args[0], args[1])

                // Start ride based on type
                if(attraction.getType() == AttractionType.COASTER){
                    val coaster = Coaster(attraction.getCategory(), attraction.getName())
                    coaster.dispatch()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Dispatched ${args[1]} successfully."))
                }else if(attraction.getType() == AttractionType.FREEFALL){
                    val freeFall = FreeFall(attraction.getCategory(), attraction.getName())
                    freeFall.dispatch()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Dispatched ${args[1]} successfully."))
                }else if(attraction.getType() == AttractionType.SLIDE){
                    sender.sendMessage(Messages.invalidAttractionFunction())
                }else if(attraction.getType() == AttractionType.FRISBEE){
                    val frisbee = Frisbee(attraction.getCategory(), attraction.getName())
                    frisbee.dispatch()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Dispatched ${args[1]} successfully."))

                }
            }
        }

        return false
    }
}