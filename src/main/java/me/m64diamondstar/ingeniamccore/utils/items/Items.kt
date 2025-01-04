package me.m64diamondstar.ingeniamccore.utils.items

import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.util.*


object Items {

    fun getPlayerHead(fUrl: String?): ItemStack {
        var url = fUrl
        val skull = ItemStack(Material.PLAYER_HEAD)
        if (url.isNullOrEmpty()) return skull
        if(!url.contains("textures.minecraft.net"))
            url = "http://textures.minecraft.net/$url"
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

    fun getRandomPresentProfile(): PlayerProfile{
        val randomTexture = listOf("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTcyNmQ5ZDA2MzJlNDBiZGE1YmNmNjU4MzliYTJjYzk4YTg3YmQ2MTljNTNhZGYwMDMxMGQ2ZmM3MWYwNDJiNSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTM3NTA2MWQwOGYxZDdiMzE3Njc1YWE3ZmE4ODAwZDZmMjA2NmUwMThkOWY5MWVjZGRmOWNhZjMwNGU5N2U5MiJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFlNDJlMzcyNWMyYjRhZTY5MDA1ODBjNGUyYTZiODMwZjZlY2EwMjExZjdhMzY0MTQzM2ZjNjdmYmM0M2QzZiJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZlMTJmZGFlMWZjZWJhNjg3OWY2NTk3OTYxMzJhN2ZmYTA4Y2Q5MmEyNmNiN2ExMDY3ZDQ5NzAzZDdiMWI0YiJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQyOWIwNmM3YzM3Y2JkMmE3NjU5MDgyNzdmYThlYWQwZTRkYzY2YTExM2YzNDdkZTNiYWI5MWZhZGU0NjkxMiJ9fX0=").shuffled().first()

        val playerProfile = Bukkit.createProfile(UUID.randomUUID())
        playerProfile.setProperty(ProfileProperty("textures", randomTexture))
        return playerProfile
    }
}