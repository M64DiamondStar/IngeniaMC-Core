package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import org.bukkit.entity.Player
import java.io.File

object AttractionUtils {

    fun getCategories(): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "rides")

        val files = ArrayList<File>()
        file.listFiles()?.let { files.addAll(it) }
        files.remove(File(IngeniaMC.plugin.dataFolder, "rides/.DS_Store"))

        return files
    }

    fun getAttractions(category: File): Array<out File>? {
        return category.listFiles()
    }

    fun getAttractions(category: String): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "rides/$category")

        val files = ArrayList<File>()
        file.listFiles()?.forEach {
            if(!it.name.contains(".DS_Store"))
                files.add(it)
        }

        return files
    }

    private fun existsCategory(category: File): Boolean{
        return File(IngeniaMC.plugin.dataFolder, "rides").listFiles()!!.contains(category)
    }

    fun existsCategory(string: String): Boolean{
        return existsCategory(File(IngeniaMC.plugin.dataFolder, "rides/$string"))
    }

    private fun existsAttraction(category: File, attraction: File): Boolean{
        return category.listFiles()!!.contains(attraction)
    }

    fun existsAttraction(category: String, attraction: String): Boolean{
        return existsAttraction(File(IngeniaMC.plugin.dataFolder, "rides/$category"), File(IngeniaMC.plugin.dataFolder,
            if(attraction.contains(".yml")) "rides/$category/$attraction" else "rides/$category/$attraction.yml"))
    }

    fun getAllAttractions(): List<Attraction>{
        val list = ArrayList<Attraction>()
        for(category in getCategories()){
            category.listFiles()?.forEach { if(!it.name.contains(".DS_Store"))
                list.add(Attraction(category.name, it.name))}
        }
        return list
    }

    fun getTotalRidecount(player: Player): Int{
        var i = 0
        getAllAttractions().forEach { i += it.getRidecount(player) }
        return i
    }

    fun spawnAllAttractions(){
        for(attraction in getAllAttractions()){
            if(attraction.getType() == AttractionType.FREEFALL){
                val freeFall = FreeFall(attraction.getCategory(), attraction.getName())
                freeFall.spawn()
            }
        }
    }

    fun despawnAllAttractions(){
        for(attraction in getAllAttractions()){
            if(attraction.getType() == AttractionType.FREEFALL){
                val freeFall = FreeFall(attraction.getCategory(), attraction.getName())
                freeFall.despawn()
            }
        }
    }


}