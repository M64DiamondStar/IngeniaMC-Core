package me.m64diamondstar.ingeniamccore.General.Commands.Ingenia;

import me.m64diamondstar.ingeniamccore.Utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

            default -> sender.sendMessage(Messages.invalidSubcommand("ig"));
        }


        return false;
    }

}
