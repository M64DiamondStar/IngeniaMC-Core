package me.m64diamondstar.ingeniamccore.attractions.utils

import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

enum class CountdownType {

    COUNTDOWN {
        override fun sendActionBarMessage(player: Player, countdown: Int) {
            if(countdown != 1) {
                (player as Audience).sendActionBar(
                    Component.text("Ride will dispatch in $countdown seconds.").color(
                        TextColor
                            .fromHexString(MessageType.PLAYER_UPDATE)))
            }else{
                (player as Audience).sendActionBar(
                    Component.text("Ride will dispatch in 1 second.").color(
                        TextColor
                            .fromHexString(MessageType.PLAYER_UPDATE)))
            }
        }
    },
    SOON {
        override fun sendActionBarMessage(player: Player, countdown: Int) {
            (player as Audience).sendActionBar(
                Component.text("Ride will dispatch soon.").color(
                    TextColor
                        .fromHexString(MessageType.PLAYER_UPDATE)))
        }
    },
    NONE {
        override fun sendActionBarMessage(player: Player, countdown: Int) {return}
    },
    OTHER_PLAYERS {
        override fun sendActionBarMessage(player: Player, countdown: Int) {
            (player as Audience).sendActionBar(
                Component.text("Waiting for other players to enter.").color(
                    TextColor
                        .fromHexString(MessageType.PLAYER_UPDATE)))
        }
    };

    abstract fun sendActionBarMessage(player: Player, countdown: Int)

}