package me.m64diamondstar.ingeniamccore.protect

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

class FeatureManager: DataConfiguration("", "features") {

    fun enableFeature(type: FeatureType){
        this.getConfig().set("Features.$type.Enabled", true)
        this.save()
    }

    fun disableFeature(type: FeatureType){
        this.getConfig().set("Features.$type.Enabled", false)
        this.save()
    }

    fun isFeatureEnabled(type: FeatureType): Boolean{
        if(!this.getConfig().contains("Features.$type.Enabled")) return true
        return this.getConfig().getBoolean("Features.$type.Enabled")
    }

    fun sendMessage(player: Player){
        (player as Audience).sendMessage(MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}>This feature is currently disabled. Please contact a team member if you think this is an error."))
    }

}