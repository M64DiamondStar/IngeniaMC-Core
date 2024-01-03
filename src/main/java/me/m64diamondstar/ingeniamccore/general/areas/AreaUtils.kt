package me.m64diamondstar.ingeniamccore.general.areas

import me.m64diamondstar.ingeniamccore.IngeniaMC
import java.io.File

object AreaUtils {

    private val areas = ArrayList<Area>()

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
        if(!category.exists()) return false
        return category.listFiles()!!.contains(area)
    }

    fun existsArea(category: String, area: String): Boolean{
        return existsArea(File(IngeniaMC.plugin.dataFolder, "area/$category"), File(IngeniaMC.plugin.dataFolder,
            if(area.contains(".yml")) "area/$category/$area" else "area/$category/$area.yml"))
    }

    fun getAllAreas(): List<Area>{
        return areas
    }

    fun addArea(area: Area) {
        areas.add(area)
    }

    fun getAllAreasFromData(): List<Area>{
        val list = ArrayList<Area>()
        for(category in getCategories()){
            category.listFiles()?.forEach {
                if(!it.name.contains(".DS_Store"))
                    list.add(Area(category.name, it.name))
            }
        }
        return list
    }

    fun getAreaFromName(name: String?): Area? {
        if(name == null) return null
        if(name.split("/").size != 2) return null
        if(!existsArea(name.split("/")[0], name.split("/")[1])) return null
        return Area(name.split("/")[0], name.split("/")[1])
    }

}