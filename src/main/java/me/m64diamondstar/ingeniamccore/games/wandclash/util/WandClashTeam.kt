package me.m64diamondstar.ingeniamccore.games.wandclash.util

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

enum class WandClashTeam {

    INCENDIO {
        override fun getDisplayName(): String {
            return "Incendio"
        }

        override fun getColoredDisplayName(): String {
            return Colors.format("#db8d70&lI#db896c&ln#db8569&lc#db8165&le#db7c61&ln#db785d&ld#db745a&li#db7056&lo")
        }

        override fun getComponentDisplayName(): Component {
            return MiniMessage.miniMessage().deserialize("<gradient:#DB8D70:#DB7056>Incendio")
        }
    },
    GLACIUS {
        override fun getDisplayName(): String {
            return "Glacius"
        }

        override fun getColoredDisplayName(): String {
            return Colors.format("#75aefa&lG#70a9f6&ll#6aa5f2&la#65a0ef&lc#5f9beb&li#5a97e7&lu#5492e3&ls")
        }

        override fun getComponentDisplayName(): Component {
            return MiniMessage.miniMessage().deserialize("<gradient:#75aefa:#5492e3>Glacius")
        }
    };


    abstract fun getDisplayName(): String
    abstract fun getColoredDisplayName(): String
    abstract fun getComponentDisplayName(): Component

}