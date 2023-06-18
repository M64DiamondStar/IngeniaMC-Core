package me.m64diamondstar.ingeniamccore.shows.editor.effect

import me.m64diamondstar.ingeniamccore.shows.editor.show.EditShowGui
import me.m64diamondstar.ingeniamccore.shows.utils.Effect
import me.m64diamondstar.ingeniamccore.shows.EffectShow
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.shows.editor.utils.GuiItems
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.gui.Gui
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class CreateEffectGui(private val player: Player, effectShow: EffectShow): Gui(IngeniaPlayer(player)) {

    private val showCategory: String = effectShow.getCategory()
    private val showName: String = effectShow.getName()

    override fun setDisplayName(): String {
        return "Creating effect for $showName..."
    }

    override fun setSize(): Int {
        return 36
    }

    override fun handleInventory(event: InventoryClickEvent) {
        if(event.currentItem == null) return

        if(event.slot == 31){
            val editEffectShowGui = EditShowGui(event.whoClicked as Player, EffectShow(showCategory, showName, null))
            editEffectShowGui.open()
        }

        if(event.slot !in 9..26) return

        Effect.Type.values().forEach {
            if(event.currentItem!!.itemMeta!!.lore?.last()?.split(": ")!![1] == it.toString()){

                val effectShow = EffectShow(showCategory, showName, null)
                val id = effectShow.getMaxId() + 1
                val effect = it.getTypeClass(effectShow, id)
                effectShow.setDefaults(id, filterDefaults(player, effect))

                val editEffectShowGui = EditShowGui(player, EffectShow(showCategory, showName, null))
                editEffectShowGui.open()

            }
        }
    }

    override fun setInventoryItems() {
        for(i in 0..8) inventory.setItem(i, GuiItems.getBlackPane())
        for(i in 27..35) inventory.setItem(i, GuiItems.getBlackPane())

        inventory.setItem(31, GuiItems.getBack())

        val item = ItemStack(Material.STONE)
        val meta = item.itemMeta!!
        Effect.Type.values().forEach {
            item.type = it.getDisplayMaterial()
            meta.setDisplayName(Colors.format("#dcb5ff&l${it.toString().lowercase().replace("_", " ")
                .replaceFirstChar(Char::titlecase)}"))
            meta.lore = listOf(
                Colors.format(MessageType.BACKGROUND + "Click to choose this effect."),
                Colors.format(MessageType.BACKGROUND + "Enum Type: $it")
            )
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

            item.itemMeta = meta
            inventory.addItem(item)
        }
    }

    private fun filterDefaults(player: Player, effect: Effect): List<Pair<String, Any>>{
        val filtered = ArrayList<Pair<String, Any>>()

        effect.getDefaults().forEach {
            when (it.first){
                "Location" -> {
                    filtered.add(Pair("Location", LocationUtils.getStringFromLocation(player.location)))
                }

                else -> {
                    filtered.add(Pair(it.first, it.second))
                }
            }
        }

        return filtered
    }
}