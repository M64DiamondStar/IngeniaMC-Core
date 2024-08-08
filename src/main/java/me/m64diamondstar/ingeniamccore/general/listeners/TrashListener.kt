package me.m64diamondstar.ingeniamccore.general.listeners

import com.jeff_media.customblockdata.CustomBlockData
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.inventory.utilities.TrashCanInventory
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class TrashListener: Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent){
        val player = event.player
        if(event.action != Action.RIGHT_CLICK_BLOCK) return
        if(event.clickedBlock == null || event.clickedBlock!!.type.isAir) return

        val data = CustomBlockData(event.clickedBlock!!, IngeniaMC.plugin)
        if(data.has(NamespacedKey(IngeniaMC.plugin, "trash-can"), PersistentDataType.BOOLEAN)){
            event.isCancelled = true

            val featureManager = FeatureManager()
            if(!featureManager.isFeatureEnabled(FeatureType.TRASHCANS) && !player.hasPermission("ingenia.admin")){
                player.sendMessage(Messages.featureDisabled())
                return
            }

            val trashCanInventory = TrashCanInventory(player)
            trashCanInventory.open()

        }
    }

}