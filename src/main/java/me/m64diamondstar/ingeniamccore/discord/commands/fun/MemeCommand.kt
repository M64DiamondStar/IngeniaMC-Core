package me.m64diamondstar.ingeniamccore.discord.commands.`fun`

import com.github.blad3mak3r.memes4j.Meme
import com.github.blad3mak3r.memes4j.Memes4J
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class MemeCommand: ListenerAdapter() {


    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if(!event.name.equals("meme", ignoreCase = true))
            return

        event.deferReply().queue()

        val memeRequest = Memes4J.getRandomMeme()

        try {
            var meme: Meme

            do{
                meme = memeRequest.complete()
            }while (meme.nsfw)

            val embedBuilder = EmbedBuilder()

            embedBuilder.setTitle(meme.title)
            embedBuilder.setImage(meme.image)
            embedBuilder.setFooter(meme.author)
            embedBuilder.setColor(Color.decode("#ffb833"))

            event.hook.sendMessageEmbeds(embedBuilder.build()).queue()

        } catch (e: Exception){
            e.printStackTrace()
        }
    }


}