package me.m64diamondstar.ingeniamccore.Database;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Main plugin;

    private final String host;

    {
        assert false;
        host = plugin.getConfig().getString("MySQL.Host");
    }
    
    private final String port = plugin.getConfig().getString("MySQL.Port");
    private final String database = plugin.getConfig().getString("MySQL.Database");
    private final String username = plugin.getConfig().getString("MySQL.Username");
    private final String password = plugin.getConfig().getString("MySQL.Password");

    private Connection connection;

    public boolean isConnected(){
        return (connection != null);
    }

    public void connect() throws SQLException {
        if(isConnected()) return;
        connection = DriverManager.getConnection("jdbc:mysql://" + username + ":" + password + "@" + host + ":" + port + "/" + database + "?useSSL=false",
                username, password);
    }

    public void disconnect(){
        if(!isConnected()) return;
        try{
            connection.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public MySQL(Main main){
        plugin = main;

        try {
            this.connect();
        } catch (SQLException e) {
            Bukkit.getLogger().info("------------------------");
            Bukkit.getLogger().info(Color.RED + " Database not connected!");
            Bukkit.getLogger().info("------------------------");
        }

        if(this.isConnected()){
            Bukkit.getLogger().info("------------------------");
            Bukkit.getLogger().info(Color.GREEN + " Database is connected!");
            Bukkit.getLogger().info("------------------------");
        }


    }

}
