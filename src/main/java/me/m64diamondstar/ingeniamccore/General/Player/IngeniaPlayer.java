package me.m64diamondstar.ingeniamccore.General.Player;

import me.m64diamondstar.ingeniamccore.Database.Tables.Player.Exp;
import me.m64diamondstar.ingeniamccore.Database.Tables.Player.GoldenStars;
import me.m64diamondstar.ingeniamccore.General.Scoreboard.Scoreboard;
import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Utils.Colors;
import me.m64diamondstar.ingeniamccore.Utils.MessageLocation;
import me.m64diamondstar.ingeniamccore.Utils.MessageType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class IngeniaPlayer {

    private final Player player;
    private Scoreboard scoreboard;

    public IngeniaPlayer (Player player){
        this.player = player;
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

    public long getExp(){
        Exp exp = new Exp();
        exp.createPlayer(player);
        return exp.getExp(player);
    }

    public void setExp(long l){
        Exp exp = new Exp();
        exp.createPlayer(player);
        exp.setExp(player, l);
    }

    public void addExp(long l){
        Exp exp = new Exp();
        exp.createPlayer(player);
        exp.addExp(player, l);
    }

    public long getBal(){
        GoldenStars gs = new GoldenStars();
        gs.createPlayer(player);
        return gs.getBal(player);
    }

    public void setBal(long l){
        GoldenStars gs = new GoldenStars();
        gs.createPlayer(player);
        gs.setBal(player, l);
    }

    public void addBal(long l){
        GoldenStars gs = new GoldenStars();
        gs.createPlayer(player);
        gs.addBal(player, l);
    }

    public void showBoard(){
        scoreboard = new Scoreboard(this);
        scoreboard.createBoard();
        scoreboard.startUpdating();
        scoreboard.showBoard();
    }

    public void hideBoard(){
        scoreboard.hideBoard();
    }



}
