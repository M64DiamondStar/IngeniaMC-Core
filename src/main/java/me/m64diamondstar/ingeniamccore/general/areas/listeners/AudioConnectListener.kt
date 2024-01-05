package me.m64diamondstar.ingeniamccore.general.areas.listeners

import com.craftmend.openaudiomc.api.impl.event.events.ClientConnectEvent
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

object AudioConnectListener {

    fun startListeners(){
        IngeniaMC.audioApi.eventDriver
            .on(ClientConnectEvent::class.java)
            .setHandler {
                val player = Bukkit.getPlayer(it.client.owner.uniqueId) ?: return@setHandler
                (player as Audience).sendMessage(
                    MiniMessage.miniMessage().deserialize(
                        "<${MessageType.ERROR}>Please use /fixaudio if your audio isn't playing properly.")
                )
            }
    }

}