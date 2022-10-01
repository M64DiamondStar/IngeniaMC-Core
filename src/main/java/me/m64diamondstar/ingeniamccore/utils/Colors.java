package me.m64diamondstar.ingeniamccore.utils;

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
        return ChatColor.translateAlternateColorCodes('&', msg).replace(":gs:", "✪");
    }

    public static String format(String msg, MessageType messageType) {
        return switch (messageType) {
            case ERROR -> format("#bd4d4d" + msg);
            case BACKGROUND -> format("#858585" + msg);
            case SUCCESS -> format("#53bd4d" + msg);
            case INFO -> format("#6ac4c1" + msg);
        };
    }

}
