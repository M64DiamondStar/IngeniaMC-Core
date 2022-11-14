package me.m64diamondstar.ingeniamccore.shows.utils

import org.bukkit.configuration.ConfigurationSection

abstract class EffectType(private val show: Show, private val id: Int) {

    enum class Types {
        ANIMATRONIC,
        ANIMATRONIC_GROUP,
        DIALOGUE,
        FALLING_BLOCK,
        FILL_BLOCK,
        FIREWORK,
        FOUNTAIN,
        PARTICLE,
        PARTICLE_EMITTER,
        PARTICLE_LINE,
        SET_BLOCK
    }

    abstract fun execute()

    abstract fun getType(): Types

    abstract fun isSync(): Boolean

    fun getSection(): ConfigurationSection {
        return show.getConfig().getConfigurationSection("$id")!!
    }

    fun getShow(): Show {
        return show
    }

}