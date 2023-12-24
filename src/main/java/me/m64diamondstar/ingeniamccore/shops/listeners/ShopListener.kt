package me.m64diamondstar.ingeniamccore.shops.listeners

import com.jeff_media.customblockdata.CustomBlockData
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shops.inventories.ShopInventory
import me.m64diamondstar.ingeniamccore.shops.utils.ShopUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class ShopListener: Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent){
        val player = event.player
        if(event.action != Action.RIGHT_CLICK_BLOCK) return
        if(event.clickedBlock == null || event.clickedBlock!!.type.isAir) return
        if(!event.clickedBlock!!.type.toString().contains("SIGN")) return

        val data = CustomBlockData(event.clickedBlock!!, IngeniaMC.plugin)
        if(data.has(NamespacedKey(IngeniaMC.plugin, "shop-category"), PersistentDataType.STRING) &&
            data.has(NamespacedKey(IngeniaMC.plugin, "shop-name"), PersistentDataType.STRING)){
                event.isCancelled = true
                val category = data.get(NamespacedKey(IngeniaMC.plugin, "shop-category"), PersistentDataType.STRING)!!
                val name = data.get(NamespacedKey(IngeniaMC.plugin, "shop-name"), PersistentDataType.STRING)!!
                val shopInventory = ShopInventory(player, category, name)
                shopInventory.open()
            }
    }

    @EventHandler
    fun onSignChange(event: SignChangeEvent){
        val player = event.player
        if(!event.block.type.toString().contains("SIGN")) return

        if(event.getLine(0) == null || event.getLine(1) == null || event.getLine(2) == null) return

        if(event.getLine(0).equals("[shop]", ignoreCase = true)){
            if(ShopUtils.existsCategory(event.getLine(1)!!) && ShopUtils.existsShop(event.getLine(1)!!, event.getLine(2)!!)){
                val data = CustomBlockData(event.block, IngeniaMC.plugin)
                data.set(NamespacedKey(IngeniaMC.plugin, "shop-category"), PersistentDataType.STRING, event.getLine(1)!!)
                data.set(NamespacedKey(IngeniaMC.plugin, "shop-name"), PersistentDataType.STRING, event.getLine(2)!!)

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created shop sign!"))

                event.setLine(0, "-=<>=-")
                event.setLine(1, Colors.format("&lClick&r to open"))
                event.setLine(2, "the shop!")
                event.setLine(3, "-=<>=-")
            }
        }
    }

    @EventHandler
    fun onSignDestroy(event: BlockBreakEvent){
        val player = event.player
        if(!event.block.type.toString().contains("SIGN")) return

        val data = CustomBlockData(event.block, IngeniaMC.plugin)
        if(data.has(NamespacedKey(IngeniaMC.plugin, "shop-category"), PersistentDataType.STRING) &&
            data.has(NamespacedKey(IngeniaMC.plugin, "shop-name"), PersistentDataType.STRING)){
            player.sendMessage(Colors.format(MessageType.ERROR + "Successfully deleted shop sign!"))
        }
    }

}