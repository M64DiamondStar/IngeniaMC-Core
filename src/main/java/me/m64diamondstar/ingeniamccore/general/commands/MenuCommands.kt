package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.general.warps.AttractionInventory
import me.m64diamondstar.ingeniamccore.general.warps.ShopInventory
import me.m64diamondstar.ingeniamccore.utils.messages.Messages.noPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MenuCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(noPlayer())
            return false
        }

        val player: Player = sender

        if(string.equals("cosmetic", ignoreCase = true) || string.equals("cosmetics", ignoreCase = true)) {
            val inv = CosmeticsInventory(player, "國")
            inv.open()
        }

        else if(string.equals("rides", ignoreCase = true)){
            val inv = AttractionInventory(player = IngeniaPlayer(player))
            inv.open()
        }

        else if(string.equals("shops", ignoreCase = true)){
            val inv = ShopInventory(player = IngeniaPlayer(player))
            inv.open()
        }

        else if(string.equals("wand", ignoreCase = true) || string.equals("wands", ignoreCase = true)){
            val inv = CosmeticsInventory(player, "圖")
            inv.open()
        }

        return false
    }
}