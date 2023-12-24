package me.m64diamondstar.ingeniamccore.utils

enum class Rank {
    VISITOR{
        override fun getDisplayName(): String {
            return "Visitor"
        }
    },
    VIP{
        override fun getDisplayName(): String {
            return "VIP"
        }
    },
    VIP_PLUS{
        override fun getDisplayName(): String {
            return "VIP+"
        }
    },
    TEAM_LEADER{
        override fun getDisplayName(): String {
            return "Team"
        }
    },
    LEAD{
        override fun getDisplayName(): String {
            return "Lead"
        }
    };

    abstract fun getDisplayName(): String
}