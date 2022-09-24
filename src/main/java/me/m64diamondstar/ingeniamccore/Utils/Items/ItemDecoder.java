package me.m64diamondstar.ingeniamccore.Utils.Items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemDecoder {

    private String encodedItem;

    public ItemDecoder(String encodedItem){
        this.encodedItem = encodedItem;
    }

    public ItemStack decodedItem(){

        ItemStack item = null;

        byte[] rawData = Base64.getDecoder().decode(encodedItem);

        try{

            ByteArrayInputStream io = new ByteArrayInputStream(rawData);
            BukkitObjectInputStream in = new BukkitObjectInputStream(io);

            item = (ItemStack) in.readObject();

            in.close();

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return item;
    }

}
