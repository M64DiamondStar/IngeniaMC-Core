package me.m64diamondstar.ingeniamccore.utils.event.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReceiveRidecountEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    public final OfflinePlayer player;
    public final int amount;
    public final String attractionCategory;
    public final String attractionName;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ReceiveRidecountEvent(OfflinePlayer player, int amount, String attractionCategory, String attractionName) {
        this.player = player;
        this.amount = amount;
        this.attractionCategory = attractionCategory;
        this.attractionName = attractionName;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
