package me.m64diamondstar.ingeniamccore.general.commands.ingenia;

import me.m64diamondstar.ingeniamccore.cosmetics.CosmeticsInventory;
import me.m64diamondstar.ingeniamccore.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IngeniaCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


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
                CosmeticsInventory inventory = new CosmeticsInventory((Player) sender);
                inventory.openInventory();
            }

            default -> sender.sendMessage(Messages.invalidSubcommand("ig"));
        }


        return false;
    }

}
