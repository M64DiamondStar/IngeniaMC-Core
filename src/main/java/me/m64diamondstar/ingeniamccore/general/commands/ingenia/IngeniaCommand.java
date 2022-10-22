package me.m64diamondstar.ingeniamccore.general.commands.ingenia;

import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsInventory;
import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory;
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer;
import me.m64diamondstar.ingeniamccore.utils.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IngeniaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0) {
            return false;
        }

        switch (args[0]) {

            //Exp sub-command
            //Get, set, subtract or add exp to a player account.
            case "exp" -> {
                ExpSubcommand exp = new ExpSubcommand(sender, args);
                exp.execute();
            }

            //Balance sub-command
            //Get, set, subtract or add golden stars to a player account.
            case "balance" -> {
                BalanceSubcommand bal = new BalanceSubcommand(sender, args);
                bal.execute();
            }

            //Scoreboard sub-command
            //Show or hide the board for specific players.
            case "scoreboard" -> {
                ScoreboardSubcommand scoreboard = new ScoreboardSubcommand(sender, args);
                scoreboard.execute();
            }

            case "menu" -> {

                if(!(sender instanceof Player player)){
                    sender.sendMessage(Messages.noPlayer());
                    return false;
                }

                MainInventory inv = new MainInventory(new IngeniaPlayer(player));
                inv.open();
            }

            default -> sender.sendMessage(Messages.invalidSubcommand("ig"));
        }

        return false;
    }

}
