package me.m64diamondstar.ingeniamccore.general.areas.listeners

import com.craftmend.openaudiomc.api.EventApi
import com.craftmend.openaudiomc.api.events.client.ClientConnectEvent
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

object AudioConnectListener {

    fun startListeners(){

        EventApi.getInstance().registerHandler(ClientConnectEvent::class.java) {
            val player = Bukkit.getPlayer(it.client.actor.uniqueId) ?: return@registerHandler
            (player as Audience).sendMessage(
                MiniMessage.miniMessage().deserialize(
                    "<${MessageType.ERROR}>Please use /fixaudio if your audio isn't playing properly.")
            )
        }

    }

}