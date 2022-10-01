package me.m64diamondstar.ingeniamccore.database.tables.cosmeticitems;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.utils.items.ItemDecoder;
import me.m64diamondstar.ingeniamccore.utils.items.ItemEncoder;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hats {

    private Main plugin;

    public Hats(){
        this.plugin = Main.getPlugin(Main.class);
        createTable();
    }

    /**
     * Create the database table of this class.
     */
    public void createTable(){
        try{

            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS cosmeticsHats " +
                    "(" +
                    "NAME VARCHAR(100)," +
                    "ENCODEDITEM MEDIUMTEXT(100)," +
                    "PRICE BIGINT(50)," +
                    "PRIMARY KEY (NAME))");

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Add a cosmetic item to the database.
     * Please use the name param to create a simple name for easy access to the item.
     */
    public void createItem(ItemStack item, String name){
        try{

            assert plugin.sql != null;
            plugin.sql.connect();

            if(!exists(name)) {

                ItemEncoder encoder = new ItemEncoder(item);

                PreparedStatement ps = plugin.sql.getConnection().prepareStatement("INSERT IGNORE INTO cosmeticHats" +
                        " (NAME,ENCODEDITEM,PRICE) VALUES (?,?,?)");

                ps.setString(1, name);
                ps.setString(2, encoder.encodedItem());
                ps.setLong(3, 0);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Check to see if a cosmetic item with this name already exists.
     */
    public boolean exists(String name) {
        try{

            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM cosmeticsHats WHERE NAME=?");
            ps.setString(1, name);
            ResultSet results = ps.executeQuery();

            return results.next();

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Set the price for a specific item.
     */
    public void setPrice(String name, long price){
        try{

            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE cosmeticsHats SET PRICE=? WHERE NAME=?");
            ps.setLong(1, price);
            ps.setString(2, name);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Return the item from the given name.
     * @return ItemStack with the given name.
     */
    public ItemStack getItem(String name){
        try{

            assert plugin.sql != null;
            plugin.sql.connect();

            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT ENCODEDITEM FROM cosmeticsHats WHERE NAME=?");
            ps.setString(1, name);

            ResultSet results = ps.executeQuery();

            ItemStack item;

            if(results.next()) {
                ItemDecoder decoder = new ItemDecoder(results.getString("ENCODEDITEM"));
                item = decoder.decodedItem();
                return item;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
