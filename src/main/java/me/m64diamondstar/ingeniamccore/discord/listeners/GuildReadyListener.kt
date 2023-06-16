package me.m64diamondstar.ingeniamccore.discord.listeners

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

class GuildReadyListener: ListenerAdapter() {

    override fun onGuildReady(event: GuildReadyEvent) {
        val guild = event.guild

        guild.updateCommands()

            .addCommands(Commands.slash("close", "Closes a ticket."))

            .addCommands(Commands.slash("meme", "Show a random meme."))

            .addCommands(Commands.slash("whatismyid", "Get your discord ID."))

            .addCommands(Commands.slash("ip", "Retrieve the server IP and version."))

            .addCommands(Commands.slash("link", "Link your Minecraft account with your Discord account."))

            // ADMIN-ONLY COMMANDS
            .addCommands(Commands.slash("ticket", "Edit ticket configuration.")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)))

            .addCommands(Commands.slash("embed", "Create an embedded message.")
                .addOption(OptionType.STRING, "title", "The title.", true)
                .addOption(OptionType.STRING, "description", "The description.", true)
                .addOption(OptionType.STRING, "color", "The color.", false)
                .addOption(OptionType.CHANNEL, "channel", "The channel to send the message in", false)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)))
            .queue()
    }

}