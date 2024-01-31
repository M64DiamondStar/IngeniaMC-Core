package me.m64diamondstar.ingeniamccore.games.wandclash.util

enum class ClashWandType {
    ATTACK {
        override fun getDisplayName(): String {
            return "Attack"
        }
    },
    DEFENSE {
        override fun getDisplayName(): String {
            return "Defence"
        }
    },
    AGILITY {
        override fun getDisplayName(): String {
            return "Agility"
        }
    },
    HEALING {
        override fun getDisplayName(): String {
            return "Healing"
        }
    },
    ILLUSION {
        override fun getDisplayName(): String {
            return "Illusion"
        }
    };


    abstract fun getDisplayName(): String
}
