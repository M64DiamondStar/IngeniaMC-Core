package me.m64diamondstar.ingeniamccore.discord.commands.minecraft

import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import org.bukkit.Statistic
import java.awt.Color

class PlaytimeCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if(!event.name.equals("playtime", ignoreCase = true))
            return

        event.deferReply(false).queue()

        if(!BotUtils.LinkedUtils.isLinked(event.user.idLong) || BotUtils.LinkedUtils.getLinked(event.user.idLong) == null || !Bukkit.getOfflinePlayer(BotUtils.LinkedUtils.getLinked(event.user.idLong)!!).hasPlayedBefore()){
            val embedBuilder = EmbedBuilder()
            embedBuilder.setTitle("Not linked!")
            embedBuilder.setDescription("You are not linked with a Minecraft account. \n" +
                    "Use `/link ${event.user.idLong}` in the minecraft server to link your Minecraft account.")
            event.hook.sendMessageEmbeds(embedBuilder.build()).queue()
            return
        }

        val player = Bukkit.getOfflinePlayer(BotUtils.LinkedUtils.getLinked(event.user.idLong)!!)

        val ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE)
        val totalSeconds: Int = ticks / 20
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val playTimeFormatted = String.format("%02dh %02dm %02ds", hours, minutes, seconds)

        val embedBuilder = EmbedBuilder()

        when(ticks){
            in 0..216000 -> {
                embedBuilder.setDescription("Welcome to IngeniaMC! You currently have $playTimeFormatted of playtime.")
            }

            in 216001..1728000 -> {
                embedBuilder.setDescription("Hey! You currently have $playTimeFormatted of playtime.")
            }

            in 1728001..7200000 -> {
                embedBuilder.setDescription("Nice! You already have $playTimeFormatted of playtime.")
            }

            in 7200001..36000000 -> {
                embedBuilder.setDescription("Woah, you seem like a big IngeniaMC fan! You have $playTimeFormatted of playtime!")
            }

            in 36000001..144000000 -> {
                embedBuilder.setDescription("You've already played for so long! You have $playTimeFormatted of playtime!")
            }

            else -> {
                embedBuilder.setDescription("Go touch grass, you have $playTimeFormatted of playtime...")
            }
        }

        embedBuilder.setColor(Color.decode("#ffb833"))

        event.hook.sendMessageEmbeds(embedBuilder.build()).queue()

    }

}