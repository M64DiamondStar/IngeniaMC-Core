package me.m64diamondstar.ingeniamccore.general.player;

import me.m64diamondstar.ingeniamccore.data.files.PlayerConfig;
import me.m64diamondstar.ingeniamccore.general.scoreboard.Scoreboard;
import me.m64diamondstar.ingeniamccore.utils.messages.Colors;
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation;
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType;
import me.m64diamondstar.ingeniamccore.wands.Wands;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class IngeniaPlayer {

    private final Player player;
    private Scoreboard scoreboard;
    private PlayerConfig config;

    public IngeniaPlayer (Player player){
        this.player = player;
        this.config = new PlayerConfig(player.getUniqueId());
    }

    public void startUp(){
        player.setCollidable(false);
    }

    public Player getPlayer(){
        return this.player;
    }

    public String getName(){
        return player.getName();
    }

    public void sendMessage(String string){
        player.sendMessage(Colors.format(string));
    }

    public void sendMessage(String string, MessageType messageType){
        player.sendMessage(Colors.format(string, messageType));
    }

    public void sendMessage(String string, MessageLocation messageLocation){
        switch (messageLocation) {
            case CHAT -> player.sendMessage(Colors.format(string));
            case HOTBAR -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Colors.format(string)));
            case TITLE -> player.sendTitle(Colors.format(string), "", 10, 50, 10);
            case SUBTITLE -> player.sendTitle("", Colors.format(string), 10, 50, 10);
        }
    }

    public void setGameMode(GameMode gameMode){
        player.setGameMode(gameMode);
        this.sendMessage("Your gamemode has changed to: " + gameMode.toString().toLowerCase(), MessageType.BACKGROUND);
    }

    public void sendMessage(String s, String color) {
        this.sendMessage(Colors.format(color + s));
    }

    public long getExp(){
        this.config = new PlayerConfig(player.getUniqueId());
        return config.getExp();
    }

    public void setExp(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setExp(l);
    }

    public void addExp(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setExp(l + getExp());
    }

    public long getBal(){
        this.config = new PlayerConfig(player.getUniqueId());
        return config.getBal();
    }

    public void setBal(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setBal(l);
    }

    public void addBal(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setBal(l + getBal());
    }

    public void setScoreboard(boolean on){
        if(scoreboard == null)
            scoreboard = new Scoreboard(this);

        if(on) {
            scoreboard.createBoard();
            scoreboard.startUpdating();
            scoreboard.showBoard();
        }else{
            scoreboard.hideBoard();
        }
    }

    public List<ItemStack> getWands(){
        return Wands.getAccessibleWands(player);
    }

    public void setWand(ItemStack item){
        player.getInventory().setItem(5, item);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 1.5F);
    }

}
