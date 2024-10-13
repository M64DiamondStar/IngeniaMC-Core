package me.m64diamondstar.ingeniamccore.protect.listeners

import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

class EntityMountListener: Listener {

    private val cooldowns = HashMap<UUID, Long>()

    @EventHandler(priority = EventPriority.HIGH)
    fun onMount(event: EntityMountEvent){
        if(event.entity !is Player) return
        if(!cooldowns.containsKey(event.entity.uniqueId)) {
            cooldowns[event.entity.uniqueId] = System.currentTimeMillis()
            return
        }

        if(System.currentTimeMillis() - cooldowns[event.entity.uniqueId]!! <= 2000){
            (event.entity as Audience).sendActionBar(MiniMessage.miniMessage().deserialize("<${MessageType.WARNING}>Please slow down..."))
            event.isCancelled = true
            cooldowns[event.entity.uniqueId] = System.currentTimeMillis()
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent){
        cooldowns.remove(event.player.uniqueId)
    }

}