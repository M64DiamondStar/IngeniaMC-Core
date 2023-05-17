package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class DiscordSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {
        if(args.size == 4){
            if(args[1].equals("linkingCooldown", ignoreCase = true)){
                if(args[3].equals("reset", ignoreCase = true)){
                    // Reset cooldown of player
                    if(Bukkit.getPlayer(args[2]) == null) {
                        sender.sendMessage(Colors.format(Messages.invalidPlayer()))
                        return
                    }
                    val ingeniaPlayer = IngeniaPlayer(Bukkit.getPlayer(args[2])!!)
                    ingeniaPlayer.resetLinkingCooldown()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully reset ${args[2]}'s cooldown."))
                }
            }
        }
    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()
        if(args.size == 2){
            tabs.add("linkingCooldown")
        }
        if(args.size == 3){
            if(args[1].equals("linkingCooldown", ignoreCase = true)){
                Bukkit.getOnlinePlayers().forEach { tabs.add(it.name) }
            }
        }
        if(args.size == 4){
            if(args[1].equals("linkingCooldown", ignoreCase = true)){
                tabs.add("reset")
            }
        }
        return tabs
    }
}