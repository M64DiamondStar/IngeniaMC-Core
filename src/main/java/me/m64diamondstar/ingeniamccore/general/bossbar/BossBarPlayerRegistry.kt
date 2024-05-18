package me.m64diamondstar.ingeniamccore.general.bossbar

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object BossBarPlayerRegistry {

    private val players = HashMap<Pair<UUID, BossBarIndex>, BossBar>()

    fun addPlayer(player: Player, bossBarIndex: BossBarIndex, bossBar: BossBar) {
        players[Pair(player.uniqueId, bossBarIndex)] = bossBar
    }

    fun removePlayer(player: Player, bossBarIndex: BossBarIndex) {
        players.remove(Pair(player.uniqueId, bossBarIndex))
    }

    fun getBossBar(player: Player, bossBarIndex: BossBarIndex): BossBar? {
        return players[Pair(player.uniqueId, bossBarIndex)]
    }

    fun hideBossBar(player: Player, bossBarIndex: BossBarIndex) {
        if(getBossBar(player, bossBarIndex) != null)
            (player as Audience).hideBossBar(getBossBar(player, bossBarIndex)!!)
    }

    fun showBossBar(player: Player, bossBarIndex: BossBarIndex) {
        if(getBossBar(player, bossBarIndex) != null)
            (player as Audience).showBossBar(getBossBar(player, bossBarIndex)!!)
    }
}