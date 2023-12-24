package me.m64diamondstar.ingeniamccore.shops.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.parkour.Parkour
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

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

    fun getAllShops(): List<Parkour>{
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