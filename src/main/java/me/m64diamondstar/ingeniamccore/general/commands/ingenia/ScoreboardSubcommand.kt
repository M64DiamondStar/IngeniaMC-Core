package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.utils.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ScoreboardSubcommand(private val sender: CommandSender, private val args: Array<String>) {
    /**
     * Execute the command
     */
    fun execute() {
        if (args.size != 2 && args.size != 3) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig scoreboard <show/hide> [player]")))
            return
        }
        if (args[1].equals("show", ignoreCase = true) || args[1].equals("true", ignoreCase = true)) {
            val player: IngeniaPlayer = if (args.size == 3) {
                IngeniaPlayer(Bukkit.getPlayer(args[2]))
            } else {
                IngeniaPlayer(sender as Player)
            }
            player.setScoreboard(true)
        } else if (args[1].equals("hide", ignoreCase = true) || args[1].equals("false", ignoreCase = true)) {
            val player: IngeniaPlayer
            if (args.size == 3) {
                player = IngeniaPlayer(Bukkit.getPlayer(args[2]))
                if (player.player == null) {
                    sender.sendMessage(Messages.invalidPlayer())
                    return
                }
            } else {
                player = IngeniaPlayer(sender as Player)
            }
            player.setScoreboard(false)
        } else {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig scoreboard <show/hide> [player]")))
        }
    }
}