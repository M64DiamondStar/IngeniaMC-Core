package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ScoreboardSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {
    /**
     * Execute the command
     */
    override fun execute() {
        if (args.size != 2 && args.size != 3) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig scoreboard <show/hide> [player]")))
            return
        }
        if (args[1].equals("show", ignoreCase = true) || args[1].equals("true", ignoreCase = true)) {
            val player: IngeniaPlayer = if (args.size == 3) {
                IngeniaPlayer(Bukkit.getPlayer(args[2])!!)
            } else {
                IngeniaPlayer(sender as Player)
            }
            player.setScoreboard(true)
        } else if (args[1].equals("hide", ignoreCase = true) || args[1].equals("false", ignoreCase = true)) {
            val player: IngeniaPlayer
            if (args.size == 3) {
                if (Bukkit.getPlayer(args[2]) == null) {
                    sender.sendMessage(Messages.invalidPlayer())
                    return
                }
                player = IngeniaPlayer(Bukkit.getPlayer(args[2])!!)
            } else {
                player = IngeniaPlayer(sender as Player)
            }
            player.setScoreboard(false)
        } else {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig scoreboard <show/hide> [player]")))
        }
    }

    override fun getTabCompleters(): ArrayList<String>{
        val tabs = ArrayList<String>()

        if(args.size == 2){
            tabs.add("show")
            tabs.add("hide")
        }

        return tabs
    }
}