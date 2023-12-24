package me.m64diamondstar.ingeniamccore.cosmetics.inventory

import me.m64diamondstar.ingeniamccore.cosmetics.data.CosmeticItems
import me.m64diamondstar.ingeniamccore.cosmetics.data.CosmeticLore
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class CosmeticsInventory(private var player: Player, private var selected: String): Gui(IngeniaPlayer(player)) {

    private val freeSlots = Array(21) {i -> if(i in  0..6) i + 19 else if(i in 7..13) i + 21 else i + 23}

    override fun setDisplayName(): String {
        return Colors.format("\uF808&f田\uF81C\uF81A\uF819\uF811$selected")
    }

    override fun setSize(): Int {
        return 54
    }

    override fun handleInventory(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val ingeniaPlayer = IngeniaPlayer(player)
        val cosmeticPlayer = CosmeticPlayer(player)

        if(event.slot == -999 || !event.view.title.contains("田")) return
        if(event.clickedInventory?.type == InventoryType.PLAYER) return

        if(event.slot == 49){
            val mainInventory = MainInventory(getPlayer())
            mainInventory.open()
            return
        }

        if(event.slot in 1..7 && event.slot != 4) {
            var inventory = CosmeticsInventory(player, "國")

            //All the different options

            when (event.slot) {
                1 -> inventory = CosmeticsInventory(player, "國") // Hats
                2 -> inventory = CosmeticsInventory(player, "因") // Shirts
                3 -> inventory = CosmeticsInventory(player, "圖") // Wands
                5 -> inventory = CosmeticsInventory(player, "果") // Balloons
                6 -> inventory = CosmeticsInventory(player, "四") // Pants
                7 -> inventory = CosmeticsInventory(player, "界") // Boots
            }

            inventory.open()
        }

        when(selected){
            "國" -> { // Hats
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    val currentHatID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.HAT)
                    val hatID = CosmeticItems(CosmeticType.HAT).getID(event.currentItem!!) ?: return

                    if(currentHatID == hatID){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.HAT)
                        addHats()
                        return
                    }

                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.HAT, CosmeticItems(CosmeticType.HAT).getID(event.currentItem!!)!!)
                    addHats()
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                }
            }

            "因" -> { // Shirts
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    val currentShirtID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.SHIRT)
                    val shirtID = CosmeticItems(CosmeticType.SHIRT).getID(event.currentItem!!) ?: return

                    if(currentShirtID == shirtID){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.SHIRT)
                        addShirts()
                        return
                    }

                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.SHIRT, CosmeticItems(CosmeticType.SHIRT).getID(event.currentItem!!)!!)
                    addShirts()
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                }
            }

            "圖" -> { // Wands
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    ingeniaPlayer.setWand(event.currentItem)
                    inventory.setItem(3, event.currentItem)
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                }
            }

            "果" -> { // Balloons

            }

            "四" -> { // Pants
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    val currentPantsID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.PANTS)
                    val pantsID = CosmeticItems(CosmeticType.PANTS).getID(event.currentItem!!) ?: return

                    if(currentPantsID == pantsID){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.PANTS)
                        addPants()
                        return
                    }

                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.PANTS, CosmeticItems(CosmeticType.PANTS).getID(event.currentItem!!)!!)
                    addPants()
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                }
            }

            "界" -> { // Shoes
                if (freeSlots.contains(event.slot) && event.currentItem != null) {
                    val currentShoesID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.SHOES)
                    val shoesID = CosmeticItems(CosmeticType.SHOES).getID(event.currentItem!!) ?: return

                    if(currentShoesID == shoesID){
                        cosmeticPlayer.getEquipment().unEquipCosmetic(CosmeticType.SHOES)
                        addShoes()
                        return
                    }

                    cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.SHOES, CosmeticItems(CosmeticType.SHOES).getID(event.currentItem!!)!!)
                    addShoes()
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
                }
            }
        }
    }

    override fun setInventoryItems() {

        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lGo Back"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to go back to the main menu."))
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        inventory.setItem(49, transparentItem)

        if(player.inventory.getItem(5) != null
            && player.inventory.getItem(5)!!.type == Material.BLAZE_ROD
            && player.inventory.getItem(5)!!.hasItemMeta()){
            inventory.setItem(3, player.inventory.getItem(5))
        }

        when(selected){
            "國" -> addHats()
            "因" -> addShirts()
            "圖" -> addWands()
            //"果" -> addBalloons()
            "四" -> addPants()
            "界" -> addShoes()
        }
    }

    /**
     * Run when wand gui is chosen.
     **/
    private fun addWands(){
        val ingeniaPlayer = IngeniaPlayer(player)

        for(i in 0 until ingeniaPlayer.wands.size){
            inventory.setItem(freeSlots[i], IngeniaPlayer(player).wands[i])
        }
    }

    /**
     * Run when hat gui is chosen.
     */
    private fun addHats(){
        val cosmeticPlayer = CosmeticPlayer(player)
        val currentHatID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.HAT)
        if(cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.HAT).isEmpty())
            return

        for(i in 0 until cosmeticPlayer.getAllCosmetics(CosmeticType.HAT).size){
            val item = cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.HAT).toList()[i]
            inventory.setItem(freeSlots[i],
                if(currentHatID == cosmeticPlayer.getAllCosmetics(CosmeticType.HAT).toList()[i])
                    CosmeticLore.getAsDeselect(item)
                else
                    CosmeticLore.getAsSelect(item))

        }
    }

    /**
     * Run when shirt gui is chosen.
     */
    private fun addShirts(){
        val cosmeticPlayer = CosmeticPlayer(player)
        val currentShirtID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.SHIRT)
        if(cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.SHIRT).isEmpty())
            return

        for(i in 0 until cosmeticPlayer.getAllCosmetics(CosmeticType.SHIRT).size){
            val item = cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.SHIRT).toList()[i]
            inventory.setItem(freeSlots[i],
                if(currentShirtID == cosmeticPlayer.getAllCosmetics(CosmeticType.SHIRT).toList()[i])
                    CosmeticLore.getAsDeselect(item)
                else
                    CosmeticLore.getAsSelect(item))
        }
    }

    /**
     * Run when pants gui is chosen.
     */
    private fun addPants(){
        val cosmeticPlayer = CosmeticPlayer(player)
        val currentPantsID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.PANTS)
        if(cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.PANTS).isEmpty())
            return

        for(i in 0 until cosmeticPlayer.getAllCosmetics(CosmeticType.PANTS).size){
            val item = cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.PANTS).toList()[i]
            inventory.setItem(freeSlots[i],
                if(currentPantsID == cosmeticPlayer.getAllCosmetics(CosmeticType.PANTS).toList()[i])
                    CosmeticLore.getAsDeselect(item)
                else
                    CosmeticLore.getAsSelect(item))
        }
    }

    /**
     * Run when boots gui is chosen.
     */
    private fun addShoes(){
        val cosmeticPlayer = CosmeticPlayer(player)
        val currentShoesID = cosmeticPlayer.getEquipment().getEquippedId(CosmeticType.SHOES)
        if(cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.SHOES).isEmpty())
            return

        for(i in 0 until cosmeticPlayer.getAllCosmetics(CosmeticType.SHOES).size){
            val item = cosmeticPlayer.getAllCosmeticsAsItemStack(CosmeticType.SHOES).toList()[i]
            inventory.setItem(freeSlots[i],
                if(currentShoesID == cosmeticPlayer.getAllCosmetics(CosmeticType.SHOES).toList()[i])
                    CosmeticLore.getAsDeselect(item)
                else
                    CosmeticLore.getAsSelect(item))
        }
    }

}