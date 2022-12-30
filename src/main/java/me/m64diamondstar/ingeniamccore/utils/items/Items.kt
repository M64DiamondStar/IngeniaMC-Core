package me.m64diamondstar.ingeniamccore.utils.items

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
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

    fun getRandomPresentProfile(): GameProfile{
        val randomTexture = listOf("1c6274c22d726fc120ce25736030cc8af238b44bcbf56655207953c414422f",
            "6c8652bfdb7adde128e7eacc50d16eb9f487a3209b304de3b9697cebf13323b",
            "4acb3c1e1b34f8734aedfabd1e1f5e0b280bef924fb8bbf3e692d2538266f4",
            "928e692d86e224497915a39583dbe38edffd39cbba457cc95a7ac3ea25d445",
            "6cef9aa14e884773eac134a4ee8972063f466de678363cf7b1a21a85b7",
            "ed97f4f44e796f79ca43097faa7b4fe91c445c76e5c26a5ad794f5e479837",).shuffled().first()

        val profile = GameProfile(UUID.randomUUID(), null)
        val encodedData: ByteArray = Base64.getEncoder().encode(
            java.lang.String.format(
                "{textures:{SKIN:{url:\"%s\"}}}",
                "http://textures.minecraft.net/texture/$randomTexture"
            ).toByteArray()
        )

        profile.properties.put("textures", Property("textures", String(encodedData)))
        return profile
    }
}