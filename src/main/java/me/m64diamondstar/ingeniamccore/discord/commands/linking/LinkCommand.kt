package me.m64diamondstar.ingeniamccore.discord.commands.linking

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit

class LinkCommand: ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if(event.button.id != "verifyDiscordLink") return
        if(event.channelType != ChannelType.PRIVATE) return

        event.deferReply().queue()

        val id = event.user.idLong

        if(!BotUtils.LinkingUtils.isLinking(id)) {
            event.hook.sendMessage("Session is over. Please use '/link' in the Minecraft Server again.").queue()
            return
        }

        if(Bukkit.getPlayer(BotUtils.LinkingUtils.getLinking(id)!!) != null ||
            Bukkit.getPlayer(BotUtils.LinkingUtils.getLinking(id)!!)!!.isOnline) {

            val player = Bukkit.getPlayer(BotUtils.LinkingUtils.getLinking(id)!!)!!
            val ingeniaPlayer = IngeniaPlayer(player)

            player.sendMessage(Colors.format(MessageType.INFO + "You're now successfully linked with your Minecraft account!"))
            ingeniaPlayer.playerConfig.setDiscord(event.user.idLong)
            ingeniaPlayer.setNewLinkAttempt()

            event.hook.sendMessage("Successfully linked!").queue()

            val guild = DiscordBot.jda.getGuildById(IngeniaMC.plugin.config.getLong("Discord.Bot.Guild-ID"))
            val linkedRole = guild?.getRoleById(IngeniaMC.plugin.config.getLong("Discord.Bot.Linked-Role-ID"))

            if (linkedRole != null) {
                guild.addRoleToMember(event.user, linkedRole).queue()
            }
        }else{
            event.hook.sendMessage("The Minecraft account has to be online in order to verify.").queue()
        }
    }
}