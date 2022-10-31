package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.Main
import java.io.File

class AttractionUtils {

    companion object {

        fun getCategories(): Array<out File>? {
            val file = File(Main.plugin.dataFolder, "rides")
            return file.listFiles()
        }

        fun getAttractions(category: File): Array<out File>? {
            return category.listFiles()
        }

        fun getAttractions(category: String): Array<out File>? {
            val file = File(Main.plugin.dataFolder, "rides/$category")
            return file.listFiles()
        }

        private fun existsCategory(category: File): Boolean{
            return File(Main.plugin.dataFolder, "rides").listFiles()!!.contains(category)
        }

        fun existsCategory(string: String): Boolean{
            return existsCategory(File(Main.plugin.dataFolder, "rides/$string"))
        }

        private fun existsAttraction(category: File, attraction: File): Boolean{
            return category.listFiles()!!.contains(attraction)
        }

        fun existsAttraction(category: String, attraction: String): Boolean{
            return existsAttraction(File(Main.plugin.dataFolder, "rides/$category"), File(Main.plugin.dataFolder, "rides/$category/$attraction"))
        }

    }

}