package me.m64diamondstar.ingeniamccore.npc.utils

enum class DialogueBackdropType {
    DEFAULT{
        override fun getBackdrop(): String {
            return "\uFF01"
        }
    },
    HIGHWAY{
        override fun getBackdrop(): String {
            return "\uFF02"
        }
    },
    INGENIAMC{
        override fun getBackdrop(): String {
            return "\uFF03"
        }

    };

    abstract fun getBackdrop(): String
}