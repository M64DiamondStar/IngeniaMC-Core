package me.m64diamondstar.ingeniamccore.general.commands;

import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer;
import me.m64diamondstar.ingeniamccore.utils.MessageType;
import me.m64diamondstar.ingeniamccore.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GamemodeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(Messages.noPlayer());
            return false;
        }

        IngeniaPlayer player = new IngeniaPlayer((Player) sender);

        switch (label) {
            case "gmc" -> setGameMode(GameMode.CREATIVE, player, args);
            case "gms" -> setGameMode(GameMode.SURVIVAL, player, args);
            case "gma" -> setGameMode(GameMode.ADVENTURE, player, args);
            case "gmsp" -> setGameMode(GameMode.SPECTATOR, player, args);
        }

        return false;
    }


    private void setGameMode(GameMode gameMode, IngeniaPlayer player, String[] args){

        if(args.length == 0){
            player.setGameMode(gameMode);
        }

        else if(args.length == 1){

            if(Bukkit.getPlayer(args[0]) == null){
                player.sendMessage("This player is not online!", MessageType.ERROR);
                return;
            }

            IngeniaPlayer target = new IngeniaPlayer(Bukkit.getPlayer(args[0]));
            target.setGameMode(gameMode);
            player.sendMessage("Successfully changed " + args[0] + "'s gamemode to " + gameMode.toString().toLowerCase(), MessageType.SUCCESS);

        }

        else{
            player.sendMessage("Please use '/gm(c/a/s/sp) [player]'", MessageType.ERROR);
        }
    }
}
