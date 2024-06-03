package me.m64diamondstar.ingeniamccore.games.wandclash.gui

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class TeamChooseGui(val player: Player): InventoryHandler(IngeniaPlayer(player)) {
    override fun setDisplayName(): Component {
        return Component.text("${Font.getGuiNegativeSpace(0)}\uEB03").color(TextColor.color(255, 255, 255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return true
    }

    override fun onClick(event: InventoryClickEvent) {

    }

    override fun onDrag(event: InventoryDragEvent) {

    }

    override fun onOpen(event: InventoryOpenEvent) {

    }

    override fun onClose(event: InventoryCloseEvent) {}
}