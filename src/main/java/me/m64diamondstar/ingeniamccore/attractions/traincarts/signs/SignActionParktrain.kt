package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.scheduler.BukkitRunnable

class SignActionParktrain : SignAction() {
    override fun match(info: SignActionEvent): Boolean {
        return info.isType("parktrain")
    }

    override fun execute(info: SignActionEvent) {
        if (info.isAction(SignActionType.GROUP_ENTER)) {
            if (!info.hasGroup()) return
            object : BukkitRunnable() {
                var c = 30
                override fun run() {
                    if (c == 30) {
                        info.group.properties.playersEnter = true
                        info.group.properties.playersExit = true
                    }
                    if (c == 0) {
                        info.group.properties.playersEnter = false
                        info.group.properties.playersExit = false
                        cancel()
                        return
                    }
                    for (member in info.group) {
                        for (player in member.entity.playerPassengers) {
                            (player as Audience).sendActionBar(MiniMessage.miniMessage()
                                .deserialize(
                                    if(c != 1) MessageType.INGENIA + "Train is leaving in $c seconds..."
                                    else MessageType.INGENIA + "Train is leaving in $c second..."
                                ))
                        }
                    }
                    c -= 1
                }
            }.runTaskTimer(IngeniaMC.plugin, 120L, 20L)
        }
    }

    override fun build(event: SignChangeActionEvent): Boolean {
        return SignBuildOptions.create()
            .setName(if (event.isCartSign) "Parktrain Station" else "Parktrain Station")
            .setDescription("...")
            .handle(event.player)
    }
}