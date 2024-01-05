package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import me.m64diamondstar.ingeniamccore.warps.WarpManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class WarpTabCompleter : TabCompleter {

    private val tc = ArrayList<String>()

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {

        if(args.size == 1){
            val warpManager = WarpManager()
            tc.addAll(warpManager.getAllIDs())
        }else
            tc.clear()

        val result = ArrayList<String>()
        for (a in tc) {
            if (a.lowercase().startsWith(args[args.size - 1].lowercase()))
                result.add(a)
        }

        return result
    }
}