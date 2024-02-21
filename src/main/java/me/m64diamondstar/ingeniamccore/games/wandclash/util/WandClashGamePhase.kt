package me.m64diamondstar.ingeniamccore.games.wandclash.util

enum class WandClashGamePhase {

    DISABLED {
        override fun isJoinable(): Boolean = false
    },
    INACTIVE {
        override fun isJoinable(): Boolean = true
    },
    WAITING_FOR_PLAYERS {
        override fun isJoinable(): Boolean = true
    },
    MODE_VOTE {
        override fun isJoinable(): Boolean = true
    },
    TEAM_CHOOSE {
        override fun isJoinable(): Boolean = true
    },
    STARTING {
        override fun isJoinable(): Boolean = false
    },
    ONGOING {
        override fun isJoinable(): Boolean = false
    },
    ENDING {
        override fun isJoinable(): Boolean = false
    };

    abstract fun isJoinable(): Boolean

}