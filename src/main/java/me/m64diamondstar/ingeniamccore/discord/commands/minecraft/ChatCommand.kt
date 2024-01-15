package me.m64diamondstar.ingeniamccore.discord.commands.minecraft

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class ChatCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "minecraft-chat") return
        if (event.channelType != ChannelType.TEXT) return
        if (BotUtils.findRole(event.member!!, "Lead") != null){ // User has Lead
            event.deferReply(true).queue()

            val embedBuilder = EmbedBuilder()

            embedBuilder.setTitle("Minecraft Chat")
            embedBuilder.setDescription("Set this channel to the Minecraft Chat")
            embedBuilder.setThumbnail("https://ingeniamc.net/images/nametag.png")
            embedBuilder.setColor(Color.decode("#ffb833"))

            BotUtils.ChatUtils.chatChannel = event.channel.asTextChannel()

            event.hook.sendMessageEmbeds(embedBuilder.build()).queue()
        }
    }

}