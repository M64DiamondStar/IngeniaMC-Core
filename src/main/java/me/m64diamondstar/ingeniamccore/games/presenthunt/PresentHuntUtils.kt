package me.m64diamondstar.ingeniamccore.games.presenthunt

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.Location
import java.io.File

object PresentHuntUtils {

    private val activePresents = HashMap<Location, Pair<String, String>>()

    fun addActivePresent(location: Location, presentHunt: PresentHunt) {
        activePresents[location] = Pair(presentHunt.category, presentHunt.name)
    }

    fun removeActivePresent(location: Location){
        activePresents.remove(location)
    }

    fun containsActivePresent(location: Location): Boolean{
        return activePresents.contains(location)
    }

    fun getActivePresent(location: Location): PresentHunt? {
        return activePresents[location]?.let { activePresents[location]?.let { it1 -> PresentHunt(it.first, it1.second) } }
    }

    fun saveActivePresents(){
        getAllPresentHunts().forEach {
            it.saveActivePresents()
        }
    }

    fun loadActivePresents(){
        getAllPresentHunts().forEach {
            it.loadActivePresents()
        }
    }



    fun getCategories(): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "games/presenthunt")

        val files = ArrayList<File>()
        file.listFiles()?.let { files.addAll(it) }
        files.remove(File(IngeniaMC.plugin.dataFolder, "games/presenthunt/.DS_Store"))

        return files
    }

    fun getPresentHunts(category: String): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "games/presenthunt/$category")

        val files = ArrayList<File>()
        file.listFiles()?.forEach {
            if(!it.name.contains(".DS_Store"))
                files.add(it)
        }

        return files
    }

    private fun existsCategory(category: File): Boolean{
        return File(IngeniaMC.plugin.dataFolder, "games/presenthunt").listFiles()!!.contains(category)
    }

    fun existsCategory(string: String): Boolean{
        return existsCategory(File(IngeniaMC.plugin.dataFolder, "games/presenthunt/$string"))
    }

    private fun existsPresentHunt(category: File, area: File): Boolean{
        if(!category.exists()) return false
        return category.listFiles()!!.contains(area)
    }

    fun existsPresentHunt(category: String, area: String): Boolean{
        return existsPresentHunt(
            File(IngeniaMC.plugin.dataFolder, "games/presenthunt/$category"), File(
                IngeniaMC.plugin.dataFolder,
                if(area.contains(".yml")) "games/presenthunt/$category/$area" else "games/presenthunt/$category/$area.yml")
        )
    }

    fun getAllPresentHunts(): List<PresentHunt>{
        val list = ArrayList<PresentHunt>()
        for(category in getCategories()){
            category.listFiles()?.forEach {
                if(!it.name.contains(".DS_Store"))
                    list.add(PresentHunt(category.name, it.name))
            }
        }
        return list
    }

}