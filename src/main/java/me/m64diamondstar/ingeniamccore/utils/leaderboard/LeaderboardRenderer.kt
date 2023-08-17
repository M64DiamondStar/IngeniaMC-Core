package me.m64diamondstar.ingeniamccore.utils.leaderboard

import org.bukkit.entity.Player
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import java.awt.image.BufferedImage

class LeaderboardRenderer(leaderboard: Leaderboard, player: Player, title: String): MapRenderer() {

    private val image: BufferedImage
    private var done: Boolean

    init{
        done = false

        val image = leaderboard.getImage(player, title)

        this.image = image

    }

    override fun render(map: MapView, canvas: MapCanvas, player: Player) {
        if(done)
            return

        canvas.drawImage(0, 0, image)

        map.isTrackingPosition = false
        done = true
    }


}