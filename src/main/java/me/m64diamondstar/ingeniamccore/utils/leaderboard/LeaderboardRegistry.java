package me.m64diamondstar.ingeniamccore.utils.leaderboard;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderboardRegistry {

    private static final Map<String, Map<UUID, Integer>> boards = new HashMap<>();

    public static Map<String, Map<UUID, Integer>> getBoards(){
        return boards;
    }

    public static void setBoard(String attractionName, OfflinePlayer player, int id){
        setBoard(attractionName, player.getUniqueId(), id);
    }

    public static void setBoard(String attractionName, UUID uuid, int id){
        Map<UUID, Integer> playerMaps;
        if(getBoards().containsKey(attractionName)){
            playerMaps = getBoards().get(attractionName);
        }else{
            playerMaps = new HashMap<>();
        }
        playerMaps.put(uuid, id);
        boards.put(attractionName, playerMaps);
    }

    public static Integer getId(String attractionName, OfflinePlayer player){
        if(boards.containsKey(attractionName)){
            Map<UUID, Integer> playerMaps = getBoards().get(attractionName);
            return playerMaps.get(player.getUniqueId());
        }
        return null;
    }

}
