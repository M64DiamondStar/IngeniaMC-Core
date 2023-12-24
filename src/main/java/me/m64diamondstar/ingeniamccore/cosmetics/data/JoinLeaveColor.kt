package me.m64diamondstar.ingeniamccore.cosmetics.data

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import java.awt.Color

class JoinLeaveColor : DataConfiguration("data/cosmetic/joinleave", "COLORS.yml") {

    /**
     * Check if an ID exists
     * @param id the ID of the cosmetic
     */
    fun exists(id: String): Boolean {
        return this.getConfig().get(id) != null
    }

    /**
     * @param id the ID to give to the color
     * @param color the of this color
     * @param name the name of the color
     * @param override whether it should override already existing IDs
     * @return whether the operation was successful
     */
    fun create(id: String, color: String, name: String, override: Boolean): Boolean {
        if (this.getConfig().get(id) != null && !override)
            return false // Operation was cancelled because color already existed
        this.getConfig().set("$id.Data", color)
        this.getConfig().set("$id.Name", name)
        this.save()
        return true // Operation has been completed successfully
    }

    /**
     * Change the color of a cosmetic
     * @param id the ID of the cosmetic color
     * @param color the new color it should receive
     */
    fun setColor(id: String, color: String) {
        create(id, color, getName(id), true)
    }

    /**
     * Change the name of a cosmetic color
     * @param id the ID of the cosmetic color
     * @param name the new name it should receive
     */
    fun setName(id: String, name: String) {
        this.getConfig().set("$id.Name", name)
        this.save()
    }

    /**
     * Get the color of a cosmetic color
     * @param id the ID of the cosmetic color
     * @return the color
     */
    fun getColor(id: String): Color? {
        val color = this.getConfig().getString("$id.Data") ?: return null
        return Colors.getJavaColorFromHex(color)
    }

    /**
     * Get the name of a cosmetic color
     * @param id the ID of the cosmetic color
     * @return the color, if not found returns the ID
     */
    fun getName(id: String): String {
        return getConfig().getString("$id.Name") ?: id
    }

    /**
     * Get the hex value of a cosmetic color
     * @param id the ID of the cosmetic color
     */
    fun getHexColor(id: String): String? {
        return getConfig().getString("$id.Data")
    }

    /**
     * Delete a cosmetic color
     * @param id the ID of the cosmetic
     */
    fun delete(id: String) {
        this.getConfig().set(id, null)
        this.save()
    }

    /**
     * Get all the current IDs in this cosmetic file
     * @return the list of the IDs
     */
    fun getAllIDs(): List<String> {
        val list = ArrayList<String>()
        this.getConfig().getKeys(false).forEach { list.add(it) }
        return list
    }

    /**
     * Gets the color as a leather horse armor item stack with custom model data 4
     * @param id the ID of the cosmetic
     * @return the item
     */
    fun getItem(id: String): ItemStack? {
        if(!exists(id)) return null
        if(getColor(id) == null) return null
        val item = ItemStack(Material.LEATHER_HORSE_ARMOR)
        val meta = item.itemMeta!! as LeatherArmorMeta
        meta.setCustomModelData(4)
        meta.setDisplayName(Colors.format(getHexColor(id) + getName(id)))
        meta.lore = listOf(Colors.format(MessageType.DEFAULT + "Join/Leave Color: " + getHexColor(id)) + getHexColor(id))
        meta.setColor(org.bukkit.Color.fromRGB(getColor(id)!!.red, getColor(id)!!.green, getColor(id)!!.blue))
        item.itemMeta = meta
        return item
    }
}