package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.commands.BotUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.exceptions.ContextException
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.awt.Color
import kotlin.concurrent.thread

class LinkCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(Messages.noPlayer())
            return false
        }

        val player: Player = sender
        val ingeniaPlayer = IngeniaPlayer(player)

        if(string.equals("link", ignoreCase = true)){ // When player uses /link\
            if(!ingeniaPlayer.isNewLinkingAttemptAvailable()){
                player.sendMessage(Colors.format(MessageType.ERROR + "You're on cooldown at the moment. Please wait ${ingeniaPlayer.getLinkingCooldown()}."))
                return false
            }

            // Check if player is already linked
            if(ingeniaPlayer.playerConfig.hasDiscord()){
                player.sendMessage(Colors.format(MessageType.ERROR + "You're already linked! " +
                        "Please use '/unlink' if you want to link your discord again."))
                return false
            }

            // Player is not linked yet.
            if(args.size != 1){
                player.sendMessage(Colors.format(MessageType.ERROR + "Please use '/link <discord-id>'."))
                player.sendMessage(Colors.format(MessageType.ERROR + "If you don't know your Discord ID, use '/whatismyid' in our Discord server."))
                player.sendMessage(Colors.format(MessageType.ERROR + "! You can only use this ONCE A DAY !"))
                return false
            }

            thread {// Start a new thread for retrieving discord user

                try {
                    if (args[0].toLongOrNull() == null || DiscordBot.jda.retrieveUserById(args[0])
                            .complete() == null
                    ) { // Check if discord user id exists
                        player.sendMessage(Colors.format(MessageType.ERROR + "This Discord ID does not exist."))
                        player.sendMessage(Colors.format(MessageType.ERROR + "Please use '/link <discord-id>'."))
                        return@thread
                    }
                }catch (ex: ErrorResponseException){
                    player.sendMessage(Colors.format(MessageType.ERROR + "This Discord ID does not exist."))
                    player.sendMessage(Colors.format(MessageType.ERROR + "Please use '/link <discord-id>'."))
                    return@thread
                }

                // Check if discord user is in ingeniamc discord
                if (DiscordBot.jda.getGuildById(IngeniaMC.plugin.config.getLong("Discord.Bot.Guild-ID"))
                        ?.retrieveMemberById(args[0])?.complete() == null
                ) {
                    player.sendMessage(Colors.format(MessageType.ERROR + "This Discord user is not part of the IngeniaMC Discord."))
                    return@thread
                }

                val member = DiscordBot.jda.getGuildById(IngeniaMC.plugin.config.getLong("Discord.Bot.Guild-ID"))
                    ?.retrieveMemberById(args[0])?.complete()!!

                // Check if member is already linked
                if (BotUtils.findRole(member, "Linked with '/link'") != null) {
                    player.sendMessage(Colors.format(MessageType.ERROR + "This Discord user is already linked."))
                    player.sendMessage(Colors.format(MessageType.ERROR + "Use '/unlink' on the linked Minecraft account to unlink."))
                    return@thread
                }


                // All checks passed
                // Member is not linked yet

                val embedBuilder = EmbedBuilder()

                embedBuilder.setTitle("**Link Request**")
                embedBuilder.setDescription(
                    "The Minecraft Account `${player.name}` wants to link to your Discord.\n" +
                            "Please click the `Verify` button to link this account."
                )
                embedBuilder.setThumbnail("https://visage.surgeplay.com/full/${player.uniqueId}.png")
                embedBuilder.setColor(Color.decode("#73db70"))

                val verifyButton = Button.success("verifyDiscordLink", "Verify")

                member.user.openPrivateChannel().queue {
                    it.sendMessageEmbeds(embedBuilder.build()).addActionRow(verifyButton).queue()
                }

                // Put the player and discord in a linking session
                BotUtils.LinkingUtils.addLinking(member.idLong, player.uniqueId)

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully sent a request! Check your DMs."))
            }
        }

        else{ // Only other option is /unlink

            val guild = DiscordBot.jda.getGuildById(IngeniaMC.plugin.config.getLong("Discord.Bot.Guild-ID"))
            val linkedRole = guild?.getRoleById(IngeniaMC.plugin.config.getLong("Discord.Bot.Linked-Role-ID"))

            if(ingeniaPlayer.playerConfig.getDiscordID() == 0L){
                player.sendMessage(Colors.format(MessageType.ERROR + "Already unlinked."))
                return false
            }

            // Remove role from member
            try {
                guild?.retrieveMemberById(ingeniaPlayer.playerConfig.getDiscordID())?.queue {
                    if (linkedRole != null)
                        guild.removeRoleFromMember(it, linkedRole).queue()
                }
            }catch (_: ErrorResponseException){ }
            catch (_: ContextException){}

            // Set discord to null in player config
            ingeniaPlayer.playerConfig.setDiscord(null)

            player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully unlinked your account."))
        }

        return false
    }
}