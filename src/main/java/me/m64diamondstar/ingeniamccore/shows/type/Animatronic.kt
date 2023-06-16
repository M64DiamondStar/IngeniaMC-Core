package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.shows.utils.Effect
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Animatronic(show: Show, id: Int): Effect(show, id) {

    override fun execute(players: List<Player>?) {
        val name = getSection().getString("Name")!!
        Bukkit.dispatchCommand(Bukkit.getServer().consoleSender, "anima play $name")
    }

    override fun getType(): Type{
        return Type.ANIMATRONIC
    }

    override fun isSync(): Boolean {
        return true
    }

    override fun getDefaults(): List<Pair<String, Any>> {
        val list = ArrayList<Pair<String, Any>>()
        list.add(Pair("Type", "ANIMATRONIC"))
        list.add(Pair("Name", "anima"))
        list.add(Pair("Delay", 0))
        return list
    }
}