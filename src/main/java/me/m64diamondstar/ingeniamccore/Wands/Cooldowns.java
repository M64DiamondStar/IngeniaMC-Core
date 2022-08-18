package me.m64diamondstar.ingeniamccore.Wands;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Cooldowns {

    static Map<String, Long> cooldowns = new HashMap<>();

    public static void addPlayer(Player player, Long op, Long vipp, Long vip, Long visitor){

        if(player.hasPermission("ingeniawands.owner") || player.isOp()) {
            cooldowns.put(player.getName(), System.currentTimeMillis() + (op));
        }

        else if(player.hasPermission("ingenia.vip+")) {
            cooldowns.put(player.getName(), System.currentTimeMillis() + (vipp));
        }

        else if(player.hasPermission("ingenia.vip")) {
            cooldowns.put(player.getName(), System.currentTimeMillis() + (vip));
        }

        else {
            cooldowns.put(player.getName(), System.currentTimeMillis() + (visitor));
        }
    }



    public static boolean isOnCooldown(Player player){
        if(cooldowns.containsKey(player.getName())) {
            if(cooldowns.get(player.getName()) > System.currentTimeMillis()) {

                long timeleft = (cooldowns.get(player.getName()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("§6§lI§e§lngenia §8§l> §cWait §4" + timeleft + " §4Second(s) §cto use this wand again!");

                return true;
            }
        }
        return false;
    }
}
