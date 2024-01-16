package me.m64diamondstar.ingeniamccore.general.commands

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.world.inventory.MenuType
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TestGuiCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val gui = ChestGui(6, "test")
        gui.show(sender)

        val runnable = object: BukkitRunnable(){
            var c = 0
            override fun run() {

                val entityPlayer = (sender as CraftPlayer).handle
                val title = Component.Serializer.fromJson("{\"text\":\"counter: $c\"}")
                val packet = ClientboundOpenScreenPacket(entityPlayer.containerMenu.containerId, MenuType.GENERIC_9x6, title)
                entityPlayer.connection.send(packet)
                //entityPlayer.initInventoryMenu()
                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 3L)

        gui.setOnClose {
            runnable.cancel()
        }

        return false
    }
}