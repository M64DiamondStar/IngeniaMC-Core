package me.m64diamondstar.ingeniamccore.games.guesstheword

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class GuessTheWord {

    private val unscrambledWord: String = GuessTheWordUtils.getRandomWord()
    private var currentWord: String = GuessTheWordUtils.scramble(unscrambledWord)
    private val playerUuids = ArrayList<UUID>()

    fun execute(){

        GuessTheWordRegistry.add(this)

        val bossBar = Bukkit.createBossBar(
            Colors.format("#eba7a7Guess The Word &f» #eba7a7$currentWord"),
            BarColor.RED,
            BarStyle.SEGMENTED_6
        )

        for (p in Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(p!!)
        }
        bossBar.progress = 1.0
        bossBar.isVisible = true

        object: BukkitRunnable(){

            var progress = 1.0
            val time = 1.0/6.0

            override fun run() {
                bossBar.progress = progress
                if(unscrambledWord == currentWord){
                    bossBar.setTitle(Colors.format("#eba7a7The Word Was &f» #eba7a7$unscrambledWord"))
                    bossBar.color = BarColor.WHITE
                    bossBar.progress = 1.0
                    Bukkit.getScheduler().runTaskLater(IngeniaMC.plugin,
                        Runnable { bossBar.removeAll() }, 60L
                    )

                    GuessTheWordRegistry.remove(this@GuessTheWord)

                    this.cancel()
                    return
                }

                if (progress == 1.0) currentWord = GuessTheWordUtils.unscramble(currentWord, unscrambledWord)

                progress -= time
                if (progress <= 0) {
                    progress = 1.0
                    bossBar.setTitle(Colors.format("#eba7a7Guess The Word &f» #eba7a7$currentWord"))
                    for (p in Bukkit.getOnlinePlayers()) {
                        if (!bossBar.players.contains(p)) bossBar.addPlayer(p)
                    }
                }
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 20L)
    }

    fun getPlayers(): List<UUID>{
        return playerUuids
    }

    fun addPlayer(player: Player){
        playerUuids.add(player.uniqueId)
    }

    fun getWord(): String{
        return unscrambledWord
    }

}