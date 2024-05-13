package me.m64diamondstar.ingeniamccore.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.level.ServerPlayer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*


object PlayerGameProfile {

    fun getTextureProfile(player: Player, displayName: String): GameProfile {
        val textures = getFromPlayer(player)
        val gameProfile = GameProfile(UUID.randomUUID(), displayName)
        gameProfile.properties.put("textures", Property("textures", textures[0], textures[1]))
        return gameProfile
    }

    fun getFromName(name: String): Array<String>? {
        try {
            val url0: URL = URL("https://api.mojang.com/users/profiles/minecraft/$name")
            val reader0: InputStreamReader = InputStreamReader(url0.openStream())
            val uuid: String = JsonParser().parse(reader0).getAsJsonObject().get("id").asString

            val url1: URL = URL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid?unsigned=false")
            val reader1: InputStreamReader = InputStreamReader(url1.openStream())
            val textureProperty: JsonObject =
                JsonParser.parseReader(reader1).getAsJsonObject().get("properties").getAsJsonArray().get(0)
                    .getAsJsonObject()
            val texture: String = textureProperty.get("value").asString
            val signature: String = textureProperty.get("signature").asString

            return arrayOf(texture, signature)
        } catch (e: IOException) {
            System.err.println("Could not get skin data from session servers!")
            e.printStackTrace()
            return null
        }
    }

    fun getFromPlayer(playerBukkit: Player): Array<String> {
        val playerNMS: ServerPlayer = (playerBukkit as CraftPlayer).handle
        val profile: GameProfile = playerNMS.gameProfile
        val property = profile.properties["textures"].iterator().next()
        val texture: String = property.value
        val signature: String = property.signature!!
        return arrayOf(texture, signature)
    }

}