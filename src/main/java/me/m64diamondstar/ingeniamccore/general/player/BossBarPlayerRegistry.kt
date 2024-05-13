package me.m64diamondstar.ingeniamccore.general.player

import net.kyori.adventure.bossbar.BossBar
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object BossBarPlayerRegistry {

    private val players = HashMap<UUID, BossBar>()

    fun addPlayer(player: Player, bossBar: BossBar) {
        players[player.uniqueId] = bossBar
    }

    fun removePlayer(player: Player) {
        players.remove(player.uniqueId)
    }

    fun getBossBar(player: Player): BossBar? {
        return players[player.uniqueId]
    }
}