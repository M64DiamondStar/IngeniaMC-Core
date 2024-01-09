package me.m64diamondstar.ingeniamccore.utils.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReceiveGoldenStarsEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    public final Player player;
    public final long amount;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ReceiveGoldenStarsEvent(Player player, long amount) {
        this.player = player;
        this.amount = amount;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
