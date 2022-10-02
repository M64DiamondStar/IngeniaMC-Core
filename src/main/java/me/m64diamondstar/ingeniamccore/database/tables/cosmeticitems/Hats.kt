package me.m64diamondstar.ingeniamccore.database.tables.cosmeticitems

import me.m64diamondstar.ingeniamccore.Main
import java.sql.SQLException
import me.m64diamondstar.ingeniamccore.utils.items.ItemEncoder
import me.m64diamondstar.ingeniamccore.utils.items.ItemDecoder
import org.bukkit.inventory.ItemStack

class Hats {
    private val plugin: Main

    /**
     * Create the database table of this class.
     */
    fun createTable() {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            plugin.sql!!.connection?.prepareStatement(
                "CREATE TABLE IF NOT EXISTS cosmeticsHats " +
                        "(" +
                        "NAME VARCHAR(100)," +
                        "ENCODEDITEM MEDIUMTEXT(100)," +
                        "PRICE BIGINT(50)," +
                        "PRIMARY KEY (NAME))"
            )?.executeUpdate()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    /**
     * Add a cosmetic item to the database.
     * Please use the name param to create a simple name for easy access to the item.
     */
    fun createItem(item: ItemStack?, name: String?) {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            if (!exists(name)) {
                val encoder = ItemEncoder(item)
                val ps = plugin.sql!!.connection?.prepareStatement(
                    "INSERT IGNORE INTO cosmeticHats" +
                            " (NAME,ENCODEDITEM,PRICE) VALUES (?,?,?)"
                )
                ps?.setString(1, name)
                ps?.setString(2, encoder.encodedItem())
                ps?.setLong(3, 0)
            }
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    /**
     * Check to see if a cosmetic item with this name already exists.
     */
    fun exists(name: String?): Boolean {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val ps = plugin.sql!!.connection?.prepareStatement("SELECT * FROM cosmeticsHats WHERE NAME=?")
            ps?.setString(1, name)
            val results = ps?.executeQuery()
            if (results != null) {
                return results.next()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Set the price for a specific item.
     */
    fun setPrice(name: String?, price: Long) {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val ps = plugin.sql!!.connection?.prepareStatement("UPDATE cosmeticsHats SET PRICE=? WHERE NAME=?")
            ps?.setLong(1, price)
            ps?.setString(2, name)
            ps?.executeUpdate()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    /**
     * Return the item from the given name.
     * @return ItemStack with the given name.
     */
    fun getItem(name: String?): ItemStack? {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val ps = plugin.sql!!.connection?.prepareStatement("SELECT ENCODEDITEM FROM cosmeticsHats WHERE NAME=?")
            ps?.setString(1, name)
            val results = ps?.executeQuery()
            val item: ItemStack
            if (results != null) {
                if (results.next()) {
                    val decoder = ItemDecoder(results.getString("ENCODEDITEM"))
                    item = decoder.decodedItem()
                    return item
                }
            }
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
        return null
    }

    init {
        plugin = Main.plugin
        createTable()
    }
}