package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsInventory
import me.m64diamondstar.ingeniamccore.utils.messages.Messages.noPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CosmeticCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(noPlayer())
            return false
        }

        val player: Player = sender

        val inv = CosmeticsInventory(player)
        inv.openInventory()

        return false
    }
}