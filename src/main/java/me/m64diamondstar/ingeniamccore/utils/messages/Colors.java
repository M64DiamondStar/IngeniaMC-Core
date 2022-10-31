package me.m64diamondstar.ingeniamccore.utils.messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {

    private static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String format(String msg) {
        Matcher match = pattern.matcher(msg);
        while(match.find()) {
            String color = msg.substring(match.start(), match.end());
            msg = msg.replace(color, ChatColor.of(color) + "");
            match = pattern.matcher(msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg).replace(":gs:", "✪");
    }

    public static Color getJavaColorFromString(String string){
        String[] args = string.split(", ");

        int r = 0;
        int g = 0;
        int b = 0;

        try{
            r = Integer.parseInt(args[0]);
            g = Integer.parseInt(args[0]);
            b = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            Bukkit.getLogger().warning(e.getCause() + ": Error with converting String to Int in " + e.getClass());
        }

        return new Color(r, g, b);
    }

    public static String format(String msg, MessageType messageType) {
        return messageType + msg;
    }

}
