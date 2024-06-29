package me.m64diamondstar.ingeniamccore.npc.utils

enum class DialogueBackdropType {
    DEFAULT{
        override fun getBackdrop(): String {
            return "\uEE01"
        }
    },
    HIGHWAY{
        override fun getBackdrop(): String {
            return "\uEE02"
        }
    },
    INGENIAMC{
        override fun getBackdrop(): String {
            return "\uEE03"
        }

    };

    abstract fun getBackdrop(): String
}