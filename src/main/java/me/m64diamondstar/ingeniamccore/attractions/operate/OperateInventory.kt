package me.m64diamondstar.ingeniamccore.attractions.operate

import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.custom.Frisbee
import me.m64diamondstar.ingeniamccore.attractions.custom.Slide
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class OperateInventory(player: IngeniaPlayer, private val attraction: Attraction): InventoryHandler(player) {
    override fun setDisplayName(): Component {
        return Component.text("Operate")
    }

    override fun setSize(): Int {
        return 45
    }

    override fun onClick(event: InventoryClickEvent) {
        /*
         GATES
        */
        if(event.slot == 10){
            attraction.closeGates()
            getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
        }

        if(event.slot == 12){
            attraction.openGates()
            getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
        }

        /*
         DESPAWN
        */
        if(event.slot == 14){
            if(attraction.getType() == AttractionType.COASTER){
                val coaster = Coaster(attraction.getCategory(), attraction.getName())
                coaster.despawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.FREEFALL){
                val freeFall = FreeFall(attraction.getCategory(), attraction.getName())
                freeFall.despawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.SLIDE){
                val slide = Slide(attraction.getCategory(), attraction.getName())
                slide.despawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.FRISBEE){
                val frisbee = Frisbee(attraction.getCategory(), attraction.getName())
                frisbee.despawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }
        }

        /*
         SPAWN
        */
        if(event.slot == 16){
            if(attraction.getType() == AttractionType.COASTER){
                val coaster = Coaster(attraction.getCategory(), attraction.getName())
                coaster.spawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.FREEFALL){
                val freeFall = FreeFall(attraction.getCategory(), attraction.getName())
                freeFall.spawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.SLIDE){
                val slide = Slide(attraction.getCategory(), attraction.getName())
                slide.spawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.FRISBEE){
                val frisbee = Frisbee(attraction.getCategory(), attraction.getName())
                frisbee.spawn()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }
        }

        /*
         DISPATCH
        */
        if(event.slot == 31){
            if(attraction.getType() == AttractionType.COASTER){
                val coaster = Coaster(attraction.getCategory(), attraction.getName())
                coaster.dispatch()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.FREEFALL){
                val freeFall = FreeFall(attraction.getCategory(), attraction.getName())
                freeFall.dispatch()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }else if(attraction.getType() == AttractionType.SLIDE){
                getPlayer().sendMessage(Messages.invalidAttractionFunction())
            }else if(attraction.getType() == AttractionType.FRISBEE){
                val frisbee = Frisbee(attraction.getCategory(), attraction.getName())
                frisbee.dispatch()
                getPlayer().player.playSound(getPlayer().player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
            }
        }
    }

    override fun onOpen(event: InventoryOpenEvent) {
        val inventory = event.inventory
        val item = ItemStack(Material.RED_WOOL)
        val meta = item.itemMeta!!
        meta.setDisplayName(Colors.format(MessageType.ERROR + "Close"))
        item.itemMeta = meta
        inventory.setItem(10, item)

        item.type = Material.GREEN_WOOL
        meta.setDisplayName(Colors.format(MessageType.SUCCESS + "Open"))
        item.itemMeta = meta
        inventory.setItem(12, item)

        item.type = Material.OAK_FENCE_GATE
        meta.setDisplayName(Colors.format(MessageType.INFO + "&lGates"))
        item.itemMeta = meta
        inventory.setItem(11, item)



        item.type = Material.RED_WOOL
        meta.setDisplayName(Colors.format(MessageType.ERROR + "Despawn"))
        item.itemMeta = meta
        inventory.setItem(14, item)

        item.type = Material.GREEN_WOOL
        meta.setDisplayName(Colors.format(MessageType.SUCCESS + "Spawn"))
        item.itemMeta = meta
        inventory.setItem(16, item)

        item.type = Material.MINECART
        meta.setDisplayName(Colors.format(MessageType.INFO + "&lDespawn / Spawn"))
        item.itemMeta = meta
        inventory.setItem(15, item)



        item.type = Material.REDSTONE
        meta.setDisplayName(Colors.format(MessageType.INFO + "&lDispatch"))
        item.itemMeta = meta
        inventory.setItem(31, item)
    }

    override fun onClose(event: InventoryCloseEvent) {}

}