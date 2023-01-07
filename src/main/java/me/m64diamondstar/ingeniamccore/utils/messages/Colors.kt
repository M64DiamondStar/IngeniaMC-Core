package me.m64diamondstar.ingeniamccore.utils.messages

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import java.awt.Color
import java.util.regex.Pattern

object Colors {
    private val pattern = Pattern.compile("#[a-fA-F0-9]{6}")
    @JvmStatic
    fun format(msg: String): String {
        var msg = msg
        var match = pattern.matcher(msg)
        while (match.find()) {
            val color = msg.substring(match.start(), match.end())
            msg = msg.replace(color, ChatColor.of(color).toString() + "")
            match = pattern.matcher(msg)
        }
        return ChatColor.translateAlternateColorCodes('&', msg).replace(":gs:", "✪")
    }

    fun getJavaColorFromString(string: String): Color {
        val args = string.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var r = 0
        var g = 0
        var b = 0
        try {
            r = args[0].toInt()
            g = args[1].toInt()
            b = args[2].toInt()
        } catch (e: NumberFormatException) {
            Bukkit.getLogger().warning(e.cause.toString() + ": Error with converting String to Int in " + e.javaClass)
        }
        return Color(r, g, b)
    }

    fun format(msg: String, messageType: MessageType): String {
        return messageType.toString() + msg
    }
}