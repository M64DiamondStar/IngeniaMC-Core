package me.m64diamondstar.ingeniamccore.games.wandclash.util

import me.m64diamondstar.ingeniamccore.IngeniaMC
import java.io.File

object WandClashUtils {

    fun getWandClashArenas(): ArrayList<File> {
        try {
            val file = File(IngeniaMC.plugin.dataFolder, "games/wandclash/arenas")

            val files = ArrayList<File>()
            file.listFiles()?.forEach {
                if (!it.name.contains(".DS_Store"))
                    files.add(it)
            }

            return files
        }catch (e: NullPointerException){
            return ArrayList()
        }
    }

    private fun existsWandClashArena(name: File): Boolean{
        return File(IngeniaMC.plugin.dataFolder, "games/wandclash/arenas").listFiles()!!.contains(name)
    }

    fun existsWandClashArena(name: String): Boolean{
        return existsWandClashArena(
            File(IngeniaMC.plugin.dataFolder,
                if(name.contains(".yml")) "games/wandclash/arenas/$name" else "games/wandclash/arenas/$name.yml")
        )
    }

}