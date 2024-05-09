package me.m64diamondstar.ingeniamccore.general.listeners

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageBuilder
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.npc.utils.DialoguePlayerRegistry
import me.m64diamondstar.ingeniamccore.utils.TeamHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class LeaveListener : Listener {

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val bukkitPlayer = e.player
        bukkitPlayer.walkSpeed = 0.2f
        if(bukkitPlayer.isInsideVehicle){
            bukkitPlayer.leaveVehicle()
        }
        val player = IngeniaPlayer(bukkitPlayer)
        player.updatePlaytime()
        player.updateYearPlaytime()

        DialoguePlayerRegistry.getDialoguePlayer(bukkitPlayer)?.getDialogue(bukkitPlayer)?.end()

        e.quitMessage = MessageBuilder.LeaveMessageBuilder(player.name, player.leaveColor, player.leaveMessage).build()

        if(TeamHandler.containsPlayer(bukkitPlayer))
            TeamHandler.removePlayer(bukkitPlayer)

        if(BotUtils.ChatUtils.chatChannel != null){
            val joinLeaveMessage = JoinLeaveMessage(MessageType.LEAVE)
            DiscordBot.jda.getTextChannelById(BotUtils.ChatUtils.chatChannel!!.id)?.sendMessage(
                "${BotUtils.EmojiUtils.getLeaveEmoji()} " +
                        "${joinLeaveMessage.getMessage(player.leaveMessage ?: "default")?.replace("%player%", player.name)?.replace("_", "\\_")}"
            )?.queue()
        }
        JoinListener.logged.remove(bukkitPlayer.uniqueId)
    }

}