package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import me.m64diamondstar.ingeniamccore.general.commands.ingenia.BalanceSubcommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.ExpSubcommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.ScoreboardSubcommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class IngeniaTabCompleter: TabCompleter {

    private val tc = ArrayList<String>()

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String>? {


        if(args.size == 1) {
            tc.clear()
            tc.add("balance")
            tc.add("scoreboard")
            tc.add("exp")
        }else if(args.size > 1){

            tc.clear()

            if(args[0].equals("balance", true)) {
                val sub = BalanceSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("scoreboard", true)) {
                val sub = ScoreboardSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("exp", true)) {
                val sub = ExpSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }

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