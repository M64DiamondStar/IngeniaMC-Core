package me.m64diamondstar.ingeniamccore.protect.listeners

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.protect.moderation.ModerationRegistry
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatListener: Listener {

    private val times = HashMap<Player, ArrayList<Long>>()
    private val words = HashMap<Player, ArrayList<String>>()

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    fun onPlayerChat(event: AsyncPlayerChatEvent){
        val player = event.player

        for(blockedWord in ModerationRegistry.getBlockedWords()){
            if(event.message.lowercase().contains("\\b$blockedWord\\b".toRegex())){
                val timesList = if(times.contains(player)) times[player]!! else ArrayList()
                val wordsList = if(words.contains(player)) words[player]!! else ArrayList()

                wordsList.add(blockedWord)
                timesList.add(System.currentTimeMillis())
                timesList.forEach {
                    if(System.currentTimeMillis() - it > 60000)
                        times.remove(player)
                }

                if(timesList.size >= 6){
                    player.sendMessage(Colors.format(MessageType.ERROR + "Your messages contain potentially harmful words. " +
                            "A report will be sent to the staff for safety reasons."))
                    sendReport(player, event.message, wordsList)
                    times.remove(player)
                    words.remove(player)
                    return
                }

                words[player] = wordsList
                times[player] = timesList
            }
        }
    }

    private fun sendReport(player: Player, message: String, words: ArrayList<String>){

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val timeNow = LocalDateTime.now()

        val embedBuilder = EmbedBuilder()

        embedBuilder.setAuthor(player.name, null, "https://visage.surgeplay.com/face/${player.uniqueId}.png")
        embedBuilder.setDescription(" » $message\n" +
                "\n" +
                "**Words used since last report**: ${words.joinToString(", ")}")
        embedBuilder.setFooter(
            "Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                    "  ${dateTimeFormatter.format(timeNow)}", null
        )
        embedBuilder.setColor(Color.decode(MessageType.ERROR))

        DiscordBot.jda.getGuildById(IngeniaMC.plugin.config.getString("Discord.Bot.Guild-ID")!!)!!
            .getTextChannelById(IngeniaMC.plugin.config.getString("Discord.Webhook.Potential-Danger-ID")!!)!!
            .sendMessageEmbeds(embedBuilder.build()).queue()
    }

}