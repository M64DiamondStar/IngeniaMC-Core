package me.m64diamondstar.ingeniamccore.general.commands.tabcompleters

import me.m64diamondstar.ingeniamccore.general.commands.ingenia.*
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic.CosmeticColorSubcommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic.CosmeticMessageSubcommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.cosmetic.CosmeticSubcommand
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
            tc.add("area")
            tc.add("database")
            tc.add("discord")
            tc.add("item")
            tc.add("colors")
            tc.add("messages")
            tc.add("cosmetic")
            tc.add("cosmeticmessage")
            tc.add("cosmeticcolor")
            tc.add("shop")
            tc.add("warp")
            tc.add("consumable")
            tc.add("block")
        }else if(args.size > 1){

            tc.clear()

            if(args[0].equals("balance", true)) {
                val sub = BalanceSubcommand(sender, args)
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
            }else if(args[0].equals("game", true)){
                val sub = GameSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("protect", true)){
                val sub = ProtectionSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("area", true)){
                val sub = AreaSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("database", true)){
                val sub = DatabaseSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("discord", true)){
                val sub = DiscordSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("item", true)){
                val sub = ItemSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("messages", true)){
                val sub = MessagesSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("colors", true)){
                val sub = ColorsSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("cosmetic", true)){
                val sub = CosmeticSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("cosmeticmessage", true)){
                val sub = CosmeticMessageSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("cosmeticcolor", true)) {
                val sub = CosmeticColorSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("shop", true)) {
                val sub = ShopSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("warp", true)) {
                val sub = WarpSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("consumable", true)) {
                val sub = ConsumableSubcommand(sender, args)
                tc.addAll(sub.getTabCompleters())
            }else if(args[0].equals("block", true)) {
                val sub = BlockSubcommand(sender, args)
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