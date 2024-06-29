package me.m64diamondstar.ingeniamccore.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

class ScoreboardTeamManager {

    private val scoreboard: org.bukkit.scoreboard.Scoreboard = Bukkit.getScoreboardManager().mainScoreboard

    init {
        setupTeams()
    }

    private fun setupTeams() {
        createTeam("a_lead")
        createTeam("b_team")
        createTeam("c_teamtrial")
        createTeam("d_vipplus")
        createTeam("e_vip")
        createTeam("f_default")
    }

    private fun createTeam(name: String) {
        if(scoreboard.getTeam(name) != null)
            scoreboard.registerNewTeam(name)
    }

    fun addPlayerToTeam(player: Player) {
        val team = getTeamForPlayer(player)
        team?.addEntry(player.name)
    }

    private fun getTeamForPlayer(player: Player): Team? {
        return when {
            player.hasPermission("ingenia.lead") -> scoreboard.getTeam("a_lead")
            player.hasPermission("ingenia.team") -> scoreboard.getTeam("b_team")
            player.hasPermission("ingenia.teamtrial") -> scoreboard.getTeam("c_teamtrial")
            player.hasPermission("ingenia.vip+") -> scoreboard.getTeam("d_vipplus")
            player.hasPermission("ingenia.vip") -> scoreboard.getTeam("e_vip")
            else -> scoreboard.getTeam("f_default")
        }
    }
}