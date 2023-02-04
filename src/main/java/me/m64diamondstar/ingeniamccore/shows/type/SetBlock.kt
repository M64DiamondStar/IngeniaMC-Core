package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.utils.Effect
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

class SetBlock(show: Show, private val id: Int) : Effect(show, id) {

    override fun execute(players: List<Player>?) {
        try {
            val location = LocationUtils.getLocationFromString(getSection().getString("Location")!!) ?: return
            val material =
                if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!.uppercase()) else Material.STONE
            val duration = if (getSection().get("Duration") != null) getSection().getLong("Duration") else 0
            val real = if (getSection().get("Real") != null) getSection().getBoolean("Real") else false
            val normalBlock = location.block
            val normalBlockType = location.block.type
            val normalBlockData = location.block.blockData

            if (real) {
                location.block.type = material

                Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                    location.block.type = normalBlockType
                    location.block.blockData = normalBlockData
                }, duration)
            } else {
                if(players != null){
                    players.forEach { it.sendBlockChange(location, material.createBlockData()) }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                        players.forEach { it.sendBlockChange(location, normalBlock.blockData) }
                    }, duration)
                }else{
                    for (player in Bukkit.getOnlinePlayers())
                        player.sendBlockChange(location, material.createBlockData())
                    Bukkit.getScheduler().scheduleSyncDelayedTask(IngeniaMC.plugin, {
                        for (player in Bukkit.getOnlinePlayers())
                            player.sendBlockChange(location, normalBlock.blockData)
                    }, duration)
                }
            }
        }catch (ex: IllegalArgumentException){
            IngeniaMC.plugin.logger.warning("Couldn't play effect with ID $id from ${getShow().getName()} in category ${getShow().getCategory()}.")
            IngeniaMC.plugin.logger.warning("The block you entered doesn't exist. Please choose a valid material.")
        }

    }

    override fun getType(): Type {
        return Type.SET_BLOCK
    }

    override fun isSync(): Boolean {
        return true
    }

    override fun getDefaults(): List<Pair<String, Any>> {
        val list = ArrayList<Pair<String, Any>>()
        list.add(Pair("Type", "SET_BLOCK"))
        list.add(Pair("Location", "world, 0, 0, 0"))
        list.add(Pair("Block", "STONE"))
        list.add(Pair("Duration", 100))
        list.add(Pair("Real", false))
        list.add(Pair("Delay", 0))
        return list
    }
}