package me.m64diamondstar.ingeniamccore.utils.event.player;

import me.m64diamondstar.ingeniamccore.general.areas.Area;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SwitchAreaEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    public final Player player;
    public final Area toArea;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public SwitchAreaEvent(Player player, Area toArea) {
        this.player = player;
        this.toArea = toArea;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
