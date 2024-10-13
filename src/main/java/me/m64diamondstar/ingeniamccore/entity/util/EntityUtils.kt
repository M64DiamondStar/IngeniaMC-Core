package me.m64diamondstar.ingeniamccore.entity.util

import io.github.retrooper.packetevents.util.SpigotConversionUtil
import io.github.retrooper.packetevents.util.SpigotReflectionUtil
import org.bukkit.Location

object EntityUtils {

    fun nextEntityId(): Int {
        return SpigotReflectionUtil.generateEntityId()
    }

    fun convertBukkitToEntityLocation(bukkitLocation: Location): com.github.retrooper.packetevents.protocol.world.Location {
        return SpigotConversionUtil.fromBukkitLocation(bukkitLocation)
    }

}