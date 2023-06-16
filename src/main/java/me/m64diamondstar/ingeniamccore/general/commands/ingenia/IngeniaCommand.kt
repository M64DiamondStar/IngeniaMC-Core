package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors.format
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages.invalidSubcommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class IngeniaCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            return false
        }
        when (args[0]) {
            "exp" -> {
                val exp = ExpSubcommand(sender, args)
                exp.execute()
            }

            "balance" -> {
                val bal = BalanceSubcommand(sender, args)
                bal.execute()
            }

            "scoreboard" -> {
                val scoreboard = ScoreboardSubcommand(sender, args)
                scoreboard.execute()
            }

            "menu" -> {
                val menuSubcommand = MenuSubcommand(sender, args)
                menuSubcommand.execute()
            }

            "attraction" -> {
                val attractionSubcommand = AttractionSubcommand(sender, args)
                attractionSubcommand.execute()
            }

            "show" -> {
                val showSubcommand = ShowSubcommand(sender, args)
                showSubcommand.execute()
            }

            "game" -> {
                val gameSubcommand = GameSubcommand(sender, args)
                gameSubcommand.execute()
            }

            "reload" -> {
                IngeniaMC.plugin.reloadConfig()
                sender.sendMessage(format(MessageType.SUCCESS + "Successfully reloaded config.yml!"))
            }

            "protect" -> {
                val protectionSubcommand = ProtectionSubcommand(sender, args)
                protectionSubcommand.execute()
            }

            "area" -> {
                val areaSubcommand = AreaSubcommand(sender, args)
                areaSubcommand.execute()
            }

            "database" -> {
                val databaseSubcommand = DatabaseSubcommand(sender, args)
                databaseSubcommand.execute()
            }

            "discord" -> {
                val discordSubcommand = DiscordSubcommand(sender, args)
                discordSubcommand.execute()
            }

            else -> sender.sendMessage(invalidSubcommand("ig"))
        }
        return false
    }
}