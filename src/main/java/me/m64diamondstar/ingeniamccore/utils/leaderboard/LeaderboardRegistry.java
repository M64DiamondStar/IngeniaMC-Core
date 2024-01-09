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

    public static void setBoard(String attractionID, OfflinePlayer player, int id){
        setBoard(attractionID, player.getUniqueId(), id);
    }

    public static void setBoard(String attractionID, UUID uuid, int id){
        Map<UUID, Integer> playerMaps;
        if(getBoards().containsKey(attractionID)){
            playerMaps = getBoards().get(attractionID);
        }else{
            playerMaps = new HashMap<>();
        }
        playerMaps.put(uuid, id);
        boards.put(attractionID, playerMaps);
    }

    public static Integer getId(String attractionID, OfflinePlayer player){
        if(boards.containsKey(attractionID)){
            Map<UUID, Integer> playerMaps = getBoards().get(attractionID);
            return playerMaps.get(player.getUniqueId());
        }
        return null;
    }

}
