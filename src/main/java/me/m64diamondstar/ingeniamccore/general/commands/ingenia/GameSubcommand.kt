package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWord
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWordUtils
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.command.CommandSender

class GameSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {

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
                    guessTheWord.start()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Started a new game of Guess The Word."))
                }
            }else{
                sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
            }
        }

    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()
        if(args.size == 2){
            tabs.add("guesstheword")
        }

        if(args.size == 3){
            if(args[1].equals("guesstheword", ignoreCase = true)){
                tabs.add("start")
                tabs.add("add")
                tabs.add("remove")
            }
        }

        return tabs
    }
}