package me.m64diamondstar.ingeniamccore.general.player.data

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.general.player.StatisticKey
import me.m64diamondstar.ingeniamccore.general.player.StatisticType
import me.m64diamondstar.ingeniamccore.utils.PlayerSelectors
import org.bukkit.Bukkit
import java.util.*

class PlayerConfig(uuid: UUID) : DataConfiguration("data/player", uuid.toString()) {

    init {

        getConfig().set("Username", Bukkit.getOfflinePlayer(uuid).name)
        save()
    }

    fun getBal(): Long {
        return if (getConfig().get("Economy.Golden-Stars") != null)
            getConfig().getLong("Economy.Golden-Stars")
        else
            0
    }

    fun setBal(bal: Long){
        getConfig().set("Economy.Golden-Stars", bal)
        save()
    }

    fun getExp(): Long {
        return if (getConfig().get("Levels.Exp") != null)
            getConfig().getLong("Levels.Exp")
        else
            0
    }

    fun setExp(bal: Long){
        getConfig().set("Levels.Exp", bal)
        save()
    }

    fun getLevel(): Int{
        return if (getConfig().get("Levels.Level") != null)
            getConfig().getInt("Levels.Level")
        else
            0

    }

    fun setLevel(level: Int){
        getConfig().set("Levels.Level", level)
        save()
    }



    /**
     * Gets the message when a player joins.
     * @return the ID of the message in the join message configuration
     */
    fun getJoinMessage(): String? {
        return getConfig().getString("Messages.Join")
    }

    /**
     * Sets the message when a player joins.
     */
    fun setJoinMessage(msg: String?){
        getConfig().set("Messages.Join", msg)
        save()
    }

    /**
     * Gets the color of the name when a player joins.
     * @return the ID of the color in the join leave color configuration
     */
    fun getJoinColor(): String? {
        return getConfig().getString("Messages.JoinColor")
    }

    /**
     * Sets the color of a name when a player joins.
     */
    fun setJoinColor(color: String?){
        getConfig().set("Messages.JoinColor", color)
        save()
    }


    /**
     * Gets the title of the player.
     * @return the title as a string, but it's a minimessage
     */
    fun getTitle(): String? {
        return getConfig().getString("Titles.Title")
    }


    /**
     * Sets the title of the player.
     */
    fun setPlayerTitle(title: String?){
        getConfig().set("Titles.Title", title)
        save()
    }



    /**
     * Gets the message when a player leaves.
     * @return the ID of the message in the leave message configuration
     */
    fun getLeaveMessage(): String? {
        return getConfig().getString("Messages.Leave")
    }

    /**
     * Sets the message when a player leaves.
     */
    fun setLeaveMessage(id: String?){
        getConfig().set("Messages.Leave", id)
        save()
    }

    /**
     * Gets the color of a name when a player leaves.
     * @return the ID of the color in the join leave color configuration
     */
    fun getLeaveColor(): String? {
        return getConfig().getString("Messages.LeaveColor")
    }

    /**
     * Sets the color of a name when a player leaves.
     */
    fun setLeaveColor(color: String?){
        getConfig().set("Messages.LeaveColor", color)
        save()
    }

    /**
     * Check if the player already linked its Discord.
     */
    fun hasDiscord(): Boolean{
        return getConfig().get("Discord.ID") != null
    }

    /**
     * Link a player account to a Discord account.
     */
    fun setDiscord(id: Long?){
        getConfig().set("Discord.ID", id)
        save()
    }

    /**
     * Get the player's Discord ID.
     */
    fun getDiscordID(): Long {
        return getConfig().getLong("Discord.ID")
    }

    /**
     * Set year playtime
     * @param year the year
     * @param playtime the playtime in ticks
     */
    fun setYearPlaytime(year: Int, playtime: Int){
        getConfig().set("Playtime.$year", playtime)
        save()
    }

    /**
     * Get year playtime
     * @param year the year
     * @return the playtime in ticks
     */
    fun getYearPlaytime(year: Int): Int{
        return getConfig().getInt("Playtime.$year")
    }

    /**
     * Set total playtime
     * @param playtime the playtime in ticks
     */
    fun setPlaytime(playtime: Int){
        getConfig().set("Playtime.Total", playtime)
        save()
    }

    /**
     * Get total playtime
     * @return the playtime in ticks
     */
    fun getPlaytime(): Int{
        return getConfig().getInt("Playtime.Total")
    }

    fun getBodyWearId(): String? {
        return getConfig().getString("External-Cosmetics.BodyWear")
    }

    fun setBodyWearId(id: String?){
        getConfig().set("External-Cosmetics.BodyWear", id)
        save()
    }

    fun setShowSkinDuringDialogue(boolean: Boolean) {
        getConfig().set("Settings.ShowSkinDuringDialogue", boolean)
    }

    fun getShowSkinDuringDialogue(): Boolean {
        if(getConfig().get("Settings.ShowSkinDuringDialogue") == null) return true
        return getConfig().getBoolean("Settings.ShowSkinDuringDialogue")
    }

    fun setShowHud(boolean: Boolean) {
        getConfig().set("Settings.ShowHud", boolean)
    }

    fun getShowHud(): Boolean {
        if(getConfig().get("Settings.ShowHud") == null) return true
        return getConfig().getBoolean("Settings.ShowHud")
    }

    fun setFancyTeleport(boolean: Boolean) {
        getConfig().set("Settings.FancyTeleport", boolean)
    }

    fun getFancyTeleport(): Boolean {
        if(getConfig().get("Settings.FancyTeleport") == null) return true
        return getConfig().getBoolean("Settings.FancyTeleport")
    }

    fun setShowPlayers(playerSelector: PlayerSelectors) {
        getConfig().set("Settings.ShowPlayers", playerSelector.name)
    }

    fun getShowPlayers(): PlayerSelectors {
        return PlayerSelectors.valueOf(getConfig().getString("Settings.ShowPlayers") ?: "ALL")
    }

    fun setShowNametags(playerSelector: PlayerSelectors) {
        getConfig().set("Settings.ShowNametags", playerSelector.name)
    }

    fun getShowNametags(): PlayerSelectors {
        return PlayerSelectors.valueOf(getConfig().getString("Settings.ShowNametags") ?: "ALL")
    }

    /**
     * Holds all statistics for a player
     */
    inner class StatisticContainer {

        fun <T> set(key: StatisticKey<T>, value: T) {
            // Ensure value matches the type defined in the key
            if (!isValueOfType(value, key.type)) {
                throw IllegalArgumentException("Invalid value type for key '${key.name}': expected ${key.type::class.simpleName}, got ${value!!::class.simpleName}")
            }
            // Store the value in the config
            getConfig().set("statistics.${key.name}", value)
            save()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> get(key: StatisticKey<T>): T? {
            val value = getConfig().get("statistics.${key.name}")
            if (value == null) return null

            // Check if the value retrieved from config matches the expected type
            return if (isValueOfType(value, key.type)) {
                value as T
            } else {
                throw IllegalStateException("Stored value for key '${key.name}' is of incorrect type. Expected ${key.type::class.simpleName}, but got ${value::class.simpleName}")
            }
        }

        private fun <T> isValueOfType(value: Any?, type: StatisticType<T>): Boolean {
            return when (type) {
                is StatisticType.IntType -> value is Int
                is StatisticType.StringType -> value is String
                // Add checks for additional types here
            }
        }
    }


}