package me.m64diamondstar.ingeniamccore.data.files

import me.m64diamondstar.ingeniamccore.data.Configuration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class PlayerConfig(uuid: UUID) : Configuration("data/player", uuid.toString(), false) {

    private var player: Player

    init {
        player = Bukkit.getPlayer(uuid)!!

        getConfig().set("Username", Bukkit.getOfflinePlayer(uuid).name)

        reload()
    }

    fun getBal(): Long {
        return if (getConfig().get("Economy.Golden-Stars") != null)
            getConfig().getLong("Economy.Golden-Stars")
        else
            0
    }

    fun setBal(bal: Long){
        getConfig().set("Economy.Golden-Stars", bal)
        reload()
    }

    fun getExp(): Long {
        return if (getConfig().get("Levels.Exp") != null)
            getConfig().getLong("Levels.Exp")
        else
            0
    }

    fun setExp(bal: Long){
        val expComments = ArrayList<String>()
        expComments.add("This is the amount of EXP the player has.")
        expComments.add("The level of the player will be automatically calculated.")
        getConfig().setComments("Levels", expComments)
        getConfig().set("Levels.Exp", bal)
        reload()
    }


}