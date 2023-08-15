package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class AttractionTabCompleter : TabCompleter {

    private val tc = ArrayList<String>()

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {

        if (args.size == 1) {
            tc.clear()
            AttractionUtils.getCategories().forEach { tc.add(it.name) }
        } else if (args.size == 2) {
            tc.clear()
            AttractionUtils.getAttractions(args[0]).forEach { tc.add(it.name) }
        } else
            tc.clear()


        val result = ArrayList<String>()
        for (a in tc) {
            if (a.lowercase().startsWith(args[args.size - 1].lowercase()))
                result.add(a)
        }

        return result
    }
}