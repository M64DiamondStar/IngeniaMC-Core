package me.m64diamondstar.ingeniamccore.general.listeners;

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player bukkitPlayer = e.getPlayer();
        IngeniaPlayer player = new IngeniaPlayer(bukkitPlayer);
        player.setScoreboard(true);

    }

}
