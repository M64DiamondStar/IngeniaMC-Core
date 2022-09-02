package me.m64diamondstar.ingeniamccore.Database.Tables;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Table {

    private final Main plugin;

    public Table(Main plugin){
        this.plugin = plugin;
    }

    public void create(String table){
        try{
            if(!plugin.SQL.isConnected())
                plugin.SQL.connect();

            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " " +
                    "(UUID VARCHAR(100),NAME VARCHAR(100),EXP BIGINT(255),PRIMARY KEY (UUID))");

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addObject(String table, ObjectType objectType, Object obj){
        try{
            if(!plugin.SQL.isConnected())
                plugin.SQL.connect();
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE " + objectType + "=?");
            ps.setString(1, (String) obj);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

}
