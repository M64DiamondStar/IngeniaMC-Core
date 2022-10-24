package me.m64diamondstar.ingeniamccore.general.inventory

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class MainInventory(player: IngeniaPlayer): Gui(player) {

    private val profileSlots = Array(12) {i -> if(i in  0..3) i else if(i in 4..7) i + 5 else i + 10}
    private val achievementSlots = Array(4) {i -> i + 27}
    private val rankSlots = Array(4) {i -> i + 36}
    private val settingsSlots = Array(4) {i -> i + 45}

    override fun setDisplayName(): String {
        return Colors.format("&f\uF808手")
    }

    override fun setSize(): Int {
        return 54
    }

    override fun setInventoryItems() {
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta

        transparentMeta.setDisplayName(Colors.format("#4b85e3&lProfile"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view your profile."))
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        profileSlots.forEach { inventory.setItem(it, transparentItem) }

        val playerHead = ItemStack(Material.PLAYER_HEAD)
        val playerMeta = playerHead.itemMeta as SkullMeta

        playerMeta.setDisplayName(Colors.format("#4b85e3&lProfile"))
        playerMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view your profile."))
        playerMeta.setCustomModelData(1)
        playerMeta.owningPlayer = getPlayer().player

        playerHead.itemMeta = playerMeta

        inventory.setItem(10, playerHead)



        transparentMeta.setDisplayName(Colors.format("#5eab59&lAchievements"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view your achievements."))
        transparentItem.itemMeta = transparentMeta

        achievementSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.setDisplayName(Colors.format("#9659ab&lRank Perks"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view your rank perks."))
        transparentItem.itemMeta = transparentMeta

        rankSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.setDisplayName(Colors.format("#9c9c9c&lSettings"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view your server settings."))
        transparentItem.itemMeta = transparentMeta

        settingsSlots.forEach { inventory.setItem(it, transparentItem) }


    }

    override fun handleInventory(event: InventoryClickEvent) {

    }

}