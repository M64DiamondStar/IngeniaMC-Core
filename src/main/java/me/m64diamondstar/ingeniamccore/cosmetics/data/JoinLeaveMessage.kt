package me.m64diamondstar.ingeniamccore.cosmetics.data

import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType
import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class JoinLeaveMessage(private val messageType: MessageType): DataConfiguration("data/cosmetic/joinleave", "$messageType.yml") {

    /**
     * Check if an ID exists
     * @param id the ID of the cosmetic
     */
    fun exists(id: String): Boolean {
        return this.getConfig().get(id) != null
    }

    /**
     * @param id the ID to give to the message
     * @param message the full message
     * @param override whether it should override already existing IDs
     * @return whether the operation was successful
     */
    fun create(id: String, message: String, name: String, override: Boolean): Boolean {
        if (this.getConfig().get(id) != null && !override)
            return false // Operation was cancelled because color already existed
        this.getConfig().set("$id.Data", message)
        this.getConfig().set("$id.Name", name)
        this.save()
        return true // Operation has been completed successfully
    }

    /**
     * Change the message
     * @param id the ID of the message
     * @param message the new message it should receive
     */
    fun setMessage(id: String, message: String) {
        create(id, message, getName(id), true)
    }

    /**
     * Change the name
     * @param id the ID of the message
     * @param name the new name it should receive
     */
    fun setName(id: String, name: String) {
        this.getConfig().set("$id.Name", name)
        this.save()
    }

    /**
     * Get the message
     * @param id the ID of the message
     * @return the message
     */
    fun getMessage(id: String): String? {
        return getConfig().getString("$id.Data")
    }

    /**
     * Get the name
     * @param id the ID of the message
     * @return the name
     */
    fun getName(id: String): String {
        return getConfig().getString("$id.Name") ?: id
    }

    /**
     * Delete a message
     * @param id the ID of the message
     */
    fun delete(id: String) {
        this.getConfig().set(id, null)
        this.save()
    }

    /**
     * Get all the current IDs in this message file
     * @return the list of the IDs
     */
    fun getAllIDs(): List<String> {
        val list = ArrayList<String>()
        this.getConfig().getKeys(false).forEach { list.add(it) }
        return list
    }

    /**
     * Gets the message as a feather item stack with custom model data 2
     * when it's a join message and 3 when it's a leave message
     * @param id the ID of the cosmetic
     * @return the item
     */
    fun getItem(id: String): ItemStack? {
        if(!exists(id)) return null
        val item = ItemStack(Material.FEATHER)
        val meta = item.itemMeta!!
        when(messageType){
            MessageType.JOIN -> {
                meta.setCustomModelData(2)
                meta.setDisplayName(Colors.format(me.m64diamondstar.ingeniamccore.utils.messages.MessageType.INFO + "Join Message &l" +
                        me.m64diamondstar.ingeniamccore.utils.messages.MessageType.DEFAULT + getName(id)))
            }

            MessageType.LEAVE -> {
                meta.setCustomModelData(3)
                meta.setDisplayName(Colors.format(me.m64diamondstar.ingeniamccore.utils.messages.MessageType.INFO + "Leave Message &l" +
                        me.m64diamondstar.ingeniamccore.utils.messages.MessageType.DEFAULT + getName(id)))
            }
        }
        meta.lore = listOf(Colors.format(me.m64diamondstar.ingeniamccore.utils.messages.MessageType.DEFAULT + "Message: "),
            Colors.format(me.m64diamondstar.ingeniamccore.utils.messages.MessageType.DEFAULT + getMessage(id)))
        item.itemMeta = meta
        return item
    }
}