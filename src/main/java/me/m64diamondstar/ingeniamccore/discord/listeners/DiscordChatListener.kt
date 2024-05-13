package me.m64diamondstar.ingeniamccore.discord.listeners

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.general.player.data.DiscordUserConfig
import me.m64diamondstar.ingeniamccore.utils.EmojiUtils
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

class DiscordChatListener: ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.isWebhookMessage || event.author.isBot) return
        if (event.channelType != ChannelType.TEXT) return
        if(event.channel.asTextChannel().id != BotUtils.ChatUtils.chatChannel!!.id) return
        if(event.message.contentRaw.isEmpty()) return
        val resultMessage = if(event.message.contentRaw.length > 200)
            event.author.name + " » " + event.message.contentRaw.substring(0, 200) + "..."
        else
            event.message.contentRaw

        var hover = ""

        val discordUserConfig = DiscordUserConfig(event.author.idLong)
        if(discordUserConfig.hasMinecraft()) {
            hover = "Linked to ${Bukkit.getOfflinePlayer(discordUserConfig.getMinecraft()!!).name}"
        }

        Bukkit.getOnlinePlayers().forEach {
            (it as Audience).sendMessage(MiniMessage.miniMessage().deserialize(
                "<#7289da>DC<reset> " +
                        (if(hover.isEmpty()) "" else "<hover:show_text:${hover}>") +
                        event.author.name +
                        (if(hover.isEmpty()) "" else "</hover>") +
                        " » ${EmojiUtils.addEmoji(resultMessage)}"
            ))
        }
    }

}