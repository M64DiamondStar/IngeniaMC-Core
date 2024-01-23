package me.m64diamondstar.ingeniamccore.general.player.data

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import org.bukkit.Bukkit
import java.util.*

class PlayerConfig(uuid: UUID) : DataConfiguration("data/player", uuid.toString()) {

    init {

        getConfig().set("Username", Bukkit.getOfflinePlayer(uuid).name)
        save()
    }

    fun getBal(): Long {
        reload()
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
        reload()
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
        reload()
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
        reload()
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
        reload()
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
     * Gets the message when a player leaves.
     * @return the ID of the message in the leave message configuration
     */
    fun getLeaveMessage(): String? {
        reload()
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
        reload()
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

}