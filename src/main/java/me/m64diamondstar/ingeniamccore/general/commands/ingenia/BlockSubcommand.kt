package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import com.jeff_media.customblockdata.CustomBlockData
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class BlockSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        val player = sender

        when(args[1].lowercase()){
            "trashcan" -> {
                if(args[2].equals("add", ignoreCase = true)){
                    if(args[2].equals("add", ignoreCase = true) ||
                        args[2].equals("remove", ignoreCase = true)){

                        val block = player.getTargetBlockExact(5)
                        if(block == null || block.type == Material.AIR){
                            player.sendMessage(Colors.format(MessageType.ERROR + "Please look at a block to set the location."))
                            return
                        }

                        val data = CustomBlockData(block, IngeniaMC.plugin)

                        if(args[2].equals("add", ignoreCase = true)) {
                            data.set(NamespacedKey(IngeniaMC.plugin, "trash-can"), PersistentDataType.BOOLEAN, true)
                            player.sendMessage(Colors.format(MessageType.SUCCESS + "This block is now allowed to interact with."))
                        }

                        else{
                            data.remove(NamespacedKey(IngeniaMC.plugin, "trash-can"))
                            player.sendMessage(Colors.format(MessageType.SUCCESS + "This block is not allowed to interact with anymore."))
                        }

                        val normalState = block.blockData
                        val location = block.location

                        player.sendBlockChange(location, Material.ORANGE_STAINED_GLASS.createBlockData())

                        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                            player.sendBlockChange(location, normalState)}, 10L)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                            player.sendBlockChange(location, Material.ORANGE_STAINED_GLASS.createBlockData())}, 20L)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                            player.sendBlockChange(location, normalState)}, 30L)

                    }
                }
            }
        }
    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if(args.size == 2) {
            tabs.add("trashcan")
        }
        else if(args.size == 3){
            tabs.add("add")
            tabs.add("remove")
        }

        return tabs
    }
}