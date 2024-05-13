package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
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
        val featureManager = FeatureManager()
        if(!featureManager.isFeatureEnabled(FeatureType.MENU) && !sender.hasPermission("ingenia.admin")){
            sender.sendMessage(Messages.featureDisabled())
            return
        }

        if (args.size !in 1..4) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig menu [give/open] [player]")))
            sender.sendMessage(Colors.format(Messages.commandUsage("ig menu version <version> [player]")))
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
                    val mainInventory = MainInventory(player, 0)
                    mainInventory.open()
                }else{
                    val target = Bukkit.getPlayer(args[2])
                    if (target == null) {
                        sender.sendMessage(Messages.invalidPlayer())
                        return
                    }
                    val targetPlayer = IngeniaPlayer(target)

                    val mainInventory = MainInventory(targetPlayer, 0)
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
            } else if(args[1].equals("version", ignoreCase = true)){
                if(sender !is Player){
                    sender.sendMessage(Messages.noPlayer())
                    return
                }

                if(args.size == 3){
                    if(args[2].toIntOrNull() == null){
                        sender.sendMessage(Colors.format(Messages.commandUsage("ig menu version <version>")))
                        return
                    }
                    val version = args[2].toInt()
                    val mainInventory = MainInventory(IngeniaPlayer(sender), version)
                    mainInventory.open()
                }
                else if(args.size == 4){
                    if(args[2].toIntOrNull() == null){
                        sender.sendMessage(Colors.format(Messages.commandUsage("ig menu version <version>")))
                        return
                    }
                    val target = Bukkit.getPlayer(args[3])
                    if (target == null) {
                        sender.sendMessage(Messages.invalidPlayer())
                        return
                    }
                    val targetPlayer = IngeniaPlayer(target)
                    val version = args[2].toInt()
                    val mainInventory = MainInventory(targetPlayer, version)
                    mainInventory.open()
                }
                else{
                    sender.sendMessage(Colors.format(Messages.commandUsage("ig menu version <version> [player]")))
                }
            }

            else{
                sender.sendMessage(Colors.format(Messages.commandUsage("ig menu [give/open] [player]")))
            }

        }

        else {
            if(sender !is Player){
                sender.sendMessage(Messages.noPlayer())
                return
            }

            val player = IngeniaPlayer(sender)
            val mainInventory = MainInventory(player, 0)
            mainInventory.open()
        }
    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if (args.size == 2) {
            tabs.add("give")
            tabs.add("open")
            tabs.add("version")
        }

        if(args.size == 3){
            if(args[1].equals("version", ignoreCase = true)){
                tabs.addAll(listOf("0", "1"))
            }else
                for(player in Bukkit.getOnlinePlayers())
                    tabs.add(player.name)
        }

        if(args.size == 4){
            if(args[1].equals("version", ignoreCase = true)){
                for(player in Bukkit.getOnlinePlayers())
                    tabs.add(player.name)
            }
        }

        return tabs
    }
}