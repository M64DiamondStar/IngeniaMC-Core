package me.m64diamondstar.ingeniamccore.database

import me.m64diamondstar.ingeniamccore.Main
import org.bukkit.Bukkit
import java.sql.*
import kotlin.Throws

class MySQL(main: Main) {
    private val host: String
    private val port: String
    private val database: String
    private val username: String
    private val password: String
    var connection: Connection? = null
        private set
    val isConnected: Boolean
        get() = connection != null

    @Throws(SQLException::class)
    fun connect() {
        if (isConnected) return
        connection = DriverManager.getConnection(
            "jdbc:mysql://$username:$password@$host:$port/$database?useSSL=false",
            username, password
        )
    }

    fun disconnect() {
        if (!isConnected) return
        try {
            connection!!.close()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    init {
        port = main.config.getString("MySQL.Port")!!
        database = main.config.getString("MySQL.Database")!!
        username = main.config.getString("MySQL.Username")!!
        password = main.config.getString("MySQL.Password")!!
        host = main.config.getString("MySQL.Host")!!
        try {
            connect()
        } catch (e: SQLException) {
            Bukkit.getLogger().info("Database not connected ✗")
        }
        if (isConnected) {
            Bukkit.getLogger().info("Database is connected ✓")
        }
    }
}