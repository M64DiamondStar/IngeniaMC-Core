package me.m64diamondstar.ingeniamccore.utils.items

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.util.*

object Items {

    fun getPlayerHead(url: String?): ItemStack {
        val skull = ItemStack(Material.PLAYER_HEAD)
        if (url == null || url.isEmpty()) return skull
        val skullMeta = skull.itemMeta as SkullMeta?
        val profile = GameProfile(UUID.randomUUID(), null)
        val encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).toByteArray())
        profile.properties.put("textures", Property("textures", String(encodedData)))
        var profileField: Field? = null
        try {
            profileField = skullMeta!!.javaClass.getDeclaredField("profile")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        profileField!!.isAccessible = true
        try {
            profileField[skullMeta] = profile
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        skull.itemMeta = skullMeta
        return skull
    }
}