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

}