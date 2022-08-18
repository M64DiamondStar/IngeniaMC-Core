package me.m64diamondstar.ingeniamccore.General.Player;

import me.m64diamondstar.ingeniamccore.Utils.Colors;
import me.m64diamondstar.ingeniamccore.Utils.MessageLocation;
import me.m64diamondstar.ingeniamccore.Utils.MessageType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static me.m64diamondstar.ingeniamccore.Utils.MessageLocation.CHAT;

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

    public void sendMessage(String string, MessageType messageType){
        player.sendMessage(Colors.format(string, messageType));
    }

    public void sendMessage(String string, MessageLocation messageLocation){
        switch (messageLocation){
            case CHAT:
                player.sendMessage(Colors.format(string));
                break;
            case HOTBAR:
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Colors.format(string)));
                break;
            case TITLE:
                player.sendTitle(Colors.format(string), "", 10, 50, 10);
                break;
            case SUBTITLE:
                player.sendTitle("", Colors.format(string), 10, 50, 10);
                break;

        }
    }



}
