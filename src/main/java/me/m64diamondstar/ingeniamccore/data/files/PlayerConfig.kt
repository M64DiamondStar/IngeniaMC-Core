package me.m64diamondstar.ingeniamccore.data.files

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.data.LoadedConfiguration
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class PlayerConfig(uuid: UUID) : DataConfiguration("data/player", uuid.toString()) {

    private var player: Player

    init {
        player = Bukkit.getPlayer(uuid)!!

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
        /*val expComments = ArrayList<String>()
        expComments.add("This is the amount of EXP the player has.")
        expComments.add("The level of the player will be automatically calculated.")
        getConfig().setComments("Levels", expComments)*/
        getConfig().set("Levels.Exp", bal)
        save()
    }


    /**
     * Gets the message when a player joins.
     */
    fun getJoinMessage(): String? {
        reload()
        return if (getConfig().get("Messages.Join") != null)
            getConfig().getString("Messages.Join")
        else
            Colors.format("#6cba65+ %player%")
    }

    /**
     * Sets the message when a player joins.
     */
    fun setJoinMessage(msg: String){
        getConfig().set("Messages.Join", msg)
        save()
    }

    /**
     * Gets the color of the name when a player joins.
     */
    fun getJoinColor(): String? {
        reload()
        return if (getConfig().get("Messages.JoinColor") != null)
            getConfig().getString("Messages.JoinColor")
        else
            Colors.format("#ababab")
    }

    /**
     * Sets the color of a name when a player joins.
     */
    fun setJoinColor(color: String){
        getConfig().set("Messages.JoinColor", color)
        save()
    }



    /**
     * Gets the message when a player leaves.
     */
    fun getLeaveMessage(): String? {
        reload()
        return if (getConfig().get("Messages.Leave") != null)
            getConfig().getString("Messages.Leave")
        else
            Colors.format("#c74a4a- %player%")
    }

    /**
     * Sets the message when a player leaves.
     */
    fun setLeaveMessage(msg: String){
        getConfig().set("Messages.Leave", msg)
        save()
    }

    /**
     * Gets the color of a name when a player leaves.
     */
    fun getLeaveColor(): String? {
        reload()
        return if (getConfig().get("Messages.LeaveColor") != null)
            getConfig().getString("Messages.LeaveColor")
        else
            Colors.format("#ababab")
    }

    /**
     * Sets the color of a name when a player leaves.
     */
    fun setLeaveColor(color: String){
        getConfig().set("Messages.LeaveColor", color)
        save()
    }

}