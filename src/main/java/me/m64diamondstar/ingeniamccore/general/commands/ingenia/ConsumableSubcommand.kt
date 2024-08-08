package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.cosmetics.data.ConsumableConvertData
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ConsumableSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        val data = ConsumableConvertData()

        if(args.size < 2){
            sender.sendMessage(Messages.commandUsage("ig consumable <info/reload/setitem/delete>"))
            return
        }

        when(args[1].lowercase()){

            "info" -> {
                if(sender.inventory.itemInMainHand.isEmpty){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "You don't have an item in your main hand."))
                    return
                }

                val item = sender.inventory.itemInMainHand
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Item: " + item.type.name))
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Custom Model Data: " + item.itemMeta.customModelData))
            }

            "reload" -> {
                data.reload()
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Reloaded."))
            }

            "setitem" -> {
                if(args.size != 4){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "Usage: '/ig consumable setitem <material> <mode data>' while holding the consumed item (the item after usage)."))
                    return
                }
                if(Material.values().none { it.name.equals(args[2], ignoreCase = true) }){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "Invalid material."))
                    return
                }
                if(sender.inventory.itemInMainHand.isEmpty){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "You don't have an item in your main hand. Please hold the consumed item (the item after usage)."))
                    return
                }

                data.setConsumedItem(Material.valueOf(args[2]), args[3].toInt(), sender.inventory.itemInMainHand)
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Set."))
            }

            "delete" -> {
                if(args.size != 4){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "Usage: '/ig consumable setitem <material> <mode data>'"))
                    return
                }
                if(Material.values().none { it.name.equals(args[2], ignoreCase = true) }){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "Invalid material."))
                    return
                }

                data.removeConsumable(Material.valueOf(args[2]), args[3].toInt())
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Removed."))
            }

        }
    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if(args.size == 2) {
            tabs.add("info")
            tabs.add("reload")
            tabs.add("setitem")
            tabs.add("delete")
        }
        else if(args.size == 3){
            if(args[1].equals("setitem", ignoreCase = true) || args[1].equals("delete", ignoreCase = true)){
                Material.values().forEach { tabs.add(it.name) }
            }
        }

        return tabs
    }
}