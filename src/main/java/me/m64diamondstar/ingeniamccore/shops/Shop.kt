package me.m64diamondstar.ingeniamccore.shops

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.shops.utils.ItemRequirement
import me.m64diamondstar.ingeniamccore.shops.utils.ShopItem
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.text.NumberFormat
import java.util.*

class Shop(val category: String, val name: String): DataConfiguration("shops/$category", name) {

    /**
     * Sets the display name of the shop
     */
    var displayName: String
        set(displayName){
            this.getConfig().set("DisplayName", displayName)
            this.save()
        }
        get() {
            return this.getConfig().getString("DisplayName") ?: name
        }

    /**
     * Sets the skin of the shop
     */
    var skin: String?
        set(skin){
            this.getConfig().set("Skin", skin)
            this.save()
        }
        get() {
            return this.getConfig().getString("Skin")
        }

    /**
     * Adds an item to the shop.
     * @param shopId the ID of the shop item.
     * @param itemType the type of the item.
     * @param itemId the ID of the item from the ShopItem.
     */
    fun addItem(shopId: String, itemType: ShopItem, itemId: String) {
        this.getConfig().set("Items.$shopId.Type", itemType.toString())
        this.getConfig().set("Items.$shopId.ItemID", itemId)
        this.getConfig().set("Items.$shopId.Price", 10)
        this.getConfig().set("Items.$shopId.Slot", "auto")
        this.save()
    }

    /**
     * Removes an item from the shop.
     * @param shopId the ID of the shop item.
     */
    fun deleteItem(shopId: String){
        this.getConfig().set("Items.$shopId", null)
        this.save()
    }

    /**
     * Gets all shop IDs.
     * @return all shop IDs.
     */
    fun getAllShopIDs(): List<String>{
        return this.getConfig().getConfigurationSection("Items")?.getKeys(false)?.toList() ?: emptyList()
    }

    /**
     * Gets the ID of an item.
     * @param shopId the ID of the shop item.
     * @return the ID of the item.
     */
    fun getItemID(shopId: String): String?{
        return this.getConfig().getString("Items.$shopId.ItemID")
    }

    /**
     * Gets the type of item.
     * @param shopId the ID of the shop item.
     * @return the type of the item.
     */
    fun getShopItemType(shopId: String): ShopItem?{
        return try{
            ShopItem.valueOf(this.getConfig().getString("Items.$shopId.Type") ?: return null)
        }catch (_: IllegalArgumentException){
            null
        }
    }

    /**
     * Sets the slot of an item.
     * @param shopId the ID of the shop item.
     * @param slot the slot of the item.
     */
    fun setSlot(shopId: String, slot: Int?){
        this.getConfig().set("Items.$shopId.Slot", slot)
        this.save()
    }

    /**
     * Gets the slot of an item.
     * @param shopId the ID of the shop item.
     * @return the slot of the item.
     */
    fun getSlot(shopId: String): Int?{
        return this.getConfig().getString("Items.$shopId.Slot")?.toIntOrNull()
    }

    /**
     * Sets the price of an item.
     * @param shopId the ID of the shop item.
     * @param price the price of the item.
     */
    fun setPrice(shopId: String, price: Long?){
        this.getConfig().set("Items.$shopId.Price", price ?: 999999999)
        this.save()
    }

    /**
     * Gets the price of an item.
     * @param shopId the ID of the shop item.
     * @return the price of the item.
     */
    fun getPrice(shopId: String): Long{
        return this.getConfig().getLong("Items.$shopId.Price")
    }

