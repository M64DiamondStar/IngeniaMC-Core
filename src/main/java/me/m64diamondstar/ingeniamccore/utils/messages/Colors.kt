package me.m64diamondstar.ingeniamccore.utils.messages

import net.md_5.bungee.api.ChatColor
import java.awt.Color
import java.util.regex.Pattern

object Colors {
    private val pattern = Pattern.compile("#[a-fA-F0-9]{6}")
    @JvmStatic
    fun format(msg: String): String {
        var msg1 = msg
        var match = pattern.matcher(msg1)
        while (match.find()) {
            val color = msg1.substring(match.start(), match.end())
            msg1 = msg1.replace(color, ChatColor.of(color).toString() + "")
            match = pattern.matcher(msg1)
        }
        return ChatColor.translateAlternateColorCodes('&', msg1).replace(":gs:", "✪")
    }

    fun getJavaColorFromString(string: String): Color? {
        val args = string.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return try {
            val r = args[0].toInt()
            val g = args[1].toInt()
            val b = args[2].toInt()
            Color(r, g, b)
        } catch (e: NumberFormatException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun format(msg: String, messageType: MessageType): String {
        return messageType.toString() + msg
    }
}