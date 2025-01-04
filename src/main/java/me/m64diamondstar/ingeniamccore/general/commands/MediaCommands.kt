package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MediaCommands: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        when(command.name){
            "website" -> (sender as Audience).sendMessage(MiniMessage.miniMessage()
                .deserialize("<${MessageType.INGENIA}><click:open_url:\"https://ingeniamc.net\">Click to visit our <#ffffff>website!"))

            "store" -> (sender as Audience).sendMessage(MiniMessage.miniMessage()
                .deserialize("<${MessageType.INGENIA}><click:open_url:\"https://store.ingeniamc.net\">Click to visit our <#ffffff>store!"))

            "map" -> (sender as Audience).sendMessage(MiniMessage.miniMessage()
                .deserialize("<${MessageType.INGENIA}><click:open_url:\"https://map.ingeniamc.net\">Click to visit our <#ffffff>map!"))

            "discord" -> (sender as Audience).sendMessage(MiniMessage.miniMessage()
                .deserialize("<#7289DA><click:open_url:\"https://discord.com/invite/qv2vsAsDs8\">Click to join our <#ffffff>discord server!"))

            "youtube" -> (sender as Audience).sendMessage(MiniMessage.miniMessage()
                .deserialize("<#CD201F><click:open_url:\"https://www.youtube.com/@Ingenia_MC\">Click to check out our <#ffffff>youtube channel!"))

            "tiktok" -> (sender as Audience).sendMessage(MiniMessage.miniMessage()
                .deserialize("<gradient:#ff0050:#00f2ea><click:open_url:\"https://www.tiktok.com/@ingeniamc\">Click to view our <#ffffff>tiktok!"))

            "instagram" -> (sender as Audience).sendMessage(MiniMessage.miniMessage()
                .deserialize("<#8a3ab9><click:open_url:\"https://www.instagram.com/ingenia_mc\">Click to open our <#ffffff>insta!"))
        }

        return true
    }
}