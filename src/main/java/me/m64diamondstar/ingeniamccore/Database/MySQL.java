package me.m64diamondstar.ingeniamccore.Database;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    private Connection connection;

    public MySQL(Main main){

        port = main.getConfig().getString("MySQL.Port");
        database = main.getConfig().getString("MySQL.Database");
        username = main.getConfig().getString("MySQL.Username");
        password = main.getConfig().getString("MySQL.Password");
        host = main.getConfig().getString("MySQL.Host");

        try {
            this.connect();
        } catch (SQLException e) {
            Bukkit.getLogger().info("------------------------");
            Bukkit.getLogger().info("Database not connected!");
            Bukkit.getLogger().info("------------------------");
        }

        if(this.isConnected()){
            Bukkit.getLogger().info("------------------------");
            Bukkit.getLogger().info("Database is connected!");
            Bukkit.getLogger().info("------------------------");
        }


    }

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

}
