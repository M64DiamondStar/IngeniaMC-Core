package me.m64diamondstar.ingeniamccore.general.areas

import com.sk89q.worldedit.regions.Polygonal2DRegion
import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.Bukkit
import java.io.File

object AreaUtils {

    private val regions = HashMap<String, HashMap<String, Polygonal2DRegion>>()

    fun getCategories(): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "area")

        val files = ArrayList<File>()
        file.listFiles()?.let { files.addAll(it) }
        files.remove(File(IngeniaMC.plugin.dataFolder, "area/.DS_Store"))

        return files
    }

    private fun existsCategory(category: File): Boolean{
        return File(IngeniaMC.plugin.dataFolder, "area").listFiles()!!.contains(category)
    }

    fun existsCategory(string: String): Boolean{
        return existsCategory(File(IngeniaMC.plugin.dataFolder, "area/$string"))
    }

    private fun existsArea(category: File, area: File): Boolean{
        return category.listFiles()!!.contains(area)
    }

    fun existsArea(category: String, area: String): Boolean{
        return existsArea(File(IngeniaMC.plugin.dataFolder, "area/$category"), File(IngeniaMC.plugin.dataFolder,
            if(area.contains(".yml")) "area/$category/$area" else "area/$category/$area.yml"))
    }

    fun getAllAreas(): List<Area>{
        val list = ArrayList<Area>()
        for(category in getCategories()){
            category.listFiles()?.forEach {
                if(!it.name.contains(".DS_Store"))
                    list.add(Area(category.name, it.name))
            }
        }
        return list
    }

    fun getAreas(): MutableMap<String, HashMap<String, Polygonal2DRegion>> {
        return regions
    }

    private fun setArea(category: String, name: String, polygonal2DRegion: Polygonal2DRegion){
        if(regions[category] == null){
            val map = HashMap<String, Polygonal2DRegion>()
            map[name] = polygonal2DRegion
            regions[category] = map
        }else{
            val cat = regions[category]
            cat!![name] = polygonal2DRegion
            regions[category] = cat
        }

    }

    fun setArea(area: Area){
        try {
            setArea(area.category, area.name, area.area!!)
        }catch (ex: Exception){
            Bukkit.getLogger().warning("The area ${area.name} doesn't have a selection!")
        }
    }

}