package me.m64diamondstar.ingeniamccore.general.inventory

import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsInventory
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.warps.inventories.AttractionInventory
import me.m64diamondstar.ingeniamccore.warps.inventories.ShopInventory
import me.m64diamondstar.ingeniamccore.ranks.RankPerksInventory
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta

class MainInventory(player: IngeniaPlayer): Gui(player) {

    private val profileSlots = Array(9) {i -> if(i in  0..2) i + 3 else if(i in 3..5) i + 9 else i + 15}
    private val ridesSlots = Array(3) { i -> i + 27 }
    private val wardrobeSlots = Array(3) {i -> i + 30 }
    private val shopsSlots = Array(3) {i -> i + 33}
    private val rankSlots = Array(3) {i -> i + 39}

    override fun setDisplayName(): String {
        return Colors.format("&f${Font.getGuiNegativeSpace(0)}\uEC01${Font.getGuiNegativeSpace(1)}\uEB00")
    }

    override fun setSize(): Int {
        return 54
    }

    override fun setInventoryItems() {
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta

        transparentMeta.setDisplayName(Colors.format("#E7A300&lProfile"))
        transparentMeta.lore = getProfileLore()
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        profileSlots.forEach { inventory.setItem(it, transparentItem) }

        val playerHead = ItemStack(Material.PLAYER_HEAD)
        val playerMeta = playerHead.itemMeta as SkullMeta

        playerMeta.setDisplayName(Colors.format("#E7A300&lProfile"))

        playerMeta.lore = getProfileLore()
        playerMeta.setCustomModelData(20)
        playerMeta.owningPlayer = getPlayer().player

        playerHead.itemMeta = playerMeta

        inventory.setItem(13, playerHead)



        transparentMeta.setDisplayName(Colors.format("#DC7B1E&lRides"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view the available rides."))
        transparentItem.itemMeta = transparentMeta

        ridesSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.setDisplayName(Colors.format("#D09719&lWardrobe"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view your wardrobe."))
        transparentItem.itemMeta = transparentMeta

        wardrobeSlots.forEach { inventory.setItem(it, transparentItem) }


        transparentMeta.setDisplayName(Colors.format("#DEC716&lShops"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view the available park shops."))
        transparentItem.itemMeta = transparentMeta

        shopsSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format("#D25222&lRank Perks"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click to view your rank perks."))
        transparentItem.itemMeta = transparentMeta

        rankSlots.forEach { inventory.setItem(it, transparentItem) }


    }

    override fun handleInventory(event: InventoryClickEvent) {

        if(event.clickedInventory?.type == InventoryType.PLAYER) return

        if(wardrobeSlots.contains(event.slot)){
            val cosmeticsInventory = CosmeticsInventory(getPlayer().player, "國", 0)
            cosmeticsInventory.open()
        }

        if(ridesSlots.contains(event.slot)){
            val attractionInventory = AttractionInventory(getPlayer())
            attractionInventory.open()
        }

        if(shopsSlots.contains(event.slot)){
            val shopInventory = ShopInventory(getPlayer())
            shopInventory.open()
        }

        if(rankSlots.contains(event.slot)){
            val rankPerksInventory = RankPerksInventory(getPlayer().player)
            rankPerksInventory.open()
        }

    }

    private fun getProfileLore(): List<String>{
        val lore = ArrayList<String>()

        var expToNext = "${getPlayer().exp - LevelUtils.getExpRequirement(getPlayer().getLevel())}/" +
                "${LevelUtils.getExpRequirement(getPlayer().getLevel() + 1) - LevelUtils.getExpRequirement(getPlayer().getLevel())}"
        if(getPlayer().getLevel() == LevelUtils.getHighestLevel())
            expToNext = "At max level!"


        lore.add(Colors.format(MessageType.INGENIA + "» Name: &f" + getPlayer().name))
        lore.add(Colors.format(MessageType.INGENIA + "» Golden Stars: &f:gs:${getPlayer().bal}"))
        lore.add(Colors.format(MessageType.INGENIA + "» Level: &f${getPlayer().getLevel()}"))
        lore.add(Colors.format(MessageType.INGENIA + "» Total Exp: &f${getPlayer().exp}"))
        lore.add(Colors.format(MessageType.INGENIA + "» Exp Until Next Level: &f" + expToNext))
        lore.add(Colors.format(MessageType.INGENIA + "» Rank: &f" + getPlayer().prefix))
        lore.add(Colors.format(MessageType.INGENIA + "» Total Ridecount: &f" + AttractionUtils.getTotalRidecount(getPlayer().player)))
        return lore
    }

}