package me.m64diamondstar.ingeniamccore.utils.rewards

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.Wands

class Reward(private val rewardType: RewardType, private val arg: String) {

    fun execute(player: IngeniaPlayer) = rewardType.execute(player, arg)

    override fun toString(): String = "$rewardType: $arg"

    fun getType(): RewardType = rewardType

    fun getDisplay(): String{

        var string = ""

        when (rewardType){
            RewardType.EXP -> {
                string = Colors.format(MessageType.SUCCESS + "$arg exp")
            }

            RewardType.GS -> {
                string = Colors.format(MessageType.SUCCESS + "$arg:gs:")
            }

            RewardType.WAND -> {
                string = Colors.format(Wands.getWandDisplayName(arg))
            }
        }

        return string
    }

}