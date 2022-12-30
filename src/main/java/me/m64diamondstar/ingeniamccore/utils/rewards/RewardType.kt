package me.m64diamondstar.ingeniamccore.utils.rewards

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer

enum class RewardType {

    EXP {
        override fun execute(player: IngeniaPlayer, arg: String) {
            player.addExp(arg.toLong())
        }
    },
    GS {
        override fun execute(player: IngeniaPlayer, arg: String) {
            player.addBal(arg.toLong())
        }
    },
    WAND {
        override fun execute(player: IngeniaPlayer, arg: String) {
            player.givePermission("ingeniawands.$arg")
        }
    };

    abstract fun execute(player: IngeniaPlayer, arg: String)

}