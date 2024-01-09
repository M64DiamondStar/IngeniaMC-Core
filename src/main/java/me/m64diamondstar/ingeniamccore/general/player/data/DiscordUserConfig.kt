package me.m64diamondstar.ingeniamccore.general.player.data

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import java.util.*

class DiscordUserConfig(id: Long) : DataConfiguration("data/discord-user", id.toString()) {

    init {
        getConfig().set("Username", DiscordBot.jda.getUserById(id)?.name)
        save()
    }

    fun hasMinecraft(): Boolean {
        return getConfig().get("Minecraft") != null
    }

    fun setMinecraft(uuid: UUID?){
        getConfig().set("Minecraft", uuid.toString())
        save()
    }

    fun getMinecraft(): UUID? {
        return if (getConfig().get("Minecraft") != null)
            UUID.fromString(getConfig().getString("Minecraft"))
        else
            null
    }

}