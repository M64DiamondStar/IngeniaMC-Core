package me.m64diamondstar.ingeniamccore.shops.inventories

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.shops.Shop
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.persistence.PersistentDataType

class ShopInventory(player: Player, private val category: String, private val name: String): InventoryHandler(IngeniaPlayer(player)) {

    private val itemSlots = Array(28) { i -> if(i in  0..6) i + 10 else if(i in 7..13) i + 12 else if(i in 14..20) i + 14 else i + 16}

    override fun setDisplayName(): Component {
        val shop = Shop(category, name)
        return Component.text("${Font.getGuiNegativeSpace(0)}${shop.skin ?: "\uED00"}").color(TextColor.color(255, 255, 255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun onClick(event: InventoryClickEvent) {
        if(event.currentItem != null && event.currentItem!!.hasItemMeta() &&
            event.currentItem!!.itemMeta!!.persistentDataContainer
                .has(NamespacedKey(IngeniaMC.plugin, "shop-item-id"), PersistentDataType.STRING)){

            if(!event.currentItem!!.itemMeta!!.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "can-buy"), PersistentDataType.BOOLEAN)!!) return

            val featureManager = FeatureManager()
            if(!featureManager.isFeatureEnabled(FeatureType.SHOPS) && !getPlayer().player.hasPermission("ingenia.admin")){
                getPlayer().player.sendMessage(Messages.featureDisabled())
                return
            }

            val confirmInventory = ConfirmInventory(getPlayer().player, category, name,
                event.currentItem!!.itemMeta!!.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "shop-item-id"), PersistentDataType.STRING)!!)
            confirmInventory.open()
        }
    }

    override fun onOpen(event: InventoryOpenEvent) {
        val shop = Shop(category, name)
        val inventory = event.inventory

        val slottedItems = ArrayList<String>() // Contains all items which have a slot configured
        val autoItems = ArrayList<String>() // All items that do not have a slot configured, so are placed in the first free slot

        shop.getAllShopIDs().forEach {
            if(shop.getSlot(it) != null)
                slottedItems.add(it)
            else
                autoItems.add(it)

        }

        slottedItems.forEach {
            val item = shop.getItemStack(it, getPlayer().player)
            inventory.setItem(shop.getSlot(it)!!, item)
        }

        autoItems.forEach {
            val item = shop.getItemStack(it, getPlayer().player)
            for(slot in itemSlots) {
                if (inventory.getItem(slot) == null) {
                    inventory.setItem(slot, item)
                    return@forEach
                }
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent) {}
}