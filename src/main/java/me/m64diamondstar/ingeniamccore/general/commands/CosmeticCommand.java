package me.m64diamondstar.ingeniamccore.general.commands;

import me.m64diamondstar.ingeniamccore.database.tables.cosmeticitems.Hats;
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer;
import me.m64diamondstar.ingeniamccore.utils.MessageType;
import me.m64diamondstar.ingeniamccore.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CosmeticCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {

        if(!(sender instanceof Player player)){
            sender.sendMessage(Messages.noPlayer());
            return false;
        }

        IngeniaPlayer ingeniaPlayer = new IngeniaPlayer(player);

        switch(args[0]){

            //Get sub-command
            //Get an item by giving a specific ID
            case "get":
                Hats hats = new Hats();

                if(args.length != 2){
                    ingeniaPlayer.sendMessage(Messages.commandUsage("cosmetic get <name>"));
                    return false;
                }

                if(!hats.exists(args[1])){
                    ingeniaPlayer.sendMessage("Invalid cosmetic name/id.", MessageType.ERROR);
                    return false;
                }

                player.getInventory().addItem(hats.getItem(args[1]));
                break;

            //Menu sub-command
            //Open the cosmetic menu with all cosmetics in it
            case "menu":

                break;

            //Add sub-command
            //Add cosmetics to the database
            case "add":
                addCosmetic(args, player, ingeniaPlayer);
                break;

            default:

        }

        return false;
    }



    private void addCosmetic(String[] args, Player player, IngeniaPlayer ingeniaPlayer){

    }
}
