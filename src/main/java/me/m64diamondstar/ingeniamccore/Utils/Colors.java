package me.m64diamondstar.ingeniamccore.Utils;

import net.md_5.bungee.api.ChatColor;

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
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String format(String msg, MessageType messageType) {
        switch (messageType){
            case ERROR:
                return format("#bd4d4d" + msg);
            case BACKGROUND:
                return format("#858585" + msg);
            case SUCCESS:
                return format("#53bd4d" + msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
