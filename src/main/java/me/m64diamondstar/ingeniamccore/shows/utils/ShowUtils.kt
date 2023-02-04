package me.m64diamondstar.ingeniamccore.shows.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.entity.FallingBlock
import java.io.File

object ShowUtils {

    private val fallingBlocks = ArrayList<FallingBlock>()

    fun getCategories(): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "shows")

        val files = ArrayList<File>()
        file.listFiles()?.let { files.addAll(it) }
        files.remove(File(IngeniaMC.plugin.dataFolder, "shows/.DS_Store"))

        return files
    }

    fun getShows(category: String): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "shows/$category")

        val files = ArrayList<File>()
        file.listFiles()?.forEach {
            if(!it.name.contains(".DS_Store"))
                files.add(it)
        }

        return files
    }

    private fun existsCategory(category: File): Boolean{
        File(IngeniaMC.plugin.dataFolder, "shows").mkdirs()
        return File(IngeniaMC.plugin.dataFolder, "shows").listFiles()!!.contains(category)
    }

    fun existsCategory(string: String): Boolean{
        return existsCategory(File(IngeniaMC.plugin.dataFolder, "shows/$string"))
    }

    private fun existsShow(category: File, show: File): Boolean{
        return category.listFiles()!!.contains(show)
    }

    fun existsShow(category: String, show: String): Boolean{
        return existsShow(
            File(IngeniaMC.plugin.dataFolder, "shows/$category"), File(
                IngeniaMC.plugin.dataFolder,
                if(show.contains(".yml")) "shows/$category/$show" else "shows/$category/$show.yml")
        )
    }

    fun getFallingBlocks(): ArrayList<FallingBlock>{
        return fallingBlocks
    }

    fun addFallingBlock(fallingBlock: FallingBlock){
        fallingBlocks.add(fallingBlock)
    }

    fun removeFallingBlock(fallingBlock: FallingBlock){
        fallingBlocks.remove(fallingBlock)
    }

}