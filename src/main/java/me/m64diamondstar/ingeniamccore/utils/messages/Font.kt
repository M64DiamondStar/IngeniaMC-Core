package me.m64diamondstar.ingeniamccore.utils.messages

import me.m64diamondstar.ingeniamccore.npc.utils.CharWidth
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import java.util.ArrayList

object Font {

    fun getGuiNegativeSpace(index: Int): String{
        when (index){
            0 -> return "\uF808"
            1 -> return "\uF80C\uF80A\uF808\uF805"
        }
        return "\uF808"
    }

    interface Characters {
        companion object {
            const val ALLOW = "\uE013"
            const val DENY = "\uE014"
            const val COLOR_SCREEN = "\uFE00"
        }
    }

    fun convertToSmallText(text: String): String {
        val normalAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZĞŞÇÜİÖĄĆĘŁŃÓŚŹŻ"
        val smallTextAlphabet = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀѕᴛᴜᴠᴡxʏᴢğşçüiöᴀᴄ́ᴌśᴏ́ᴢ̇ᴢ́"

        val uppercaseText = text.uppercase()
        var convertedText = ""

        for (char in uppercaseText) {
            val index = normalAlphabet.indexOf(char)

            convertedText += if (index != -1) {
                smallTextAlphabet[index]
            } else {
                char
            }
        }

        return convertedText
    }

    fun getWidget(string: String): Component {
        val stringText = ArrayList<Pair<Char, Int>>()
        string.forEach { if(CharWidth.getAsMap().containsKey(it)) stringText.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        return Component.text("\uE022\uF801") // Left Part
            .append(Component.text().content(stringText.joinToString("") { '\uEF00'.plus(it.second).toString() + "\uF801" }).font(
                Key.key("minecraft:default")))
            .append(Component.text().content("\uE023\uF806\uF806")) // Right part
            .append(Component.text().content(stringText.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
            .append(Component.text().content(stringText.map { it.first }.joinToString("")).font(Key.key("ingeniamc:ui_top")))
            .append(Component.text("\uF826\uF826"))
    }


}