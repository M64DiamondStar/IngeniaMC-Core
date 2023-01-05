package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.shows.utils.ShowUtils
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.NumberFormatException

class ShowSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    /**
     * Executes the show sub-command
     */
    override fun execute() {

        // Play a show from start, one specific effect or start from an effect ID
        if(args.size >= 4 && args[1].equals("play", ignoreCase = true)){
            if(!exists(sender))
                return

            val show = Show(args[2], args[3])

            if(args.size == 4){
                show.play()
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully started this show."))
            }

            else if(args.size == 6){
                if(args[4].equals("only", ignoreCase = true)){
                    try {
                        if(show.playOnly(args[5].toInt()))
                            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully played effect ${args[5]} of this show."))
                        else
                            sender.sendMessage(Colors.format("${MessageType.ERROR}&n${args[5]}&r ${MessageType.ERROR}is not a valid ID."))
                    }catch (e: NumberFormatException){
                        sender.sendMessage(Colors.format("${MessageType.ERROR}&n${args[5]}&r ${MessageType.ERROR}is not a valid number."))
                    }
                }else if(args[4].equals("from", ignoreCase = true)){
                    try {
                        show.playFrom(args[5].toInt())
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully started from effect ${args[5]} of this show."))
                    }catch (e: NumberFormatException){
                        sender.sendMessage(Colors.format("${MessageType.ERROR}&n${args[5]}&r ${MessageType.ERROR}is not a valid number."))
                    }
                }else sender.sendMessage(Messages.invalidSubcommand("ig show play <category> <name> [<only/from> <id>]"))
            }else sender.sendMessage(Messages.invalidSubcommand("ig show play <category> <name> [<only/from> <id>]"))

        }

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        val player = sender

        if(args.size < 2){ //Args aren't long enough
            player.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
            return
        }

        // Create new show with category and name
        if(args.size == 4 && args[1].equals("create", ignoreCase = true)){
            if(ShowUtils.existsCategory(args[2])) {
                if (ShowUtils.existsShow(args[2], args[3])) {
                    player.sendMessage(Colors.format(MessageType.ERROR + "This show already exist. Please delete it first if you want to replace it."))
                    return
                }
            }

            val show = Show(args[2], args[3])
            show.createShow()
            player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created this show."))
        }

        //Delete a show
        else if (args.size == 4 && args[1].equals("delete", ignoreCase = true)){
            if(!exists(player))
                return
        }

        //Edit a show
        else if(args.size > 4 && args[1].equals("edit", ignoreCase = true)){
            if(!exists(player))
                return

            //val show = Show(args[2], args[3])

        }
    }

    /**
     * Adds all the tab completers needed in this sub-command
     * @return the full tab completion list
     */
    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if(args.size == 2){
            tabs.add("create")
            tabs.add("delete")
            tabs.add("edit")
            tabs.add("play")
        }

        if(args.size == 3 && !(args[1].equals("create", true) || args[1].equals("delete", true)))
            ShowUtils.getCategories().forEach { tabs.add(it.name) }

        if(args.size == 4 && !(args[1].equals("create", true) || args[1].equals("delete", true)))
            ShowUtils.getShows(args[2]).forEach { tabs.add(it.name) }



        return tabs
    }

    /**
     * Checks if the show exists based off of args 2 (category) and 3 (name)
     * and sends error messages to player
     * @return true if it exists, false if it doesn't
     */
    private fun exists(sender: CommandSender): Boolean{
        if(!ShowUtils.existsCategory(args[2])){
            sender.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[2]}&r ${MessageType.ERROR}doesn't exist!"))
            return false
        }
        if(!ShowUtils.existsShow(args[2], args[3])){
            sender.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}doesn't exist!"))
            return false
        }
        return true
    }
}