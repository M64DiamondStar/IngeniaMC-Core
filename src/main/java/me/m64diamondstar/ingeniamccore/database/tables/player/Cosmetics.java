package me.m64diamondstar.ingeniamccore.database.tables.player;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Cosmetics {

    private Main plugin;

    public Cosmetics(){
        this.plugin = Main.getPlugin(Main.class);
        createTable();
    }


    public void createTable(){
        try{
            assert plugin.sql != null;
            plugin.sql.connect();
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerCosmetics " +
                    "(NAME VARCHAR(100),UUID VARCHAR(100),EXP BIGINT(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void createPlayer(Player player){
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            UUID uuid = player.getUniqueId();
            if(!exists(uuid)){
                PreparedStatement ps = plugin.sql.getConnection().prepareStatement("INSERT IGNORE INTO playerCosmetics" +
                        " (NAME,UUID,EXP) VALUES (?,?,?)");
                ps.setString(1, player.getName());
                ps.setString(2, uuid.toString());
                ps.setLong(3, 0);
                ps.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public boolean exists(UUID uuid) {
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM playerCosmetics WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            return results.next();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public void addExp(Player player, long exp) {
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE playerCosmetics SET EXP=? WHERE UUID=?");
            ps.setLong(1, (getExp(player) + exp));
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void setExp(Player player, long exp) {
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE playerCosmetics SET EXP=? WHERE UUID=?");
            ps.setLong(1, exp);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public long getExp(Player player) {
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT EXP FROM playerCosmetics WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet results = ps.executeQuery();
            long exp;
            if(results.next()) {
                exp = results.getLong("EXP");
                return exp;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

}
