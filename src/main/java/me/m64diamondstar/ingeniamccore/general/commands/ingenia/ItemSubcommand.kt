package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.cosmetics.data.BackpackItems
import me.m64diamondstar.ingeniamccore.cosmetics.data.CosmeticItems
import me.m64diamondstar.ingeniamccore.cosmetics.utils.BackpackItemType
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ItemSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {
        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return
        }

        if(args.size == 2 && args[1].equals("reload", ignoreCase = true)){
            for(cosmeticType in CosmeticType.values()){
                CosmeticItems(cosmeticType).reload()
            }
            for(backpackType in BackpackItemType.values()){
                BackpackItems(backpackType).reload()
            }
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Reloaded."))
            return
        }

        if(args.size != 5){
            sender.sendMessage(Messages.commandUsage("ig item <create/delete/set/get/exists> <cosmetic/backpack> <cosmetic type> <ID>"))
            return
        }

        try{
            val cosmeticItems =
                if(args[2].equals("cosmetic", ignoreCase = true))
                    CosmeticItems(CosmeticType.valueOf(args[3]))
                else
                     BackpackItems(BackpackItemType.valueOf(args[3]))

            if(!cosmeticItems.exists(args[4]) &&
                !(args[1].equals("create", ignoreCase = true) || args[1].equals("exists", ignoreCase = true))){
                sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                return
            }

            when(args[1].lowercase()){
                "exists" -> {
                    if(cosmeticItems.exists(args[4])){
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "This ID exists."))
                    }else
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This ID does not exist."))
                }

                "create" -> {
                    cosmeticItems.create(args[4], sender.inventory.itemInMainHand, true)
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created the cosmetic ${args[4]} as the item " +
                            "${sender.inventory.itemInMainHand.type}."))
                }

                "delete" -> {
                    cosmeticItems.delete(args[4])
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully deleted the cosmetic ${args[4]}."))
                }

                "get" -> {
                    val item = cosmeticItems.getItem(args[4])
                    if(item == null){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The item is null and couldn't be loaded."))
                        return
                    }
                    sender.inventory.addItem(item)
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully added the cosmetic ${args[4]} to your inventory."))
                }

                "set" -> {
                    cosmeticItems.setItem(args[4], sender.inventory.itemInMainHand)
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully set the cosmetic ${args[4]} to the item " +
                            "${sender.inventory.itemInMainHand.type}."))
                }

                "reload" -> {
                    cosmeticItems.reload()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully reloaded the saved items."))
                }
            }
        }catch (_: IllegalArgumentException){
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please enter a valid cosmetic type."))
        }
    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if(args.size == 2){
            tabs.add("create")
            tabs.add("delete")
            tabs.add("set")
            tabs.add("get")
            tabs.add("exists")
            tabs.add("reload")
        }

        if(args.size == 3){
            tabs.add("cosmetic")
            tabs.add("backpack")
        }

        if(args.size == 4){
            if(args[1].equals("reload", ignoreCase = true)) return tabs
            if(args[2].equals("cosmetic", ignoreCase = true))
                CosmeticType.values().forEach { tabs.add(it.toString()) }
            else if(args[2].equals("backpack", ignoreCase = true))
                BackpackItemType.values().forEach { tabs.add(it.toString()) }
        }

        if(args.size == 5){
            if(args[1].equals("reload", ignoreCase = true)) return tabs
            try{
                val cosmeticType = CosmeticType.valueOf(args[2])
                val cosmeticItems = CosmeticItems(cosmeticType)
                tabs.addAll(cosmeticItems.getAllIDs())
            }catch (_: IllegalArgumentException){}
        }

        return tabs
    }
}