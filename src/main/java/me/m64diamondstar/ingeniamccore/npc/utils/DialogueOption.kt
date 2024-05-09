package me.m64diamondstar.ingeniamccore.npc.utils

import me.m64diamondstar.ingeniamccore.npc.Npc
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class DialogueOption(private val configurationSection: ConfigurationSection) {

    fun getType(): DialogueOptionType? {
        return if(configurationSection.getString("Type") != null && DialogueOptionType.values().map { it.name }.contains(configurationSection.getString("Type")?.uppercase()!!))
            DialogueOptionType.valueOf(configurationSection.getString("Type")!!)
        else
            null
    }

    fun getDisplay(): String? {
        return configurationSection.getString("Display")
    }

    private fun getData(): String? {
        return configurationSection.getString("Data")
    }

    fun execute(player: Player, npc: Npc){
        getType()?.execute(npc, player, getData())
    }

    fun set(type: DialogueOptionType, display: String, data: String) {
        configurationSection.set("Type", type.name)
        configurationSection.set("Display", display)
        configurationSection.set("Data", data)
    }

}