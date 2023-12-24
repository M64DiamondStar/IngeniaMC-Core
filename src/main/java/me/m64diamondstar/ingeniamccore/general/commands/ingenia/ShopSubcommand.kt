package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.shops.Shop
import me.m64diamondstar.ingeniamccore.shops.inventories.ShopInventory
import me.m64diamondstar.ingeniamccore.shops.utils.ItemRequirement
import me.m64diamondstar.ingeniamccore.shops.utils.ShopItem
import me.m64diamondstar.ingeniamccore.shops.utils.ShopUtils
import me.m64diamondstar.ingeniamccore.utils.EnumUtilities
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ShopSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        if(args.size == 1){
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please enter a valid sub-command."))
            return
        }

        when(args[1].lowercase()){

            /**
             * Creates a new shop.
             * Usage: /ig shop create <category> <name>
             */
            "create" -> {
                if(args.size == 4){
                    if(ShopUtils.existsCategory(args[2]) && ShopUtils.existsShop(args[2], args[3])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The shop ${args[3]} already exists in the category ${args[2]}."))
                        return
                    }

                    Shop(args[2], args[3])
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created the shop ${args[3]} in the category ${args[2]}."))
                }else
                    sender.sendMessage(Messages.commandUsage("ig shop create <category> <name>"))
            }

            /**
             * Deletes an existing shop.
             * Usage: /ig shop delete <category> <name>
             */
            "delete" -> {
                if(args.size == 4){
                    if(!ShopUtils.existsCategory(args[2]) || !ShopUtils.existsShop(args[2], args[3])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The shop ${args[3]} in the category ${args[2]} does not exist."))
                        return
                    }

                    val shop = Shop(args[2], args[3])
                    shop.delete()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully deleted the shop ${args[3]} in the category ${args[2]}."))
                }else
                    sender.sendMessage(Messages.commandUsage("ig shop delete <category> <name>"))
            }

            /**
             * Opens an existing shop.
             * Usage: /ig shop open <category> <name>
             */
            "open" -> {
                if(args.size == 4){
                    if(!ShopUtils.existsCategory(args[2]) || !ShopUtils.existsShop(args[2], args[3])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The shop ${args[3]} in the category ${args[2]} does not exist."))
                        return
                    }

                    val shopInventory = ShopInventory(sender, args[2], args[3])
                    shopInventory.open()
                }else
                    sender.sendMessage(Messages.commandUsage("ig shop open <category> <name>"))
            }

            /**
             * Reloads the config of an existing shop.
             * Usage: /ig shop reload <category> <name>
             */
            "reload" -> {
                if(args.size == 4){
                    if(!ShopUtils.existsCategory(args[2]) || !ShopUtils.existsShop(args[2], args[3])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The shop ${args[3]} in the category ${args[2]} does not exist."))
                        return
                    }

                    val shop = Shop(args[2], args[3])
                    shop.reload()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully reloaded the shop ${args[3]} in the category ${args[2]}."))
                }else
                    sender.sendMessage(Messages.commandUsage("ig shop reload <category> <name>"))
            }

            /**
             * Reloads the config of an existing shop.
             * Usage: /ig shop skin <category> <name> <skin>
             */
            "skin" -> {
                if(args.size == 5){
                    if(!ShopUtils.existsCategory(args[2]) || !ShopUtils.existsShop(args[2], args[3])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "The shop ${args[3]} in the category ${args[2]} does not exist."))
                        return
                    }

                    val shop = Shop(args[2], args[3])
                    shop.skin = args[4]
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully set the shop skin to ${args[4]}."))
                }else
                    sender.sendMessage(Messages.commandUsage("ig shop skin <category> <name> <skin>"))
            }

            /**
             * Manages the items of an existing shop.
             */
            "items" -> {
                if(args.size < 5){
                    sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> create <shop item id> <item type> <item id>"))
                    sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> delete <shop item id>"))
                    sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> setSlot <shop item id> <slot>"))
                    sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> setPrice <shop item id> <price>"))
                    return
                }

                if(!ShopUtils.existsCategory(args[2]) || !ShopUtils.existsShop(args[2], args[3])){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "The shop ${args[3]} in the category ${args[2]} does not exist."))
                    return
                }
                val shop = Shop(args[2], args[3])

                when(args[4].lowercase()){
                    "create" -> {
                        if(args.size == 8){
                            shop.addItem(args[5], ShopItem.valueOf(args[6]), args[7])
                            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created an item in the shop ${args[3]} in the category ${args[2]}."))
                        }else
                            sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> create <shop item id> <item type> <item id>"))
                    }

                    "delete" -> {
                        if(args.size == 6){
                            shop.deleteItem(args[5])
                            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully deleted an item in the shop ${args[3]} in the category ${args[2]}."))
                        }else
                            sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> delete <shop item id>"))
                    }

                    "setslot" -> {
                        if(args.size == 7){
                            shop.setSlot(args[5], args[6].toIntOrNull())
                            if(args[6].toIntOrNull() == null)
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Set the slot to automatic."))
                            else
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Set the slot to ${args[6]}."))
                        }else
                            sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> setslot <shop item id> <slot>"))
                    }

                    "setprice" -> {
                        if(args.size == 7){
                            if(args[6].toLongOrNull() == null)
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Please give a valid price!"))
                            else{
                                shop.setPrice(args[5], args[6].toLongOrNull())
                                val numberFormat = NumberFormat.getNumberInstance(Locale.US)
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Set the price to ${numberFormat.format(args[6].toLong())}."))
                            }
                        }else
                            sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> setprice <shop item id> <price>"))
                    }

                    "addrequirement" -> {
                        if(args.size == 9){
                            if(!EnumUtilities.enumContains<ItemRequirement>(args[7].uppercase()))
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Please enter a valid item requirement!"))
                            else{
                                shop.addRequirement(args[5], ItemRequirement.valueOf(args[7].uppercase()), args[6], args[8])
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Added the requirement ${args[6].uppercase()}."))
                            }
                        }else
                            sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> addrequirement <shop item id> <requirement ID> <requirement> <requirement value>"))
                    }

                    "removerequirement" -> {
                        if(args.size == 7){
                            shop.removeRequirement(args[5], args[6].uppercase())
                            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Removed the requirement ${args[6].uppercase()}."))
                        }else
                            sender.sendMessage(Messages.commandUsage("ig shop items <category> <name> removerequirement <shop item id> <requirement ID>"))
                    }
                }
            }
        }

    }

    override fun getTabCompleters(): ArrayList<String>{
        val tabs = ArrayList<String>()

        if(args.size == 2){
            tabs.add("create")
            tabs.add("delete")
            tabs.add("open")
            tabs.add("reload")
            tabs.add("items")
            tabs.add("skin")
        }

        if(args.size == 3){
            ShopUtils.getCategories().forEach { tabs.add(it.name) }
        }

        if(args.size == 4){
            if(!args[1].equals("create", true) && !ShopUtils.existsCategory(args[2]))
                tabs.add("CATEGORY_DOES_NOT_EXIST")
            else{
                ShopUtils.getShops(args[2]).forEach { tabs.add(it.name) }
            }
        }

        if(args.size == 5){
            if(args[1].equals("items", true)){
                tabs.add("create")
                tabs.add("delete")
                tabs.add("setSlot")
                tabs.add("setPrice")
                tabs.add("addRequirement")
                tabs.add("removeRequirement")
            }

            if(args[1].equals("skin", ignoreCase = true)){
                tabs.add("\\uED00")
            }
        }

        if(args.size == 6){
            if(args[1].equals("items", true)){
                if(ShopUtils.existsCategory(args[2]) && ShopUtils.existsShop(args[2], args[3])){
                    val shop = Shop(args[2], args[3])
                    shop.getAllShopIDs().forEach { tabs.add(it) }
                }
            }
        }

        if(args.size == 7){
            if(args[1].equals("items", true)){
                if(args[4].equals("setSlot", true) || args[4].equals("setPrice", true)){
                    for(i in 0..9)
                        tabs.add(i.toString())
                }
                if(args[4].equals("create", true)){
                    ShopItem.values().forEach { tabs.add(it.toString()) }
                }
                if(args[4].equals("addRequirement", true) || args[4].equals("removeRequirement", true)){
                    tabs.add("requirement_id")
                }
            }
        }

        if(args.size == 8){
            if(args[1].equals("items", ignoreCase = true)) {
                if (args[4].equals("create", ignoreCase = true)) {
                    if (EnumUtilities.enumContains<ShopItem>(args[6])) {
                        ShopItem.valueOf(args[6].uppercase()).getAllItemIDs().forEach { tabs.add(it) }
                    }
                }

                if(args[4].equals("addRequirement", true) || args[4].equals("removeRequirement", true)){
                    ItemRequirement.values().forEach { tabs.add(it.toString()) }
                }
            }
        }

        if(args.size == 9){
            if(args[1].equals("items", ignoreCase = true)){
                if(args[4].equals("addRequirement", true) || args[4].equals("removeRequirement", true)){
                    if(EnumUtilities.enumContains<ItemRequirement>(args[7].uppercase())) ItemRequirement.valueOf(args[7].uppercase()).getValueFormat()
                }
            }
        }



        return tabs
    }
}