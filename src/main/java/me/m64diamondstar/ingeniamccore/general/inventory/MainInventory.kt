package me.m64diamondstar.ingeniamccore.general.inventory

import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.cosmetics.inventory.BackpackInventory
import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsInventory
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.general.inventory.settings.SettingsInventory
import me.m64diamondstar.ingeniamccore.ranks.RankPerksInventory
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.warps.inventories.AttractionInventory
import me.m64diamondstar.ingeniamccore.warps.inventories.ShopInventory
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta

class MainInventory(player: IngeniaPlayer, private val version: Int): InventoryHandler(player) {

    private val profileSlots = if(version == 0) arrayOf(3, 4, 5, 12, 13, 14, 21, 22, 23) else if(version == 1) arrayOf(14, 15, 16, 23, 24, 25, 32, 33, 34) else arrayOf()
    private val ridesSlots = if(version == 0) arrayOf(27, 28, 29) else if(version == 1) arrayOf(0, 1, 2, 3) else arrayOf()
    private val shopsSlots = if(version == 0) arrayOf(33, 34, 35) else if(version == 1) arrayOf(9, 10, 11, 12) else arrayOf()
    private val wardrobeSlots = if(version == 0) arrayOf(30, 31, 32) else if(version == 1) arrayOf(18, 19, 20, 21) else arrayOf()
    private val backpackSlots = if(version == 0) arrayOf() else if(version == 1) arrayOf(27, 28, 29, 30) else arrayOf()
    private val questSlots = if(version == 0) arrayOf() else if(version == 1) arrayOf(36, 37, 38, 39) else arrayOf()
    private val rankSlots = if(version == 0) arrayOf(39, 40, 41) else if(version == 1) arrayOf(45, 46, 47, 48) else arrayOf()

