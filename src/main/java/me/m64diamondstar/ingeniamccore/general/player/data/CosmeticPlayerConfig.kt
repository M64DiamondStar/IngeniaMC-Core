package me.m64diamondstar.ingeniamccore.general.player.data

import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import org.bukkit.Bukkit
import java.util.*

class CosmeticPlayerConfig(uuid: UUID) : DataConfiguration("data/cosmetic-user", uuid.toString()) {

    init {
        getConfig().set("Username", Bukkit.getOfflinePlayer(uuid).name)
        save()
    }

    /**
     * Gets all cosmetics a player has obtained from a specific cosmetic type
     * @param cosmeticType the type of cosmetic to check
     */
    fun getAllCosmetics(cosmeticType: CosmeticType): List<String> {
        if(this.getConfig().getConfigurationSection(cosmeticType.toString()) == null) return emptyList()
        return this.getConfig().getConfigurationSection(cosmeticType.toString())!!.getKeys(false).toList()
    }

    /**
     * Adds a cosmetic to the player's file
     * @param cosmeticType the type of the cosmetic to add
     * @param id the ID of the cosmetic
     */
    fun addCosmetic(cosmeticType: CosmeticType, id: String){
        this.getConfig().set("$cosmeticType.$id.ObtainTime", System.currentTimeMillis())
        this.save()
    }

    /**
     * Removes a cosmetic to the player's file
     * @param cosmeticType the type of the cosmetic to remove
     * @param id the ID of the cosmetic
     */
    fun removeCosmetic(cosmeticType: CosmeticType, id: String){
        this.getConfig().set("$cosmeticType.$id", null)
        this.save()
    }

    /**
     * Checks if the player has a specific cosmetic
     * @param cosmeticType the type of the cosmetic to remove
     * @param id the ID of the cosmetic
     */
    fun hasCosmetic(cosmeticType: CosmeticType, id: String): Boolean{
        return this.getConfig().get("$cosmeticType.$id") != null
    }

    /**
     * Gets all message colors
     * @return all message colors
     */
    fun getAllMessageColors(): SortedSet<String> {
        if(this.getConfig().getConfigurationSection("MESSAGE_COLORS") == null) return emptyArray<String>().toSortedSet()
        return this.getConfig().getConfigurationSection("MESSAGE_COLORS")!!.getKeys(false).toSortedSet()
    }

    /**
     * Adds a message color
     * @param id the ID of the message
     */
    fun addMessageColor(id: String){
        this.getConfig().set("MESSAGE_COLORS.$id.ObtainTime", System.currentTimeMillis())
        this.save()
    }

    /**
     * Removes a message color
     * @param id the ID of the message
     */
    fun removeMessageColor(id: String){
        this.getConfig().set("MESSAGE_COLORS.$id", null)
        this.save()
    }

    /**
     * Checks if the player has a specific message color
     * @param id the ID of the message
     * @return true if the player has the message color
     */
    fun hasMessageColor(id: String): Boolean{
        return this.getConfig().get("MESSAGE_COLORS.$id") != null
    }

    /**
     * Gets all join messages
     * @return all join messages
     */
    fun getAllJoinMessages(): SortedSet<String>{
        if(this.getConfig().getConfigurationSection("JOIN_MESSAGES") == null) return emptyArray<String>().toSortedSet()
        return this.getConfig().getConfigurationSection("JOIN_MESSAGES")!!.getKeys(false).toSortedSet()
    }

    /**
     * Adds a join message
     * @param id the ID of the message
     */
    fun addJoinMessage(id: String){
        this.getConfig().set("JOIN_MESSAGES.$id.ObtainTime", System.currentTimeMillis())
        this.save()
    }

    /**
     * Removes a join message
     * @param id the ID of the message
     */
    fun removeJoinMessage(id: String){
        this.getConfig().set("JOIN_MESSAGES.$id", null)
        this.save()
    }

    /**
     * Checks if the player has a specific join message
     * @param id the ID of the message
     * @return true if the player has the join message
     */
    fun hasJoinMessage(id: String): Boolean{
        return this.getConfig().get("JOIN_MESSAGES.$id") != null
    }

    /**
     * Gets all leave messages
     * @return all leave messages
     */
    fun getAllLeaveMessages(): SortedSet<String>{
        if(this.getConfig().getConfigurationSection("LEAVE_MESSAGES") == null) return emptyArray<String>().toSortedSet()
        return this.getConfig().getConfigurationSection("LEAVE_MESSAGES")!!.getKeys(false).toSortedSet()
    }

    /**
     * Adds a leave message
     * @param id the ID of the message
     */
    fun addLeaveMessage(id: String){
        this.getConfig().set("LEAVE_MESSAGES.$id.ObtainTime", System.currentTimeMillis())
        this.save()
    }

    /**
     * Removes a leave message
     * @param id the ID of the message
     */
    fun removeLeaveMessage(id: String){
        this.getConfig().set("LEAVE_MESSAGES.$id", null)
        this.save()
    }

    /**
     * Checks if the player has a specific leave message
     * @param id the ID of the message
     * @return true if the player has the leave message
     */
    fun hasLeaveMessage(id: String): Boolean{
        return this.getConfig().get("LEAVE_MESSAGES.$id") != null
    }

}