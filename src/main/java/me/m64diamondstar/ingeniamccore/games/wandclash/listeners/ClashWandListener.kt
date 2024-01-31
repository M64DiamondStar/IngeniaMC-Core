package me.m64diamondstar.ingeniamccore.games.wandclash.listeners

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandRegistry
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class ClashWandListener: Listener {

    @EventHandler
    fun onWandUse(event: PlayerInteractEvent){
        if(!event.hasItem()) return
        if(!event.action.toString().contains("CLICK")) return
        if(event.item!!.type != Material.AMETHYST_SHARD) return
        if(!event.item!!.hasItemMeta()) return
        if(!event.item!!.itemMeta.persistentDataContainer.has(NamespacedKey(IngeniaMC.plugin, "clash-wand"))) return

        val wandID = event.item!!.itemMeta.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "clash-wand"), PersistentDataType.STRING) ?: return
        ClashWandRegistry.getClashWand(wandID)?.execute(event.player) ?: return
    }

}