package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsInventory
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

        when(string) {

            "hats" -> {
                val inv = CosmeticsInventory(sender, "國")
                inv.open()
            }

            "shirts" -> {
                val inv = CosmeticsInventory(sender, "因")
                inv.open()
            }

            "balloons" -> {
                val inv = CosmeticsInventory(sender, "果")
                inv.open()
            }

            "pants" -> {
                val inv = CosmeticsInventory(sender, "四")
                inv.open()
            }

            "boots" -> {
                val inv = CosmeticsInventory(sender, "界")
                inv.open()
            }

        }

        return false
    }
}