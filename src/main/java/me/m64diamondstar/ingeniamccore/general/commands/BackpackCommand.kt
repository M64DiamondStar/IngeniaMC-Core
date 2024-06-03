package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.cosmetics.inventory.BackpackInventory
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class BackpackCommand: TabExecutor {

    override fun onTabComplete(sender: CommandSender, command: Command, string: String, args: Array<out String>): List<String> {
        if(args.size == 1){
            val result = ArrayList<String>()
            for(i in 1..9) result.add(i.toString())
            return result
        }
        return emptyList()
    }

    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {
        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        var page = 1
        if(args.isNotEmpty() && args[0].toIntOrNull() != null) {
            page = args[0].toInt()
        }
        val inv = BackpackInventory(sender, page)
        inv.open()

        return true
    }
}