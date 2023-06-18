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

    fun getBukkitColorList(string: String): List<org.bukkit.Color>{
        val arg = string.split(", ")
        val list = ArrayList<org.bukkit.Color>()

        arg.forEach {
            if(it.matches(pattern.toRegex()))
                list.add(org.bukkit.Color.fromRGB(
                    java.awt.Color.decode(it).red,
                    java.awt.Color.decode(it).green,
                    java.awt.Color.decode(it).blue)
                )
        }
        return list
    }

    fun isColorList(string: String): Boolean {
        val arg = string.split(", ")

        arg.forEach {
            if(!it.matches(pattern.toRegex())) return false
        }
        return true
    }

    fun format(msg: String, messageType: MessageType): String {
        return messageType.toString() + msg
    }
}