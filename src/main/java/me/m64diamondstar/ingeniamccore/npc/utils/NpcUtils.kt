package me.m64diamondstar.ingeniamccore.npc.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.npc.Npc
import org.bukkit.entity.Player
import java.io.File

object NpcUtils {

    fun getNPCFiles(): ArrayList<File> {
        val file = File(IngeniaMC.plugin.dataFolder, "npc")

        val files = ArrayList<File>()
        file.listFiles()?.forEach {
            if(!it.name.contains(".DS_Store") && !it.name.equals("npc_cache.yml", ignoreCase = true))
                files.add(it)
        }

        return files
    }

    /**
     * Only call this on startup once!
     * Loads and registers all the NPCs
     */
    fun loadNpcFiles(){
        NpcRegistry.getAllNpcs().forEach { it.despawn() }
        NpcRegistry.clear()
        getNPCFiles().forEach {
            val name = it.name.replace(".yml", "")
            if(!existsNpc(name)) return@forEach
            Npc(name).init()
        }
    }

    private fun existsNpc(category: File, npc: File): Boolean{
        if(!category.exists()) return false
        return category.listFiles()!!.contains(npc)
    }

    fun existsNpc(npc: String): Boolean{
        return existsNpc(
            File(IngeniaMC.plugin.dataFolder, "npc"), File(
                IngeniaMC.plugin.dataFolder,
                if(npc.contains(".yml")) "npc/$npc" else "npc/$npc.yml")
        )
    }

    object PlayersMoved {
        private val playersMoved = ArrayList<Player>()

        fun addPlayer(player: Player){
            playersMoved.add(player)
        }

        fun clear(){
            playersMoved.clear()
        }

        fun getPlayers(): ArrayList<Player>{
            return playersMoved
        }
    }

}