package me.m64diamondstar.ingeniamccore.general.tablist

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors.format
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TabList(private val plugin: IngeniaMC) {
    private val header: MutableList<String> = ArrayList()
    private val footer: MutableList<String> = ArrayList()
    fun showTab(player: Player) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, object : Runnable {
            var count1 = 0 //headers
            var count2 = 0 //footers
            override fun run() {
                if (count1 >= header.size) count1 = 0
                if (count2 >= footer.size) count2 = 0
                player.setPlayerListHeaderFooter(header[count1], footer[count2])
                count1++
                count2++
            }
        }, 10, 5)
    }

    fun clearHeader() {
        header.clear()
    }

    fun clearFooter() {
        footer.clear()
    }

    fun addHeader(header1: String, player: Player) {
        header.add(format(header1.replace("%player%", player.name)))
    }

    fun addFooter(footer1: String?) {
        footer.add(format(footer1!!))
    }
}