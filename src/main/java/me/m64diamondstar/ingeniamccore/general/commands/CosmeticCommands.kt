package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsInventory
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CosmeticCommands: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        if(sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val featureManager = FeatureManager()
        if(!featureManager.isFeatureEnabled(FeatureType.MENU) && !sender.hasPermission("ingenia.admin")){
            sender.sendMessage(Messages.featureDisabled())
            return false
        }

        when(string) {

            "hats" -> {
                val inv = CosmeticsInventory(sender, "\uEB21", 0)
                inv.open()
            }

            "shirts" -> {
                val inv = CosmeticsInventory(sender, "\uEB22", 0)
                inv.open()
            }

            "bodywear" -> {
                val inv = CosmeticsInventory(sender, "\uEB24", 0)
                inv.open()
            }

            "balloons" -> {
                val inv = CosmeticsInventory(sender, "\uEB25", 0)
                inv.open()
            }

            "pants" -> {
                val inv = CosmeticsInventory(sender, "\uEB26", 0)
                inv.open()
            }

            "shoes" -> {
                val inv = CosmeticsInventory(sender, "\uEB27", 0)
                inv.open()
            }

        }

        return false
    }
}