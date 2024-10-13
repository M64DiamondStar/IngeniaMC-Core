package me.m64diamondstar.ingeniamccore.shops.inventories

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.shops.Shop
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import java.text.NumberFormat
import java.util.*

class ConfirmInventory(player: Player, private val category: String, private val name: String, private val shopItemID: String): InventoryHandler(
    IngeniaPlayer(player)
) {

    private var amount = 1

    override fun setDisplayName(): Component {
        val shop = Shop(category, name)
        val type = shop.getShopItemType(shopItemID)
        return if(type != null && type.allowMultiple())
            Component.text("${Font.getGuiNegativeSpace(0)}\uEDA2").color(TextColor.color(255, 255, 255))
        else
            Component.text("${Font.getGuiNegativeSpace(0)}\uEDA1").color(TextColor.color(255, 255, 255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return true
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player

        if(event.slot == 20){
            ShopInventory(getPlayer().player, category, name).open()
        }

        if(event.slot == 24){
            val shop = Shop(category, name)

            player.closeInventory()
            // Check if player has enough money
            if(getPlayer().bal < shop.getPrice(shopItemID) * amount){
                val mainTitle = MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}>Purchase Failed")
                val subTitle = MiniMessage.miniMessage().deserialize("<${MessageType.LIGHT_ERROR}>Missing requirements")

                (player as Audience).showTitle(Title.title(mainTitle, subTitle))
                return
            }

            val itemID = shop.getItemID(shopItemID)
            val itemType = shop.getShopItemType(shopItemID)

            if(itemID == null || itemType == null){
                val mainTitle = MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}>ᴘᴜʀᴄʜᴀѕᴇ ꜰᴀɪʟᴇᴅ")
                val subTitle = MiniMessage.miniMessage().deserialize("<${MessageType.LIGHT_ERROR}>Internal error")

                (player as Audience).showTitle(Title.title(mainTitle, subTitle))
                return
            }

            if(!itemType.allowMultiple() && itemType.alreadyBought(player, itemID)){
                val mainTitle = MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}>ᴘᴜʀᴄʜᴀѕᴇ ꜰᴀɪʟᴇᴅ")
                val subTitle = MiniMessage.miniMessage().deserialize("<${MessageType.LIGHT_ERROR}>You can only buy this once")

                (player as Audience).showTitle(Title.title(mainTitle, subTitle))
                return
            }

            // Check if player has all requirements
            shop.getRequirements(shopItemID).forEach {
                val requirementType = shop.getRequirementType(shopItemID, it) ?: return@forEach
                if(!requirementType.isCompleted(player, shop.getRequirementValue(shopItemID, it) ?: "")){
                    val mainTitle = MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}>ᴘᴜʀᴄʜᴀѕᴇ ꜰᴀɪʟᴇᴅ")
                    val subTitle = MiniMessage.miniMessage().deserialize("<${MessageType.LIGHT_ERROR}>You don't meet the requirements")

                    (player as Audience).showTitle(Title.title(mainTitle, subTitle))
                    return
                }
            }

            // Purchase was successful
            getPlayer().bal -= shop.getPrice(shopItemID) * amount
            itemType.givePlayer(player, itemID, amount)
            player.sendMessage(Colors.format(MessageType.SUCCESS + "Purchase was successful!"))
        }

        // Change amount
        if(event.slot == 21 || event.slot == 23){
            val shop = Shop(category, name)
            val type = shop.getShopItemType(shopItemID)

            if(type != null && !type.allowMultiple()) return

            if(event.slot == 21 && amount > 1) amount--
            if(event.slot == 23 && amount < 64 && getPlayer().bal >= shop.getPrice(shopItemID) * (amount + 1)) amount++

            event.inventory.setItem(22, shop.getItemStack(shopItemID, getPlayer().player, amount))
            event.inventory.setItem(24, confirmItem(shop))
        }
    }

    override fun onDrag(event: InventoryDragEvent) {

    }

    override fun onOpen(event: InventoryOpenEvent) {
        val shop = Shop(category, name)
        val item = shop.getItemStack(shopItemID, getPlayer().player, 1)
        val type = shop.getShopItemType(shopItemID)
        val inventory = event.inventory

        if(item == null || type == null) {
            ShopInventory(getPlayer().player, category, name).open()
            return
        }

        inventory.setItem(22, item)

        if(shop.getShopItemType(shopItemID)!!.allowMultiple()){
            val add = ItemStack(Material.FEATHER)
            val addMeta = add.itemMeta!!
            addMeta.setDisplayName(Colors.format(MessageType.SUCCESS + "ᴀᴅᴅ ᴏɴᴇ"))
            addMeta.setCustomModelData(1)
            add.itemMeta = addMeta

            inventory.setItem(23, add)

            val reduce = ItemStack(Material.FEATHER)
            val reduceMeta = reduce.itemMeta!!
            reduceMeta.setDisplayName(Colors.format(MessageType.SUCCESS + "ʀᴇᴍᴏᴠᴇ ᴏɴᴇ"))
            reduceMeta.setCustomModelData(1)
            reduce.itemMeta = reduceMeta

            inventory.setItem(21, reduce)
        }

        inventory.setItem(24, confirmItem(shop))

        val cancel = ItemStack(Material.FEATHER)
        val cancelMeta = cancel.itemMeta!!
        cancelMeta.setDisplayName(Colors.format(MessageType.ERROR + "ᴄᴀɴᴄᴇʟ"))
        cancelMeta.lore = listOf(Colors.format("${MessageType.LORE}You'll be sent back to the shop page."))
        cancelMeta.setCustomModelData(1)
        cancel.itemMeta = cancelMeta

        inventory.setItem(20, cancel)
    }

    override fun onClose(event: InventoryCloseEvent) {}

    private fun confirmItem(shop: Shop): ItemStack {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val confirm = ItemStack(Material.FEATHER)
        val confirmMeta = confirm.itemMeta!!
        confirmMeta.setDisplayName(Colors.format(MessageType.SUCCESS + "ᴄᴏɴꜰɪʀᴍ"))
        confirmMeta.lore = listOf(Colors.format("&f:gs:${numberFormat.format(shop.getPrice(shopItemID) * amount)} ${MessageType.LORE}will be withdrawn from your balance."))
        confirmMeta.setCustomModelData(1)
        confirm.itemMeta = confirmMeta

        return confirm
    }
}