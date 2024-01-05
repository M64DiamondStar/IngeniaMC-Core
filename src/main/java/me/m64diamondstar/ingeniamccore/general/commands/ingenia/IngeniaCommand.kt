package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic.CosmeticColorSubcommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic.CosmeticMessageSubcommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic.CosmeticSubcommand
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

            "menu" -> {
                val menuSubcommand = MenuSubcommand(sender, args)
                menuSubcommand.execute()
            }

            "attraction" -> {
                val attractionSubcommand = AttractionSubcommand(sender, args)
                attractionSubcommand.execute()
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

            "item" -> {
                val itemSubcommand = ItemSubcommand(sender, args)
                itemSubcommand.execute()
            }

            "messages" -> {
                val messagesSubcommand = MessagesSubcommand(sender, args)
                messagesSubcommand.execute()
            }

            "colors" -> {
                val colorsSubcommand = ColorsSubcommand(sender, args)
                colorsSubcommand.execute()
            }

            "cosmetic" -> {
                val cosmeticSubcommand = CosmeticSubcommand(sender, args)
                cosmeticSubcommand.execute()
            }

            "cosmeticmessage" -> {
                val cosmeticSubcommand = CosmeticMessageSubcommand(sender, args)
                cosmeticSubcommand.execute()
            }

            "cosmeticcolor" -> {
                val cosmeticSubcommand = CosmeticColorSubcommand(sender, args)
                cosmeticSubcommand.execute()
            }

            "shop" -> {
                val shopSubcommand = ShopSubcommand(sender, args)
                shopSubcommand.execute()
            }

            "warp" -> {
                val warpSubcommand = WarpSubcommand(sender, args)
                warpSubcommand.execute()
            }

            else -> sender.sendMessage(invalidSubcommand("ig"))
        }
        return false
    }
}