package me.m64diamondstar.ingeniamccore.general.inventory.utilities

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType

class TrashCanInventory(player: Player): InventoryHandler(IngeniaPlayer(player)) {

    override fun setDisplayName(): Component {
        return Component.text(Font.getGuiNegativeSpace(0) + "\uEBAE").color(
            TextColor.color(255,255,255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return false
    }

    override fun onClick(event: InventoryClickEvent) {
        if(event.isShiftClick && (event.whoClicked.inventory.firstEmpty() == -1 || event.whoClicked.inventory.firstEmpty() == 3)) event.isCancelled = true
        if(event.click == ClickType.NUMBER_KEY && event.hotbarButton in 0..3) event.isCancelled = true
        if(event.click == ClickType.WINDOW_BORDER_LEFT || event.click == ClickType.WINDOW_BORDER_RIGHT || event.clickedInventory == null) event.isCancelled = true
        if(event.clickedInventory?.type == InventoryType.PLAYER && (event.slot !in 4..8)) event.isCancelled = true
    }

    override fun onDrag(event: InventoryDragEvent) {
        if(event.rawSlots.all { it in 0..44 }) return
        if(event.rawSlots.all { it in 85..89 }) return
        event.isCancelled = true
    }

    override fun onOpen(event: InventoryOpenEvent) {}

    override fun onClose(event: InventoryCloseEvent) {
        (event.player as Player).playSound(event.player.location, Sound.BLOCK_DECORATED_POT_INSERT, 1f, 0.8f)
    }
}