    override fun setDisplayName(): Component {
        return Component.text("${Font.getGuiNegativeSpace(0)}\uEC02${Font.getGuiNegativeSpace(1)}" + '\uEB00'.plus(version)).color(
            TextColor.color(255,255,255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return true
    }

    override fun onClick(event: InventoryClickEvent) {
        if(event.clickedInventory?.type == InventoryType.PLAYER) return

        if(ridesSlots.contains(event.slot)){
            val attractionInventory = AttractionInventory(getPlayer())
            attractionInventory.open()
        }

        if(shopsSlots.contains(event.slot)){
            val shopInventory = ShopInventory(getPlayer())
            shopInventory.open()
        }

        if(wardrobeSlots.contains(event.slot)){
            val cosmeticsInventory = CosmeticsInventory(getPlayer().player, "\uEB21", 0)
            cosmeticsInventory.open()
        }

        if(backpackSlots.contains(event.slot)){
            val backpackInventory = BackpackInventory(getPlayer().player, 1)
            backpackInventory.open()
        }

        if(questSlots.contains(event.slot)){
            //val questInventory = QuestInventory(getPlayer().player)
            //questInventory.open()
        }

        if(rankSlots.contains(event.slot)){
            val rankPerksInventory = RankPerksInventory(getPlayer().player)
            rankPerksInventory.open()
        }

        if(event.slot == 53){
            val settingsInventory = SettingsInventory(getPlayer())
            settingsInventory.open()
        }
    }

    override fun onDrag(event: InventoryDragEvent) {

    }

    override fun onOpen(event: InventoryOpenEvent) {
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        val inventory = event.inventory

        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#E7A300><b>ᴘʀᴏꜰɪʟᴇ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(getProfileLore())
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        profileSlots.forEach { inventory.setItem(it, transparentItem) }

        val playerHead = ItemStack(Material.PLAYER_HEAD)
        val playerMeta = playerHead.itemMeta as SkullMeta

        playerMeta.displayName(MiniMessage.miniMessage().deserialize("<#E7A300><b>ᴘʀᴏꜰɪʟᴇ").decoration(TextDecoration.ITALIC, false))
        playerMeta.lore(getProfileLore())
        playerMeta.setCustomModelData(20)
        playerMeta.owningPlayer = getPlayer().player

        playerHead.itemMeta = playerMeta

        inventory.setItem(if(version == 0) 13 else 24, playerHead)



        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#E7A300><b>ʀɪᴅᴇѕ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Click to view the available rides.").decoration(TextDecoration.ITALIC, false)))
        transparentItem.itemMeta = transparentMeta

        ridesSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#E7A300><b>ᴡᴀʀᴅʀᴏʙᴇ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Click to view your wardrobe.").decoration(TextDecoration.ITALIC, false)))
        transparentItem.itemMeta = transparentMeta

        wardrobeSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#E7A300><b>ʙᴀᴄᴋᴘᴀᴄᴋ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Click to open your backpack.").decoration(TextDecoration.ITALIC, false)))
        transparentItem.itemMeta = transparentMeta

        backpackSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#E7A300><b>ѕʜᴏᴘѕ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Click to view the available park shops.").decoration(TextDecoration.ITALIC, false)))
        transparentItem.itemMeta = transparentMeta

        shopsSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#E7A300><b>ʀᴀɴᴋ ᴘᴇʀᴋѕ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Click to view your rank perks.").decoration(TextDecoration.ITALIC, false)))
        transparentItem.itemMeta = transparentMeta

        rankSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#ff4d4d><b>ɴᴏᴛ ᴏᴜᴛ ʏᴇᴛ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Coming (hopefully) soon.").decoration(TextDecoration.ITALIC, false)))
        transparentItem.itemMeta = transparentMeta

        questSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<#bfbfbf><b>ѕᴇᴛᴛɪɴɢѕ").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Click to view your settings.").decoration(TextDecoration.ITALIC, false)))
        transparentItem.itemMeta = transparentMeta

        inventory.setItem(53, transparentItem)
    }

    override fun onClose(event: InventoryCloseEvent) {}

    private fun getProfileLore(): List<Component>{
        val lore = ArrayList<Component>()

        var expToNext = "${getPlayer().exp - LevelUtils.getExpRequirement(getPlayer().getLevel())}/" +
                "${LevelUtils.getExpRequirement(getPlayer().getLevel() + 1) - LevelUtils.getExpRequirement(getPlayer().getLevel())}"
        if(getPlayer().getLevel() == LevelUtils.getHighestLevel())
            expToNext = "At max level!"


        lore.add(MiniMessage.miniMessage().deserialize("<${MessageType.INGENIA}>» ɴᴀᴍᴇ: <white>" + getPlayer().name).decoration(TextDecoration.ITALIC, false))
        lore.add(MiniMessage.miniMessage().deserialize("<${MessageType.INGENIA}>» ɢᴏʟᴅᴇɴ ѕᴛᴀʀѕ: <white>\uE016${getPlayer().bal}").decoration(TextDecoration.ITALIC, false))
        lore.add(MiniMessage.miniMessage().deserialize("<${MessageType.INGENIA}>» ʟᴇᴠᴇʟ: <white>${getPlayer().getLevel()}").decoration(TextDecoration.ITALIC, false))
        lore.add(MiniMessage.miniMessage().deserialize("<${MessageType.INGENIA}>» ᴛᴏᴛᴀʟ ᴇxᴘ: <white>${getPlayer().exp}").decoration(TextDecoration.ITALIC, false))
        lore.add(MiniMessage.miniMessage().deserialize("<${MessageType.INGENIA}>» ᴇxᴘ ᴜɴᴛɪʟ ɴᴇxᴛ ʟᴇᴠᴇʟ: <white>" + expToNext).decoration(TextDecoration.ITALIC, false))
        lore.add(MiniMessage.miniMessage().deserialize("<${MessageType.INGENIA}>» ʀᴀɴᴋ: <white>").append(getPlayer().componentIconPrefix.color(TextColor.color(255, 255, 255))).decoration(TextDecoration.ITALIC, false))
        lore.add(MiniMessage.miniMessage().deserialize("<${MessageType.INGENIA}>» ᴛᴏᴛᴀʟ ʀɪᴅᴇᴄᴏᴜɴᴛ: <white>" + AttractionUtils.getTotalRidecount(getPlayer().player)).decoration(TextDecoration.ITALIC, false))
        return lore
    }

}