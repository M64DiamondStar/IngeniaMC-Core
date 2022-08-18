package me.m64diamondstar.ingeniamccore.General.Player;

import me.m64diamondstar.ingeniamccore.Utils.Colors;
import org.bukkit.entity.Player;

public class IngeniaPlayer {

    private final Player player;

    public IngeniaPlayer (Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void sendMessage(String string){
        player.sendMessage(Colors.format(string));
    }



}
