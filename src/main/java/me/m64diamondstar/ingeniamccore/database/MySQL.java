package me.m64diamondstar.ingeniamccore.database;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    final private String host;
    final private String port;
    final private String database;
    final private String username;
    final private String password;

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
            Bukkit.getLogger().info("Database not connected ✗");
        }

        if(this.isConnected()){
            Bukkit.getLogger().info("Database is connected ✓");
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

    public Connection getConnection(){
        return this.connection;
    }

}
