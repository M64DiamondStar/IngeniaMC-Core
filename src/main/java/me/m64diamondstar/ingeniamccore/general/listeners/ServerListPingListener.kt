package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.Main
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerListPingListener: Listener {

    @EventHandler
    fun onServerListPing(event: ServerListPingEvent){
        event.motd = Colors.format(Main.plugin.config.getString("Motd")!!)
    }

}