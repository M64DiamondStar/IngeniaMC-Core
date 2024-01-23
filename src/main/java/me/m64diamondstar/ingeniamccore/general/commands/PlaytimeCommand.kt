package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Statistic
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlaytimeCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val ticks = sender.getStatistic(Statistic.PLAY_ONE_MINUTE) // Name is misleading, it's actually playtime in ticks
        val totalSeconds: Int = ticks / 20
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val playTimeFormatted = String.format("%02dh %02dm %02ds", hours, minutes, seconds)

        when(ticks){
            in 0..216000 -> {
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Welcome to IngeniaMC! You currently have $playTimeFormatted of playtime."))
            }

            in 216001..1728000 -> {
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Hey! You currently have $playTimeFormatted of playtime."))
            }

            in 1728001..7200000 -> {
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Nice! You already have $playTimeFormatted of playtime."))
            }

            in 7200001..36000000 -> {
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Woah, you seem like a big IngeniaMC fan! You have $playTimeFormatted of playtime!"))
            }

            in 36000001..144000000 -> {
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "You've already played for so long! You have $playTimeFormatted of playtime!"))
            }

            else -> {
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Go touch grass, you have $playTimeFormatted of playtime..."))
            }
        }

        return false
    }
}