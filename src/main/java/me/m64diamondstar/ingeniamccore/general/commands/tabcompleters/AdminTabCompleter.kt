package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class AdminTabCompleter: TabCompleter {

    private val tc = ArrayList<String>()

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {


        if(args.size == 1) {
            tc.clear()
            tc.add("nightvision")
            tc.add("weatherclear")
            tc.add("fly")
            tc.add("speed")
            tc.add("heal")
            tc.add("hat")
            tc.add("undress")
            tc.add("givehead")
            tc.add("back")
            tc.add("day")
            tc.add("night")
            tc.add("spawnRandomPresent")
            tc.add("location")
        }else
            tc.clear()


        val result = ArrayList<String>()
        for(a in tc){
            if(a.lowercase().startsWith(args[args.size - 1].lowercase()))
                result.add(a)
        }

        return result
    }

}