package me.m64diamondstar.ingeniamccore.General.Commands.Ingenia;

import me.m64diamondstar.ingeniamccore.General.Player.IngeniaPlayer;
import me.m64diamondstar.ingeniamccore.Utils.Colors;
import me.m64diamondstar.ingeniamccore.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardSubcommand {

    private CommandSender sender;
    private String[] args;

    public ScoreboardSubcommand(CommandSender sender, String[] args){
        this.sender = sender;
        this.args = args;
    }

    /**
     * Execute the command
     */
    public void execute(){

        if(args.length != 2 && args.length != 3) {
            sender.sendMessage(Colors.format(Messages.commandUsage("ig scoreboard <show/hide> [player]")));
            return;
        }

        if(args[1].equalsIgnoreCase("show") || args[1].equalsIgnoreCase("true")){

            IngeniaPlayer player;
            if(args.length == 3){
                player = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
            }else {
                player = new IngeniaPlayer((Player) sender);
            }
            player.setScoreboard(true);

        }else if(args[1].equalsIgnoreCase("hide") || args[1].equalsIgnoreCase("false")){
            IngeniaPlayer player;
            if(args.length == 3){
                player = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
                if(player.getPlayer() == null){
                    sender.sendMessage(Messages.invalidPlayer());
                    return;
                }
            }else {
                player = new IngeniaPlayer((Player) sender);
            }
            player.setScoreboard(false);
        }else{
            sender.sendMessage(Colors.format(Messages.commandUsage("ig scoreboard <show/hide> [player]")));
        }
    }

}
