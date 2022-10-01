package me.m64diamondstar.ingeniamccore.database.tables.player;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GoldenStars {
    private Main plugin;

    public GoldenStars(){
        this.plugin = Main.getPlugin(Main.class);
        createTable();
    }


    public void createTable(){
        try{
            assert plugin.sql != null;
            plugin.sql.connect();
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS goldenstars " +
                    "(NAME VARCHAR(100),UUID VARCHAR(100),GOLDENSTARS BIGINT(100),PRIMARY KEY (NAME))");
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
                PreparedStatement ps = plugin.sql.getConnection().prepareStatement("INSERT IGNORE INTO goldenstars" +
                        " (NAME,UUID,GOLDENSTARS) VALUES (?,?,?)");
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

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM goldenstars WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            return results.next();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public void addBal(Player player, long exp) {
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE goldenstars SET GOLDENSTARS=? WHERE UUID=?");
            ps.setLong(1, (getBal(player) + exp));
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void setBal(Player player, long exp) {
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE goldenstars SET GOLDENSTARS=? WHERE UUID=?");
            ps.setLong(1, exp);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public long getBal(Player player) {
        try{
            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT GOLDENSTARS FROM goldenstars WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet results = ps.executeQuery();
            long exp;
            if(results.next()) {
                exp = results.getLong("GOLDENSTARS");
                return exp;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
}
