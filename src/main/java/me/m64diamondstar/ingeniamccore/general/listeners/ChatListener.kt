package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.EmojiUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandSendEvent

class ChatListener: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onChat(event: AsyncPlayerChatEvent){
        val player = event.player
        val ingeniaPlayer = IngeniaPlayer(player)

        if(FeatureManager().isFeatureEnabled(FeatureType.DISCORD_CHAT_SYNC) && BotUtils.ChatUtils.chatChannel != null && !event.isCancelled && event.message.replace("\\", "").isNotBlank()){
            DiscordBot.jda.getTextChannelById(BotUtils.ChatUtils.chatChannel!!.id)?.sendMessage(
                "**${ingeniaPlayer.rawPrefix}** ${player.name.replace("_", "\\_")} » ${addBracketsToUrls(event.message
                    .replace("@", "`@`")
                    .replace("\\", "")
                    .replace("```", "`"))}"
            )?.queue()
        }

        if(player.hasPermission("ingenia.vip+") || player.isOp)
            event.format = Colors.format("${ingeniaPlayer.prefix}&r " +
                    "${ingeniaPlayer.name} » " +
                    EmojiUtils.addEmoji(event.message.replace("%", "%%"))
            )

        else if(player.hasPermission("ingenia.vip"))
            event.format = ingeniaPlayer.prefix + Colors.format("&r ") +
                    "${ingeniaPlayer.name} » " +
                    ChatColor.translateAlternateColorCodes('&',
                        EmojiUtils.addEmoji(event.message.replace("%", "%%"))
            )
        else
            event.format = ingeniaPlayer.prefix + Colors.format("&r ") +
                    "${ingeniaPlayer.name} » " +
                    EmojiUtils.addEmoji(event.message.replace("%", "%%"))

    }

    @EventHandler
    fun onPlayerCommandSend(event: PlayerCommandSendEvent){
        val player = event.player

        if(player.hasPermission("ingenia.team")) return

        event.commands.clear()
        event.commands.addAll( IngeniaMC.plugin.config.getStringList("Player-Commands"))
    }

    private fun addBracketsToUrls(input: String): String {
        val urlPattern = Regex("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

        return urlPattern.replace(input) {
            "<${it.value}>"
        }
    }

}