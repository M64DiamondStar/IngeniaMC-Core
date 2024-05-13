package me.m64diamondstar.ingeniamccore.discord.commands.minecraft

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import java.awt.Color

class LevelCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if(!event.name.equals("playtime", ignoreCase = true))
            return

        event.deferReply(false).queue()

        if(!BotUtils.LinkedUtils.isLinked(event.user.idLong) || BotUtils.LinkedUtils.getLinked(event.user.idLong) == null || !Bukkit.getOfflinePlayer(
                BotUtils.LinkedUtils.getLinked(event.user.idLong)!!).hasPlayedBefore()){
            event.hook.sendMessage("You are not linked with a Minecraft account.").queue()
            return
        }

        val player = Bukkit.getOfflinePlayer(BotUtils.LinkedUtils.getLinked(event.user.idLong)!!)

        val embedBuilder = EmbedBuilder()



        embedBuilder.setColor(Color.decode("#ffb833"))

        event.hook.sendMessageEmbeds(embedBuilder.build()).queue()

    }

}