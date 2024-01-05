package me.m64diamondstar.ingeniamccore.protect.utils

import me.m64diamondstar.ingeniamccore.data.LoadedConfiguration
import org.bukkit.Location

class BlockDataConfig: LoadedConfiguration("protect", "blockdata", resource = false, instantCreation = true) {

    fun addApprovedBlock(location: Location){
        val list = this.getConfig().getStringList("Allowed-Blocks")
        list.add("${location.world!!.name}, ${location.blockX}, ${location.blockY}, ${location.blockZ}")
        this.getConfig().set("Allowed-Blocks", list)
        this.reloadConfig()
    }

    fun removeApprovedBlock(location: Location){
        val list = this.getConfig().getStringList("Allowed-Blocks")
        list.remove("${location.world!!.name}, ${location.blockX}, ${location.blockY}, ${location.blockZ}")
        this.getConfig().set("Allowed-Blocks", list)
        this.reloadConfig()
    }

    fun isApprovedBlock(location: Location): Boolean{
        val list = this.getConfig().getStringList("Allowed-Blocks")
        if(list.contains("${location.world!!.name}, ${location.blockX}, ${location.blockY}, ${location.blockZ}"))
            return true
        return false
    }

}