package me.m64diamondstar.ingeniamccore.cosmetics.inventory

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.general.player.data.BackpackPlayerConfig
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class BackpackInventory(player: Player, private val page: Int): InventoryHandler(IngeniaPlayer(player)) {

    companion object{
        val cooldown = HashMap<Player, Long>()
    }

    override fun setDisplayName(): Component {
        return Component.text(Font.getGuiNegativeSpace(0) + '\uEB10'.plus(page)).color(
            TextColor.color(255,255,255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return false
    }

    override fun onClick(event: InventoryClickEvent) {
        if(event.slot in 45..53 && event.clickedInventory?.type != InventoryType.PLAYER) {
            event.isCancelled = true
            if(cooldown.contains(getPlayer().player) && System.currentTimeMillis() - cooldown[getPlayer().player]!! < 2000 && !getPlayer().player.isOp) {
                getPlayer().sendMessage("Please slow down!", MessageType.ERROR)
                return
            }
            if(!event.cursor.isEmpty()) return
            val page = event.slot - 44
            val newInventory = BackpackInventory(getPlayer().player, page)
            newInventory.open()
            cooldown.put(getPlayer().player, System.currentTimeMillis())
        }

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

    override fun onOpen(event: InventoryOpenEvent) {
        val backpackPlayer = BackpackPlayerConfig(getPlayer().player)
        val inventory = event.inventory
        backpackPlayer.getPage(page).forEach {
            inventory.setItem(it.key, it.value)
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        val backpackPlayer = BackpackPlayerConfig(getPlayer().player)
        val inventory = event.inventory
        val map = mutableMapOf<Int, ItemStack>()
        for(i in 0..44){
            if(inventory.getItem(i) != null && inventory.getItem(i)!!.type != Material.AIR)
                map[i] = inventory.getItem(i)!!
        }
        backpackPlayer.setPage(page, map)
    }
}