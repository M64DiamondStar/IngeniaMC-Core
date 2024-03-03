package me.m64diamondstar.ingeniamccore.games.wandclash.util

enum class WandClashGamePhase {

    DISABLED {
        override fun isJoinable(): Boolean = false
        override fun isPreGame(): Boolean = false
    },
    WAITING_FOR_PLAYERS {
        override fun isJoinable(): Boolean = true
        override fun isPreGame(): Boolean = true
    },
    MODE_VOTE {
        override fun isJoinable(): Boolean = true
        override fun isPreGame(): Boolean = true
    },
    TEAM_CHOOSE {
        override fun isJoinable(): Boolean = true
        override fun isPreGame(): Boolean = true
    },
    STARTING {
        override fun isJoinable(): Boolean = false
        override fun isPreGame(): Boolean = false
    },
    ONGOING {
        override fun isJoinable(): Boolean = false
        override fun isPreGame(): Boolean = false
    },
    ENDING {
        override fun isJoinable(): Boolean = false
        override fun isPreGame(): Boolean = false
    };

    abstract fun isJoinable(): Boolean
    abstract fun isPreGame(): Boolean

}