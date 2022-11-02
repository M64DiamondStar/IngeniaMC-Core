package me.m64diamondstar.ingeniamccore.utils.leaderboard;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderboardRegistry {

    private static Map<UUID, Integer> boards = new HashMap<>();

    public static Map<UUID, Integer> getBoards(){
        return boards;
    }

    public static void setBoard(OfflinePlayer player, int id){
        setBoard(player.getUniqueId(), id);
    }

    public static void setBoard(UUID uuid, int id){
        boards.put(uuid, id);
    }

}
