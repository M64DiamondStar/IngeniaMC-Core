package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*
import kotlin.collections.ArrayList

class MessageTabCompleter: TabCompleter {

    private val tc = ArrayList<String>()

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {

        if(label.equals("msg", ignoreCase = true) || label.equals("tell", ignoreCase = true)) {
            if (args.size == 1) {
                Bukkit.getOnlinePlayers().forEach { if (it.name != sender.name) tc.add(it.name) }
            } else
                tc.clear()
        }else{
            tc.clear()
        }

        val result = ArrayList<String>()
        for(a in tc){
            if(a.lowercase().startsWith(args[args.size - 1].lowercase()))
                result.add(a)
        }

        return result
    }

}