    /**
     * Gets the item stack of a shop item.
     * Used for displaying the item in the shop.
     */
    fun getItemStack(shopId: String, player: Player, amount: Int): ItemStack?{
        val ingeniaPlayer = IngeniaPlayer(player)

        val item = getShopItemType(shopId)?.getAsItemStack(getItemID(shopId) ?: return null) ?: return null
        val meta = item.itemMeta!!

        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val lore = meta.lore?.map { it.replace("%player%", player.name) }?.toMutableList() ?: ArrayList()
        lore.add(Colors.format(MessageType.TYPE + "&o") + getShopItemType(shopId)!!.getDisplayName())

        var hasAllRequirements = true

        if(getShopItemType(shopId) == null) return null

        // Player hasn't bought this item yet
        if(!getShopItemType(shopId)!!.allowMultiple() && !getShopItemType(shopId)!!.alreadyBought(player, getItemID(shopId) ?: "")){
            lore.add("")
            lore.add(Colors.format(MessageType.LIGHT_GRAY) + "Price:")
            lore.add(Colors.format("&f${if(ingeniaPlayer.bal >= getPrice(shopId)) Font.Characters.ALLOW else Font.Characters.DENY}${MessageType.DEFAULT} &f:gs:${MessageType.DEFAULT}" + numberFormat.format(getPrice(shopId) * amount)))

            var addedRequirementsLore = false
            if(ingeniaPlayer.bal < getPrice(shopId)) hasAllRequirements = false
            getRequirements(shopId).forEach {
                if(!addedRequirementsLore){
                    lore.add("")
                    lore.add(Colors.format(MessageType.LIGHT_GRAY) + "Requirements:")
                    addedRequirementsLore = true
                }
                val display = getRequirementType(shopId, it)?.getDisplay(getRequirementValue(shopId, it) ?: "") ?: ""
                val hasRequirement = getRequirementType(shopId, it)?.isCompleted(player, getRequirementValue(shopId, it) ?: "") ?: false
                lore.add(Colors.format("&f${if(hasRequirement) Font.Characters.ALLOW else Font.Characters.DENY}${MessageType.DEFAULT} ${MessageType.DEFAULT}" + display))
                if(!hasRequirement) hasAllRequirements = false
            }
        }
        // Player can buy multiple of this item
        else if(getShopItemType(shopId)!!.allowMultiple()){
            lore.add("")
            lore.add(Colors.format(MessageType.LIGHT_GRAY) + "Price:")
            lore.add(Colors.format("&f${if(ingeniaPlayer.bal >= getPrice(shopId)) Font.Characters.ALLOW else Font.Characters.DENY}${MessageType.DEFAULT} &f:gs:${MessageType.DEFAULT}" + numberFormat.format(getPrice(shopId) * amount)))

            var addedRequirementsLore = false
            if(ingeniaPlayer.bal < getPrice(shopId)) hasAllRequirements = false
            getRequirements(shopId).forEach {
                if(!addedRequirementsLore){
                    lore.add("")
                    lore.add(Colors.format(MessageType.LIGHT_GRAY) + "Requirements:")
                    addedRequirementsLore = true
                }
                val display = getRequirementType(shopId, it)?.getDisplay(getRequirementValue(shopId, it) ?: "") ?: ""
                val hasRequirement = getRequirementType(shopId, it)?.isCompleted(player, getRequirementValue(shopId, it) ?: "") ?: false
                lore.add(Colors.format("&f${if(hasRequirement) Font.Characters.ALLOW else Font.Characters.DENY}${MessageType.DEFAULT} ${MessageType.DEFAULT}" + display))
                if(!hasRequirement) hasAllRequirements = false
            }
            lore.add("")
            lore.add(Colors.format(MessageType.DEFAULT + "&oYou can buy multiple of this item."))
        }
        // Player already bought this item, and it can only be purchased once
        else{
            lore.add("")
            lore.add(Colors.format(MessageType.LIGHT_ERROR + "You already have this item."))
            hasAllRequirements = false
        }

        meta.lore = lore

        ItemFlag.values().forEach { meta.addItemFlags(it) }

        meta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "shop-item-id"), PersistentDataType.STRING, shopId)
        meta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "can-buy"), PersistentDataType.BOOLEAN, hasAllRequirements)

        item.itemMeta = meta
        item.amount = amount
        return item
    }

    /**
     * Deletes the shop.
     */
    fun delete(){
        this.deleteFile()
    }

    /**
     * Adds a requirement to an item.
     * @param shopId the ID of the shop item.
     * @param requirement the requirement.
     * @param value the value of the requirement.
     * @param id the ID of the requirement.
     */
    fun addRequirement(shopId: String, requirement: ItemRequirement, id: String, value: String){
        this.getConfig().set("Items.$shopId.Requirements.$id.Type", requirement.toString())
        this.getConfig().set("Items.$shopId.Requirements.$id.Value", value)
        this.save()
    }

    /**
     * Removes a requirement from an item.
     * @param shopId the ID of the shop item.
     * @param id the ID of the requirement.
     */
    fun removeRequirement(shopId: String, id: String){
        this.getConfig().set("Items.$shopId.Requirements.$id", null)
        this.save()
    }

    /**
     * Gets the requirements of an item.
     * @param shopId the ID of the shop item.
     * @return the IDs requirements of the item.
     */
    fun getRequirements(shopId: String): List<String>{
        val list = ArrayList<String>()
        this.getConfig().getConfigurationSection("Items.$shopId.Requirements")?.getKeys(false)?.forEach {
            list.add(it)
        }
        return list
    }

    /**
     * Gets the value of a requirement.
     * @param shopId the ID of the shop item.
     * @param id the ID of the requirement.
     * @return the value of the requirement.
     */
    fun getRequirementValue(shopId: String, id: String): String?{
        return this.getConfig().getString("Items.$shopId.Requirements.$id.Value")
    }

    /**
     * Gets the type of requirement.
     * @param shopId the ID of the shop item.
     * @param id the ID of the requirement.
     * @return the type of the requirement.
     */
    fun getRequirementType(shopId: String, id: String): ItemRequirement?{
        return try{
            ItemRequirement.valueOf(this.getConfig().getString("Items.$shopId.Requirements.$id.Type") ?: return null)
        }catch (_: IllegalArgumentException){
            null
        }
    }



}