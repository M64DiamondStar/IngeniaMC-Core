package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.shows.utils.Effect
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.thundertnt33.animatronics.api.Group
import org.bukkit.entity.Player

class AnimatronicGroup(show: Show, id: Int) : Effect(show, id) {

    override fun execute(players: List<Player>?) {
        val name = getSection().getString("Name")!!
        val group = Group(name)
        group.play()
    }

    override fun getType(): Type {
        return Type.ANIMATRONIC_GROUP
    }

    override fun isSync(): Boolean {
        return true
    }

    override fun getDefaults(): List<Pair<String, Any>> {
        val list = ArrayList<Pair<String, Any>>()
        list.add(Pair("Type", "ANIMATRONIC_GROUP"))
        list.add(Pair("Name", "anima"))
        list.add(Pair("Delay", 0))
        return list
    }
}