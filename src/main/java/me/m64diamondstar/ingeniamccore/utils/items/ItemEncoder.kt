package me.m64diamondstar.ingeniamccore.utils.items

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

/**
 * Encode items from an ItemStack to a string
 * @param item the ItemStack to encode
 */
class ItemEncoder(private val item: ItemStack) {
    fun encodedItem(): String? {
        var encodedItem: String? = null
        try {
            val io = ByteArrayOutputStream()
            val os = BukkitObjectOutputStream(io)
            os.writeObject(item)
            os.flush()
            val rawData = io.toByteArray()
            encodedItem = Base64.getEncoder().encodeToString(rawData)
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return encodedItem
    }
}