package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import me.m64diamondstar.ingeniamccore.protect.FeatureType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class FeatureTabCompleter: TabCompleter {
    private val tc = ArrayList<String>()

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {

        if(args.size == 1){
            tc.clear()
            tc.add("enable")
            tc.add("disable")
            tc.add("info")
        }else if(args.size == 2 && !args[0].equals("info", true)) {
            tc.clear()
            tc.addAll(FeatureType.values().map { it.toString() })
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