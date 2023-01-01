package me.m64diamondstar.ingeniamccore.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team


object TeamHandler {

    private lateinit var team: Team
    private const val team_name = "no_collision"

    fun load(){
        val sm = Bukkit.getScoreboardManager()
        val board: Scoreboard = sm!!.mainScoreboard

        team = if(board.getTeam(team_name) == null)
            board.registerNewTeam(team_name)
        else
            board.getTeam(team_name)!!

        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
    }

    fun unload() {
        team.unregister()
    }

    fun addPlayer(player: Player) {
        team.addEntry(player.name)
    }

    fun removePlayer(player: Player) {
        team.removeEntry(player.name)
    }

    fun containsPlayer(player: Player): Boolean {
        return team.entries.contains(player.name)
    }

}