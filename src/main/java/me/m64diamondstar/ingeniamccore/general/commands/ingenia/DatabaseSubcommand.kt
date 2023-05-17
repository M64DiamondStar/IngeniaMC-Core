package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {
    override fun execute() {

        if(args.size == 2 && args[1].equals("connection-test", ignoreCase = true)){
            val url = IngeniaMC.plugin.config.getString("Database-Url")

            Bukkit.getScheduler().runTaskAsynchronously(IngeniaMC.plugin, Runnable {
                try{
                    val connection = DriverManager.getConnection(url)
                    if(connection != null && !connection.isClosed){
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully connected!"))
                        connection.close()
                    }else{
                        sender.sendMessage(Colors.format(MessageType.ERROR + "Couldn't connect to the database."))
                    }
                }catch (ex: SQLException){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "Couldn't connect to the database."))
                }
            })
        }

    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if(args.size == 2){
            tabs.add("connection-test")
        }

        return tabs
    }
}