package me.m64diamondstar.ingeniamccore.utils

enum class SettingButtons {

    TOGGLE_1_ON{
        override fun getUnicode(): String {
            return "\uEB05"
        }
    },
    TOGGLE_1_OFF{
        override fun getUnicode(): String {
            return "\uEB06"
        }
    },
    TOGGLE_2_ON{
        override fun getUnicode(): String {
            return "\uEB07"
        }
    },
    TOGGLE_2_OFF{
        override fun getUnicode(): String {
            return "\uEB08"
        }
    },
    TOGGLE_3_ON{
        override fun getUnicode(): String {
            return "\uEB09"
        }
    },
    TOGGLE_3_OFF{
        override fun getUnicode(): String {
            return "\uEB0A"
        }
    },
    TOGGLE_4_ON{
        override fun getUnicode(): String {
            return "\uEB0B"
        }
    },
    TOGGLE_4_OFF{
        override fun getUnicode(): String {
            return "\uEB0C"
        }
    },
    TOGGLE_5_ON{
        override fun getUnicode(): String {
            return "\uEB0D"
        }
    },
    TOGGLE_5_OFF{
        override fun getUnicode(): String {
            return "\uEB0E"
        }
    },
    PLAYERS_1_ALL{
        override fun getUnicode(): String {
            return "\uEB0F"
        }
    },
    PLAYERS_1_STAFF{
        override fun getUnicode(): String {
            return "\uEB10"
        }
    },
    PLAYERS_1_NONE{
        override fun getUnicode(): String {
            return "\uEBA1"
        }
    },
    PLAYERS_2_ALL{
        override fun getUnicode(): String {
            return "\uEBA2"
        }
    },
    PLAYERS_2_STAFF{
        override fun getUnicode(): String {
            return "\uEBA3"
        }
    },
    PLAYERS_2_NONE{
        override fun getUnicode(): String {
            return "\uEBA4"
        }
    },
    PLAYERS_3_ALL{
        override fun getUnicode(): String {
            return "\uEBA5"
        }
    },
    PLAYERS_3_STAFF{
        override fun getUnicode(): String {
            return "\uEBA6"
        }
    },
    PLAYERS_3_NONE{
        override fun getUnicode(): String {
            return "\uEBA7"
        }
    },
    PLAYERS_4_ALL{
        override fun getUnicode(): String {
            return "\uEBA8"
        }
    },
    PLAYERS_4_STAFF{
        override fun getUnicode(): String {
            return "\uEBA9"
        }
    },
    PLAYERS_4_NONE{
        override fun getUnicode(): String {
            return "\uEBAA"
        }
    },
    PLAYERS_5_ALL{
        override fun getUnicode(): String {
            return "\uEBAB"
        }
    },
    PLAYERS_5_STAFF{
        override fun getUnicode(): String {
            return "\uEBAC"
        }
    },
    PLAYERS_5_NONE{
        override fun getUnicode(): String {
            return "\uEBAD"
        }
    };

    abstract fun getUnicode(): String

    companion object {
        fun getFromPlayerSelector(playerSelectors: PlayerSelectors, row: Int): SettingButtons {
            when(playerSelectors){
                PlayerSelectors.ALL -> {
                    return when(row){
                        1 -> PLAYERS_1_ALL
                        2 -> PLAYERS_2_ALL
                        3 -> PLAYERS_3_ALL
                        4 -> PLAYERS_4_ALL
                        5 -> PLAYERS_5_ALL
                        else -> PLAYERS_1_ALL
                    }
                }
                PlayerSelectors.STAFF -> {
                    return when(row){
                        1 -> PLAYERS_1_STAFF
                        2 -> PLAYERS_2_STAFF
                        3 -> PLAYERS_3_STAFF
                        4 -> PLAYERS_4_STAFF
                        5 -> PLAYERS_5_STAFF
                        else -> PLAYERS_1_STAFF
                    }
                }
                PlayerSelectors.NONE -> {
                    return when(row){
                        1 -> PLAYERS_1_NONE
                        2 -> PLAYERS_2_NONE
                        3 -> PLAYERS_3_NONE
                        4 -> PLAYERS_4_NONE
                        5 -> PLAYERS_5_NONE
                        else -> PLAYERS_1_NONE
                    }
                }
            }
        }
    }

}