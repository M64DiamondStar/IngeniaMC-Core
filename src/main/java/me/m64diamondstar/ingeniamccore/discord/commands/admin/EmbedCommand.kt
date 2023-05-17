package me.m64diamondstar.ingeniamccore.discord.commands.admin

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class EmbedCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(e: SlashCommandInteractionEvent) {
        if (e.name != "embed") return
        if (BotUtils.findRole(e.member!!, "Lead") == null) return

        e.deferReply(true).queue()
        e.hook.sendMessage("**Sending Embed**").queue()

        val embed = EmbedBuilder()
        embed.setTitle(e.getOption("title")!!.asString)
        embed.setDescription(e.getOption("description")!!.asString.replace("/NL", "\n"))
        if (e.getOption("color") != null) embed.setColor(Color.decode(e.getOption("color")!!.asString)) else embed.setColor(
            Color.decode("#fcba03")
        )
        if (e.getOption("channel") != null) e.getOption("channel")!!.asChannel.asTextChannel().sendMessageEmbeds(embed.build())
            .queue() else e.channel.sendMessageEmbeds(embed.build()).queue()

    }

}