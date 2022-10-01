package me.m64diamondstar.ingeniamccore.utils.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemEncoder {

    private ItemStack item;

    public ItemEncoder(ItemStack item){
        this.item = item;
    }

    public String encodedItem() {

        String encodedItem = null;

        try {

            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

            os.writeObject(item);

            os.flush();

            byte[] rawData = io.toByteArray();

           encodedItem = Base64.getEncoder().encodeToString(rawData);

            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedItem;

    }

}
