package me.m64diamondstar.ingeniamccore.games.wandclash

import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashGamePhase

class WandClashArena(val name: String) {

    private var gamePhase: WandClashGamePhase = WandClashGamePhase.DISABLED

    fun getGamePhase(): WandClashGamePhase = gamePhase
    fun getData(): WandClashData = WandClashData(name)


}