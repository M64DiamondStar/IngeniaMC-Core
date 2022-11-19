package me.m64diamondstar.ingeniamccore.utils.rewards

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.Wands

class Reward(private val rewardType: RewardType, private val arg: String) {

    fun execute(player: IngeniaPlayer){
        when (rewardType){
            RewardType.EXP -> {
                player.addExp(arg.toLong())
            }

            RewardType.GS -> {
                player.addBal(arg.toLong())
            }

            RewardType.WAND -> {
                player.givePermission("ingeniawands.$arg")
            }
        }
    }

    fun getType(): RewardType{
        return rewardType
    }

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