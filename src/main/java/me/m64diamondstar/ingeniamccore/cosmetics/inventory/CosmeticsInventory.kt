package me.m64diamondstar.ingeniamccore.cosmetics.inventory

import me.m64diamondstar.ingeniamccore.cosmetics.data.CosmeticItems
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class CosmeticsInventory(private var player: Player, private var selected: String, private val page: Int): InventoryHandler(IngeniaPlayer(player)) {

    private val freeSlots = Array(21) {i -> if(i in  0..6) i + 19 else if(i in 7..13) i + 21 else i + 23}

    private val previousPage = intArrayOf(18, 27, 36)
    private val nextPage = intArrayOf(26, 35, 44)

    override fun setDisplayName(): Component {
        return Component.text("\uF808田\uF81C\uF81A\uF819\uF811$selected").color(TextColor.color(255, 255, 255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return true
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val ingeniaPlayer = IngeniaPlayer(player)
        val cosmeticPlayer = CosmeticPlayer(player)
        val inventory = event.inventory

        if(event.slot == -999 || !event.view.title.contains("田")) return
        if(event.clickedInventory?.type == InventoryType.PLAYER) return

        if(event.slot == 49){
            val mainInventory = MainInventory(getPlayer(), 0)
            mainInventory.open()
            return
        }

        if(event.slot in 1..7 && event.slot != 4) {
            var newInventory = CosmeticsInventory(player, "國", 0)

            //All the different options

            when (event.slot) {
                1 -> { // Hats
                    newInventory = CosmeticsInventory(player, "國", 0)
                    if(event.isRightClick){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.HAT)
                        inventory.setItem(1, null)
                    }
                }
                2 -> { // Shirts
                    newInventory = CosmeticsInventory(player, "因", 0)
                    if(event.isRightClick){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.SHIRT)
                        inventory.setItem(2, null)
                    }
                }
                3 -> { // Wands
                    newInventory = CosmeticsInventory(player, "圖", 0)
                    if(event.isRightClick){
                        if(player.inventory.getItem(3) != null
                            && player.inventory.getItem(3)!!.type == Material.BLAZE_ROD
                            && player.inventory.getItem(3)!!.hasItemMeta())
                            player.inventory.setItem(3, null)
                        inventory.setItem(3, null)
                    }
                }
                5 -> { // Balloons
                    newInventory = CosmeticsInventory(player, "果", 0)
                    if(event.isRightClick){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.BALLOON)
                        inventory.setItem(5, null)
                    }
                }
                6 -> { // Pants
                    newInventory = CosmeticsInventory(player, "四", 0)
                    if(event.isRightClick){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.PANTS)
                        inventory.setItem(6, null)
                    }
                }
                7 -> { // Shoes
                    newInventory = CosmeticsInventory(player, "界", 0)
                    if(event.isRightClick){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.SHOES)
                        inventory.setItem(7, null)
                    }
                }
            }

            if(event.isLeftClick)
                newInventory.open()
        }

        if(nextPage.contains(event.slot) && event.currentItem != null){
            val cosmeticsInventory = CosmeticsInventory(player, selected, page + 1)
            cosmeticsInventory.open()
            return
        }

        if(previousPage.contains(event.slot) && event.currentItem != null){
            val cosmeticsInventory = CosmeticsInventory(player, selected, page - 1)
            cosmeticsInventory.open()
            return
        }

        when(selected){
            "國" -> { // Hats
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.HAT, CosmeticItems(CosmeticType.HAT).getID(event.currentItem!!)!!)
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                    inventory.setItem(1, addEquipmentLore(event.currentItem!!.clone(), "Hats"))
                }
            }

            "因" -> { // Shirts
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.SHIRT, CosmeticItems(CosmeticType.SHIRT).getID(event.currentItem!!)!!)
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                    inventory.setItem(2, addEquipmentLore(event.currentItem!!.clone(), "Shirts"))
                }
            }

            "圖" -> { // Wands
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    ingeniaPlayer.setWand(event.currentItem)
                    inventory.setItem(3, addEquipmentLore(event.currentItem!!.clone(), "Wands"))
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                }
            }

            "果" -> { // Balloons

            }

            "四" -> { // Pants
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.PANTS, CosmeticItems(CosmeticType.PANTS).getID(event.currentItem!!)!!)
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                    inventory.setItem(6, addEquipmentLore(event.currentItem!!.clone(), "Pants"))
                }
            }

            "界" -> { // Shoes
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.SHOES, CosmeticItems(CosmeticType.SHOES).getID(event.currentItem!!)!!)
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                    inventory.setItem(7, addEquipmentLore(event.currentItem!!.clone(), "Shoes"))
                }
            }
        }
    }

    override fun onDrag(event: InventoryDragEvent) {

    }

    override fun onOpen(event: InventoryOpenEvent) {
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        val inventory = event.inventory
        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lGo Back"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to go back to the main menu."))
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        inventory.setItem(49, transparentItem)

        if(player.inventory.getItem(3) != null
            && player.inventory.getItem(3)!!.type == Material.BLAZE_ROD
            && player.inventory.getItem(3)!!.hasItemMeta()){
            inventory.setItem(3, addEquipmentLore(player.inventory.getItem(3)!!.clone(), "Wands"))
        }

        val cosmeticPlayer = CosmeticPlayer(player)
        inventory.setItem(1,
            addEquipmentLore(CosmeticItems(CosmeticType.HAT).getItem(cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.HAT)),
                "Hats"))
        inventory.setItem(2,
            addEquipmentLore(CosmeticItems(CosmeticType.SHIRT).getItem(cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.SHIRT)),
                "Shirts"))

        inventory.setItem(5,
            addEquipmentLore(CosmeticItems(CosmeticType.BALLOON).getItem(cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.BALLOON)),
                "Balloons"))
        inventory.setItem(6,
            addEquipmentLore(CosmeticItems(CosmeticType.PANTS).getItem(cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.PANTS)),
                "Pants"))
        inventory.setItem(7,
            addEquipmentLore(CosmeticItems(CosmeticType.SHOES).getItem(cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.SHOES)),
                "Shoes"))

        when(selected){
            "國" -> addCosmetics(CosmeticType.HAT, inventory)
            "因" -> addCosmetics(CosmeticType.SHIRT, inventory)
            "圖" -> addWands(inventory)
            "果" -> addCosmetics(CosmeticType.BALLOON, inventory)
            "四" -> addCosmetics(CosmeticType.PANTS, inventory)
            "界" -> addCosmetics(CosmeticType.SHOES, inventory)
        }
    }

    override fun onClose(event: InventoryCloseEvent) {

    }

    /**
     * Run when wand gui is chosen.
     **/
    private fun addWands(inventory: Inventory){
        val ingeniaPlayer = IngeniaPlayer(player)

        for(i in 0 until ingeniaPlayer.wands.size){
            inventory.setItem(freeSlots[i], IngeniaPlayer(player).wands[i])
        }
    }

    private fun addCosmetics(cosmeticType: CosmeticType, inventory: Inventory){
        val cosmeticPlayer = CosmeticPlayer(player)
        if(cosmeticPlayer.getAllCosmeticsAsItemStack(cosmeticType).isEmpty())
            return

        var maxSize = cosmeticPlayer.getAllCosmetics(cosmeticType).size - freeSlots.size * page

        if(maxSize > freeSlots.size) {
            maxSize = freeSlots.size
            addNextButton(inventory)
        }

        if(page > 0) {
            addPreviousButton(inventory)
        }

        for(i in 0 until maxSize){
            val item = cosmeticPlayer.getAllCosmeticsAsItemStack(cosmeticType).toList()[i + freeSlots.size * page]
            inventory.setItem(freeSlots[i], item)

        }
    }

    private fun addNextButton(inventory: Inventory){
        val item = ItemStack(Material.FEATHER)
        val meta = item.itemMeta!!
        meta.setCustomModelData(1)
        meta.setDisplayName(Colors.format(MessageType.INGENIA + "&lNext Page"))
        item.itemMeta = meta
        nextPage.forEach { inventory.setItem(it, item) }
    }

    private fun addPreviousButton(inventory: Inventory){
        val item = ItemStack(Material.FEATHER)
        val meta = item.itemMeta!!
        meta.setCustomModelData(1)
        meta.setDisplayName(Colors.format(MessageType.INGENIA + "&lPrevious Page"))
        item.itemMeta = meta
        previousPage.forEach { inventory.setItem(it, item) }
    }

    private fun addEquipmentLore(itemStack: ItemStack?, cosmeticType: String): ItemStack?{
        val meta = itemStack?.itemMeta ?: return null
        meta.setDisplayName(Colors.format(MessageType.INGENIA + cosmeticType))
        meta.lore = listOf("",
            Colors.format(MessageType.LORE + "Left Click to change"),
            Colors.format(MessageType.LORE + "Right Click to un-equip"))
        itemStack.itemMeta = meta
        return itemStack
    }

}