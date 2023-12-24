package me.m64diamondstar.ingeniamccore.general.levels

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.rewards.Reward
import me.m64diamondstar.ingeniamccore.general.rewards.RewardType

object LevelUtils {

    fun getHighestLevel(): Int {
        var maxLevel = 0
        for(currentLevel in IngeniaMC.plugin.config.getConfigurationSection("Levels")!!.getKeys(false)){
            maxLevel++
        }
        return maxLevel
    }

    fun getExpFromLevel(level: Int): Long{
        return IngeniaMC.plugin.config.getLong("Levels.$level.Requirement")
    }

    fun isLevelUp(previousExp: Long, newExp: Long): Boolean{
        if(getLevel(previousExp) < getLevel(newExp)) {
            return true
        }
        return false
    }

    fun getLevelUpLevels(previousExp: Long, newExp: Long): List<Int>{
        if(getLevel(previousExp) < getLevel(newExp)) {
            val list = ArrayList<Int>()
            for(level in getLevel(previousExp) + 1..getLevel(newExp)){
                list.add(level)
            }
            return list
        }
        return emptyList()
    }

    fun getLevel(exp: Long): Int {
        var maxLevel = 0
        for(currentLevel in IngeniaMC.plugin.config.getConfigurationSection("Levels")!!.getKeys(false)){
            if(IngeniaMC.plugin.config.getLong("Levels.$currentLevel.Requirement") > exp){
                return currentLevel.toInt() - 1
            }
            maxLevel++
        }
        return maxLevel
    }

    fun getRewards(level: Int): List<Reward>{

        if(IngeniaMC.plugin.config.get("Levels.$level.Rewards") != null){
            val list = ArrayList<Reward>()
            for(reward in IngeniaMC.plugin.config.getStringList("Levels.$level.Rewards")){
                val args = reward.split(": ")
                list.add(Reward(RewardType.valueOf(args[0].uppercase()), args[1]))
            }
            return list
        }
        return emptyList()
    }

}