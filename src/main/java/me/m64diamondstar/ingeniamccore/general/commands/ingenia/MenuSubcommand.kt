package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MenuSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {
    /**
     * Execute the command
     */
    override fun execute() {
        if (args.size !in 1..3) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig menu [give/open] [player]")))
            return
        }

        if(args.size > 1){

            if(args[1].equals("open", ignoreCase = true)){
                if(sender !is Player){
                    sender.sendMessage(Messages.noPlayer())
                    return
                }

                if(args.size == 2) {
                    val player = IngeniaPlayer(sender)
                    val mainInventory = MainInventory(player)
                    mainInventory.open()
                }else{
                    val target = Bukkit.getPlayer(args[2])
                    if (target == null) {
                        sender.sendMessage(Messages.invalidPlayer())
                        return
                    }
                    val targetPlayer = IngeniaPlayer(target)

                    val mainInventory = MainInventory(targetPlayer)
                    mainInventory.open()
                }
            } else if(args[1].equals("give", ignoreCase = true)){
                if(sender !is Player){
                    sender.sendMessage(Messages.noPlayer())
                    return
                }

                if(args.size == 2) {
                    val player = IngeniaPlayer(sender)
                    player.giveMenuItem()
                }else{
                    val target = Bukkit.getPlayer(args[2])
                    if (target == null) {
                        sender.sendMessage(Messages.invalidPlayer())
                        return
                    }
                    val targetPlayer = IngeniaPlayer(target)
                    targetPlayer.giveMenuItem()
                }
            } else{
                sender.sendMessage(Colors.format(Messages.commandUsage("ig menu [give/open] [player]")))
            }

        }

        else {
            if(sender !is Player){
                sender.sendMessage(Messages.noPlayer())
                return
            }

            val player = IngeniaPlayer(sender)
            val mainInventory = MainInventory(player)
            mainInventory.open()
        }
    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if (args.size == 2) {
            tabs.add("give")
            tabs.add("open")
        }

        if(args.size == 3){
            for(player in Bukkit.getOnlinePlayers())
                tabs.add(player.name)
        }

        return tabs
    }
}