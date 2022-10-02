package me.m64diamondstar.ingeniamccore.general.scoreboard

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.Colors
import me.m64diamondstar.ingeniamccore.utils.MessageType
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.*
import org.bukkit.scoreboard.Scoreboard
import java.util.*

class Scoreboard(private val player: IngeniaPlayer) {
    private var objective: Objective? = null
    private var scoreboard: Scoreboard? = null
    private var bal: Team? = null
    private var onl: Team? = null
    private var ip: Team? = null
    fun createBoard() {
        val manager = Bukkit.getScoreboardManager()!!
        scoreboard = manager.newScoreboard
        objective =
            scoreboard!!.registerNewObjective("IngeniaMC-Board", Criteria.DUMMY, Colors.format("#f4b734&lIngeniaMC"))
        objective!!.displaySlot = DisplaySlot.SIDEBAR
        objective!!.getScore(Colors.format("&7&7")).score = 4
        objective!!.getScore(Colors.format("&7&7&6")).score = 1
        bal = scoreboard!!.registerNewTeam("bal")
        bal!!.addEntry(Colors.format("&2"))
        onl = scoreboard!!.registerNewTeam("onl")
        onl!!.addEntry(Colors.format("&1"))
        ip = scoreboard!!.registerNewTeam("ip")
        ip!!.addEntry(Colors.format("&0"))
        ip!!.prefix = Colors.format(
            "   play.IngeniaMC.net",
            MessageType.BACKGROUND
        )
        objective!!.getScore(Colors.format("&0")).score = 0
    }

    fun showBoard() {
        player.player.scoreboard = scoreboard!!
    }

    fun startUpdating() {
        object : BukkitRunnable() {
            override fun run() {
                if (player.player.scoreboard !== scoreboard) {
                    cancel()
                    return
                }
                bal!!.prefix = Colors.format("#f4b734 » Golden Stars: &r" + player.bal + ":gs:")
                objective!!.getScore(Colors.format("&2")).score = 3
                onl!!.prefix =
                    Colors.format("#f4b734 » Online Players: &r" + Bukkit.getOnlinePlayers().size)
                objective!!.getScore(Colors.format("&1")).score = 2
            }
        }.runTaskTimer(Main.plugin, 0L, 20L)
    }

    fun hideBoard() {
        player.player.scoreboard = Bukkit.getScoreboardManager()?.newScoreboard!!
    }
}