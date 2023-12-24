package me.m64diamondstar.ingeniamccore.ranks

import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.ranks.joinleavemessage.JoinLeaveInventory
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class RankPerksInventory(val player: Player) : Gui(IngeniaPlayer(player)) {

    private val joinLeaveSlots = Array(10) {i -> if(i in  0..4) i + 2 else i + 6}
    private val fireworkSlots = Array(10) {i -> if(i in  0..4) i + 20 else i + 24}
    private val passiveSlots = Array(10) {i -> if(i in  0..4) i + 38 else i + 42}
    private val viewStore = Array(4) {i -> if(i in  0..1) i + 43 else i + 50}

    override fun setDisplayName(): String {
        return Colors.format("&f\uF808を")
    }

    override fun setSize(): Int {
        return 54
    }

    override fun handleInventory(event: InventoryClickEvent) {
        if(event.clickedInventory?.type == InventoryType.PLAYER) return
        val player = event.whoClicked as Player

        if(joinLeaveSlots.contains(event.slot)){
            if(!player.hasPermission("ingenia.vip") && !player.hasPermission("ingenia.vip+")){
                player.sendMessage(Colors.format(MessageType.ERROR + "You need VIP or VIP+ to use this feature!"))
                return
            }
            val joinLeaveInventory = JoinLeaveInventory(player, JoinLeaveInventory.Selected.JOIN)
            joinLeaveInventory.open()
        }

        if(fireworkSlots.contains(event.slot)){
            if(!player.hasPermission("ingenia.vip") && !player.hasPermission("ingenia.vip+")){
                player.sendMessage(Colors.format(MessageType.ERROR + "You need VIP or VIP+ to use this feature!"))
                return
            }
            // firework inventory
        }

        if(viewStore.contains(event.slot)){
            (player as Audience).sendMessage(
                MiniMessage.miniMessage().deserialize("<#f4b734>Click <reset><hover:show_text:\"Click me!\"><click:open_url:\"https://store.ingeniamc.net\">here<reset> <#f4b734>to view the store!")
            )
            player.closeInventory()
        }

        if(event.slot == 45){
            val mainInventory = MainInventory(getPlayer())
            mainInventory.open()
        }
    }

    override fun setInventoryItems() {
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta

        transparentMeta.setCustomModelData(1)


        transparentMeta.setDisplayName(Colors.format("#4b85e3&lCustomize Join/Leave Message"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.ERROR + "&oRequires VIP or VIP+"),
            Colors.format(MessageType.BACKGROUND + ""),
            Colors.format(MessageType.BACKGROUND + "Choose in which mood you want to enter"),
            Colors.format(MessageType.BACKGROUND + "or leave the server!")
        )

        transparentItem.itemMeta = transparentMeta

        joinLeaveSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format("#d4873b&lCustomize Join Firework"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.ERROR + "&oRequires VIP to enable or disable"),
            Colors.format(MessageType.ERROR + "&oRequires VIP+ to customize"),
            Colors.format(MessageType.BACKGROUND + ""),
            Colors.format(MessageType.BACKGROUND + "Spawn in with some crazy effects"),
            Colors.format(MessageType.BACKGROUND + "which are even customizable with VIP+!")
        )

        transparentItem.itemMeta = transparentMeta

        fireworkSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format("#5eab59&lPassive Perks"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.BACKGROUND + "Soon..."),
        )

        transparentItem.itemMeta = transparentMeta

        passiveSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lView Store"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.BACKGROUND + "Click here to view the store!"),
        )

        transparentItem.itemMeta = transparentMeta

        viewStore.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lGo Back"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to go back to the main menu."))

        transparentItem.itemMeta = transparentMeta

        inventory.setItem(45, transparentItem)
    }
}