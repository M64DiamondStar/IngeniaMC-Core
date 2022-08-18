package me.m64diamondstar.ingeniamccore.Wands;

import org.bukkit.entity.Player;

public class WandPlayer {

    private Player player;
    private long cooldown;
    private boolean flying;

    public WandPlayer(Player player){
        this.player = player;
    }

    public void setCooldown(Long op, Long vipp, Long vip, Long visitor){
        if(player.hasPermission("ingeniawands.owner") || player.isOp()) {
            cooldown = System.currentTimeMillis() + (op);
        } else if(player.hasPermission("ingenia.vip+")) {
            cooldown = System.currentTimeMillis() + (vipp);
        } else if(player.hasPermission("ingenia.vip")) {
            cooldown = System.currentTimeMillis() + (vip);
        } else {
            cooldown = System.currentTimeMillis() + (visitor);
        }
    }

    public long getCooldown(){
        return cooldown;
    }

    public void setFlying(){
        this.flying = true;
    }

    public void setFlying(boolean flying){
        this.flying = flying;
    }



}
