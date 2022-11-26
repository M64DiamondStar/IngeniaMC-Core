package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import me.m64diamondstar.ingeniamccore.general.commands.ingenia.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class IngeniaTabCompleter: TabCompleter {

    private val tc = ArrayList<String>()

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {


        if(args.size == 1) {
            tc.clear()
            tc.add("balance")
            tc.add("scoreboard")
            tc.add("exp")
            tc.add("menu")
            tc.add("attraction")
            tc.add("show")
            tc.add("reload")
            tc.add("game")
            tc.add("protect")
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
            }else if(args[0].equals("menu", true)) {
                val sub = MenuSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("attraction", true)){
                val sub = AttractionSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("show", true)){
                val sub = ShowSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("game", true)){
                val sub = GameSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("protect", true)){
                val sub = ProtectionSubcommand(sender, args)
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