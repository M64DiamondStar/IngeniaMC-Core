package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.utils.items.Items
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AdminCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        if(args.isEmpty()){
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please enter a valid sub-command."))
            return false
        }

        if(args[0].equals("speed", ignoreCase = true) && args.size == 2){
            try {
                if (sender.isFlying)
                    sender.flySpeed = args[1].toFloat() / 10
                else
                    sender.walkSpeed = args[1].toFloat() / 5

                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Speed set to ${args[1]}."))
            }catch (e: NumberFormatException){
                sender.sendMessage(Messages.invalidNumber())
            }
        }

        if(args[0].equals("givehead", ignoreCase = true) && args.size == 2){
            val head = Items.getPlayerHead(args[1])
            sender.inventory.addItem(head)
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "I've been waiting for that heheh, *unzips pants*..."))
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Oh, you mean that kind of head... Well, here you go...（´＿｀)"))
        }

        if(args[0].equals("heal", ignoreCase = true) && args.size == 1){
            sender.health = 20.0
            sender.foodLevel = 20
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Fully healed."))
        }

        if(args[0].equals("fly", ignoreCase = true) && args.size == 1){
            sender.allowFlight = !sender.allowFlight
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Toggled flight."))
        }

        if (args[0].equals("undress", ignoreCase = true)) {
            if (sender.inventory.helmet != null) sender.inventory
                .addItem(sender.inventory.helmet)
            if (sender.inventory.chestplate != null) sender.inventory
                .addItem(sender.inventory.chestplate)
            if (sender.inventory.leggings != null) sender.inventory
                .addItem(sender.inventory.leggings)
            if (sender.inventory.boots != null) sender.inventory
                .addItem(sender.inventory.boots)
            sender.equipment?.helmet = ItemStack(Material.AIR)
            sender.equipment?.chestplate = ItemStack(Material.AIR)
            sender.equipment?.leggings = ItemStack(Material.AIR)
            sender.equipment?.boots = ItemStack(Material.AIR)
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Ayo, you're in public you know that right? Anyway, you've been undressed ( ͡° ͜ʖ ͡°)"))
        }

        if (args[0].equals("hat", ignoreCase = true) && args.size == 1) {
            val item: ItemStack = sender.inventory.itemInMainHand
            sender.equipment?.helmet = item
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Changed hat!"))
        }

        if (args[0].equals("weatherclear", ignoreCase = true) && args.size == 1) {
            sender.world.weatherDuration = 0
            sender.world.setStorm(false)
            sender.world.isThundering = false
            sender.sendMessage(Colors.format(MessageType.SUCCESS + "Weather has been cleared!"))
        }

        if(args[0].equals("nightvision", ignoreCase = true)){
            val effect: PotionEffect? = sender.getPotionEffect(PotionEffectType.NIGHT_VISION)
            if (effect != null) {
                sender.removePotionEffect(PotionEffectType.NIGHT_VISION)
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "SUPER EYES DISABLED."))
            } else {
                sender.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 99999999, 1, true))
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "SUPER EYES ENABLED."))
            }
        }


        return false
    }
}