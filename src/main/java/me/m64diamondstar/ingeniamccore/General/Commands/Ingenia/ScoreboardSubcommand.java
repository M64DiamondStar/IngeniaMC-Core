package me.m64diamondstar.ingeniamccore.General.Commands.Ingenia;

import me.m64diamondstar.ingeniamccore.General.Player.IngeniaPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardSubcommand {

    private CommandSender sender;
    private String[] args;

    public ScoreboardSubcommand(CommandSender sender, String[] args){
        this.sender = sender;
        this.args = args;
    }

    public void execute(){
        if(args[1].equalsIgnoreCase("show")){
            IngeniaPlayer player = new IngeniaPlayer((Player) sender);
            player.showBoard();
        }
    }

}
