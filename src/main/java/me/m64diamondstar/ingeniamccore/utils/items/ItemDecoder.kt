package me.m64diamondstar.ingeniamccore.utils.items

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.lang.ClassNotFoundException
import java.util.*

/**
 * Decode items from an ItemStack to a string
 * @param encodedItem the string to decode into an ItemStack
 */
class ItemDecoder(private val encodedItem: String) {
    fun decodedItem(): ItemStack? {
        var item: ItemStack? = null
        val rawData = Base64.getDecoder().decode(encodedItem)
        try {
            val io = ByteArrayInputStream(rawData)
            val `in` = BukkitObjectInputStream(io)
            item = `in`.readObject() as ItemStack
            `in`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return item
    }
}