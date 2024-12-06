package me.m64diamondstar.ingeniamccore.shops.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.parkour.Parkour
import java.io.File

object ShopUtils {

    fun getCategories(): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "shops")

        val files = ArrayList<File>()
        file.listFiles()?.forEach {
            if (!it.name.contains(".DS_Store"))
                files.add(it)
        }
        files.remove(File(IngeniaMC.plugin.dataFolder, "shops.DS_Store"))

        return files
    }

    fun getShops(category: String): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "shops/$category")

        val files = ArrayList<File>()
        file.listFiles()?.forEach {
            if(!it.name.contains(".DS_Store"))
                files.add(it)
        }

        return files
    }

    private fun existsCategory(category: File): Boolean{
        return try {
            File(IngeniaMC.plugin.dataFolder, "shops").listFiles()!!.contains(category)
        }catch (ex: Exception){
            false
        }
    }

    fun existsCategory(string: String): Boolean{
        return existsCategory(File(IngeniaMC.plugin.dataFolder, "shops/$string"))
    }

    private fun existsShop(category: File, area: File): Boolean{
        if(!category.exists()) return false
        return try {
            category.listFiles()!!.contains(area)
        }catch (ex: Exception){
            false
        }
    }

    fun existsShop(category: String, area: String): Boolean{
        return existsShop(
            File(IngeniaMC.plugin.dataFolder, "shops/$category"), File(
                IngeniaMC.plugin.dataFolder,
                if(area.contains(".yml")) "shops/$category/$area" else "shops/$category/$area.yml")
        )
    }

}