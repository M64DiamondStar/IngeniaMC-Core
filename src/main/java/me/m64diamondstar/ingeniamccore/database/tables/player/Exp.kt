package me.m64diamondstar.ingeniamccore.database.tables.player

import me.m64diamondstar.ingeniamccore.Main
import java.sql.SQLException
import java.util.UUID
import org.bukkit.entity.Player

class Exp {
    private val plugin: Main = Main.plugin
    private fun createTable() {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            plugin.sql!!.connection?.prepareStatement(
                "CREATE TABLE IF NOT EXISTS exp " +
                        "(NAME VARCHAR(100),UUID VARCHAR(100),EXP BIGINT(100),PRIMARY KEY (NAME))"
            )?.executeUpdate()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    fun createPlayer(player: Player) {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val uuid = player.uniqueId
            if (!exists(uuid)) {
                val ps = plugin.sql!!.connection?.prepareStatement(
                    "INSERT IGNORE INTO exp" +
                            " (NAME,UUID,EXP) VALUES (?,?,?)"
                )
                ps?.setString(1, player.name)
                ps?.setString(2, uuid.toString())
                ps?.setLong(3, 0)
                ps?.executeUpdate()
            }
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    private fun exists(uuid: UUID): Boolean {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val ps = plugin.sql!!.connection?.prepareStatement("SELECT * FROM exp WHERE UUID=?")
            ps?.setString(1, uuid.toString())
            val results = ps?.executeQuery()
            if (results != null) {
                return results.next()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun addExp(player: Player, exp: Long) {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val ps = plugin.sql!!.connection?.prepareStatement("UPDATE exp SET EXP=? WHERE UUID=?")
            ps?.setLong(1, getExp(player) + exp)
            ps?.setString(2, player.uniqueId.toString())
            ps?.executeUpdate()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    fun setExp(player: Player, exp: Long) {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val ps = plugin.sql!!.connection?.prepareStatement("UPDATE exp SET EXP=? WHERE UUID=?")
            ps?.setLong(1, exp)
            ps?.setString(2, player.uniqueId.toString())
            ps?.executeUpdate()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    fun getExp(player: Player): Long {
        try {
            assert(plugin.sql != null)
            plugin.sql!!.connect()
            val ps = plugin.sql!!.connection?.prepareStatement("SELECT EXP FROM exp WHERE UUID=?")
            ps?.setString(1, player.uniqueId.toString())
            val results = ps?.executeQuery()
            val exp: Long
            if (results != null) {
                if (results.next()) {
                    exp = results.getLong("EXP")
                    return exp
                }
            }
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
        return 0
    }

    init {
        createTable()
    }
}