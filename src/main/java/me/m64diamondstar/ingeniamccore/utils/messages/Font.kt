package me.m64diamondstar.ingeniamccore.utils.messages

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


}