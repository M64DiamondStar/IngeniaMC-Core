package me.m64diamondstar.ingeniamccore.general.rewards

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.utils.Wands

class Reward(private val rewardType: RewardType, private val arg: String) {

    fun execute(player: IngeniaPlayer) = rewardType.execute(player, arg)

    override fun toString(): String = "$rewardType: $arg"

    fun getType(): RewardType = rewardType

    fun getDisplay(): String{

        val string: String = when (rewardType){
            RewardType.EXP -> {
                Colors.format("#ffffff$arg${MessageType.SUCCESS} exp")
            }

            RewardType.GS -> {
                Colors.format("#ffffff$arg${MessageType.INGENIA}:gs:")
            }

            RewardType.WAND -> {
                Colors.format(Wands.getWandDisplayName(arg) ?: "null")
            }
        }

        return string
    }

}