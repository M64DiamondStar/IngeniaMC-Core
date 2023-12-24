package me.m64diamondstar.ingeniamccore.general.rewards

object RewardUtils {

    /**
     * Tries to get a Reward object from the string.
     * @return reward from the string
     */
    fun fromString(string: String): Reward {
        return Reward(RewardType.valueOf(string.split(": ")[0].uppercase()), string.split(": ")[1])
    }

}