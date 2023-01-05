package me.m64diamondstar.ingeniamccore.games.parkour

import me.m64diamondstar.ingeniamccore.IngeniaMC
import java.io.File

object ParkourUtils {

    fun getCategories(): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "games/parkour")

        val files = ArrayList<File>()
        file.listFiles()?.let { files.addAll(it) }
        files.remove(File(IngeniaMC.plugin.dataFolder, "games/parkour/.DS_Store"))

        return files
    }

    fun getParkours(category: String): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "games/parkour/$category")

        val files = ArrayList<File>()
        file.listFiles()?.forEach {
            if(!it.name.contains(".DS_Store"))
                files.add(it)
        }

        return files
    }

    private fun existsCategory(category: File): Boolean{
        return File(IngeniaMC.plugin.dataFolder, "games/parkour").listFiles()!!.contains(category)
    }

    fun existsCategory(string: String): Boolean{
        return existsCategory(File(IngeniaMC.plugin.dataFolder, "games/parkour/$string"))
    }

    private fun existsParkour(category: File, area: File): Boolean{
        if(!category.exists()) return false
        return category.listFiles()!!.contains(area)
    }

    fun existsParkour(category: String, area: String): Boolean{
        return existsParkour(
            File(IngeniaMC.plugin.dataFolder, "games/parkour/$category"), File(
                IngeniaMC.plugin.dataFolder,
            if(area.contains(".yml")) "games/parkour/$category/$area" else "games/parkour/$category/$area.yml")
        )
    }

    fun getAllParkours(): List<Parkour>{
        val list = ArrayList<Parkour>()
        for(category in getCategories()){
            category.listFiles()?.forEach {
                if(!it.name.contains(".DS_Store"))
                    list.add(Parkour(category.name, it.name))
            }
        }
        return list
    }

}