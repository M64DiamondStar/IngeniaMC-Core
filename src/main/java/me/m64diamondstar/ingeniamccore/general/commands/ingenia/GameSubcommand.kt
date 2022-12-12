package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWord
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWordUtils
import me.m64diamondstar.ingeniamccore.games.parkour.Parkour
import me.m64diamondstar.ingeniamccore.games.parkour.listeners.ParkourUtils
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GameSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        val player = sender

        if(args.size < 2){
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
        }

        if(args[1].equals("guesstheword", ignoreCase = true)){
            if(args.size == 4){
                if(args[2].equals("add", ignoreCase = true)){
                    GuessTheWordUtils.addWord(args[3])
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "The word has successfully been added to the list."))
                }

                else if(args[2].equals("remove", ignoreCase = true)){
                    val success = GuessTheWordUtils.removeWord(args[3])

                    if(success){
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The word has successfully been removed from the list."))
                    }else{
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This word is not on the list."))
                    }
                }
            }

            else if(args.size == 3){
                if(args[2].equals("start", ignoreCase = true)){
                    val guessTheWord = GuessTheWord()
                    guessTheWord.execute()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Started a new game of Guess The Word."))
                }
            }else{
                sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
            }
        }


        else if(args[1].equals("parkour", ignoreCase = true)){
            if(args[2].equals("create", ignoreCase = true)){
                if(ParkourUtils.existsParkour(args[3], args[4])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The parkour ${args[4]} already exists. " +
                            "If you want to reset it, please delete it and re-create it."))
                    return
                }

                val parkour = Parkour(args[3], args[4])
                parkour.createParkour(player.world)

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Parkour has successfully been created!"))
            }

            else if(args[2].equals("modify", ignoreCase = true)){
                if(!ParkourUtils.existsParkour(args[3], args[4])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The parkour ${args[4]} doesn't exist!"))
                    return
                }

                val parkour = Parkour(args[3], args[4])

                if(args[5].equals("setDisplay", ignoreCase = true) && args.size == 7){
                    parkour.displayName = args[6].replace("_", " ").replace("-", " ")
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the display name of the parkour!"))
                }

                else if(args[5].equals("setStart", ignoreCase = true) && args.size == 6){
                    parkour.startLocation = player.location
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the start location of the parkour!"))
                }

                else if(args[5].equals("setEnd", ignoreCase = true) && args.size == 6){
                    parkour.endLocation = player.location
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the end location of the parkour!"))
                }

                else if(args[5].equals("setStartRadius", ignoreCase = true) && args.size == 7){
                    try {
                        parkour.startRadius = args[6].toDouble()
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the start radius of the parkour!"))
                    }catch (e: NumberFormatException){
                        player.sendMessage(Messages.invalidNumber())
                    }
                }

                else if(args[5].equals("setEndRadius", ignoreCase = true) && args.size == 7){
                    try {
                        parkour.endRadius = args[6].toDouble()
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the end radius of the parkour!"))
                    }catch (e: NumberFormatException){
                        player.sendMessage(Messages.invalidNumber())
                    }
                }

            }

            else{
                sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
            }
        }else{
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
        }

    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()
        if(args.size == 2){
            tabs.add("guesstheword")
            tabs.add("parkour")
        }

        if(args.size == 3){
            if(args[1].equals("guesstheword", ignoreCase = true)){
                tabs.add("start")
                tabs.add("add")
                tabs.add("remove")
            }

            if(args[1].equals("parkour", ignoreCase = true)){
                tabs.add("create")
                tabs.add("modify")
            }
        }

        if(args.size == 4){
            if(args[1].equals("parkour", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    ParkourUtils.getCategories().forEach { tabs.add(it.name) }
                }
            }
        }

        if(args.size == 5){
            if(args[1].equals("parkour", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    if(!ParkourUtils.existsCategory(args[3]))
                        tabs.add("CATEGORY_DOES_NOT_EXIST")
                    else
                        ParkourUtils.getAllParkours().forEach { tabs.add(it.name) }
                }
            }
        }

        if(args.size == 6){
            if(args[1].equals("parkour", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    if(!ParkourUtils.existsParkour(args[3], args[4]))
                        tabs.add("PARKOUR_DOES_NOT_EXIST")
                    else {
                        tabs.add("setStart")
                        tabs.add("setEnd")
                        tabs.add("setStartRadius")
                        tabs.add("setEndRadius")
                        tabs.add("setDisplay")
                    }
                }
            }
        }

        return tabs
    }
}