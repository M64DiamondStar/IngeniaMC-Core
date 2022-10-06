package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.cosmetics.CosmeticsInventory
import me.m64diamondstar.ingeniamccore.utils.Messages.noPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WandCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) {
            sender.sendMessage(noPlayer())
            return false
        }

        val player: Player = sender
        val inv = CosmeticsInventory(player)
        inv.openInventory("圖")

        return false
    }

}