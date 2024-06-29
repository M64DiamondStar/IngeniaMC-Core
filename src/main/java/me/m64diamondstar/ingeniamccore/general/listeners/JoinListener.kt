package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageBuilder
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player = e.player
        val ingeniaPlayer = IngeniaPlayer(player)

        // Remove the legacy menu item
        if (player.inventory.getItem(4) != null && player.inventory.getItem(4)!!.type == Material.NETHER_STAR && player.inventory.getItem(
                4
            )!!.hasItemMeta() && player.inventory.getItem(4)!!.itemMeta.hasDisplayName()
            && player.inventory.getItem(4)!!.itemMeta.displayName.contains("IngeniaMC")
        ) {
            player.inventory.setItem(4, null)
            player.inventory.setItem(5, null)
        }

        e.joinMessage =
            MessageBuilder.JoinMessageBuilder(ingeniaPlayer.name, ingeniaPlayer.joinColor, ingeniaPlayer.joinMessage)
                .build()

        ingeniaPlayer.startUp()

        if (BotUtils.ChatUtils.chatChannel != null) {
            val joinLeaveMessage = JoinLeaveMessage(MessageType.JOIN)
            DiscordBot.jda.getTextChannelById(BotUtils.ChatUtils.chatChannel!!.id)?.sendMessage(
                "${BotUtils.EmojiUtils.getJoinEmoji()} " +
                        "${
                            joinLeaveMessage.getMessage(ingeniaPlayer.joinMessage ?: "default")
                                ?.replace("%player%", ingeniaPlayer.name)?.replace("_", "\\_")
                        }"
            )?.queue()
        }
    }

}