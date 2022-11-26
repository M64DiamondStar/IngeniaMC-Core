package me.m64diamondstar.ingeniamccore.general.commands.ingenia;

import me.m64diamondstar.ingeniamccore.IngeniaMC;
import me.m64diamondstar.ingeniamccore.utils.messages.Colors;
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType;
import me.m64diamondstar.ingeniamccore.utils.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class IngeniaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0) {
            return false;
        }

        switch (args[0]) {

            // Exp sub-command
            // Get, set, subtract or add exp to a player account.
            case "exp" -> {
                ExpSubcommand exp = new ExpSubcommand(sender, args);
                exp.execute();
            }

            // Balance sub-command
            // Get, set, subtract or add golden stars to a player account.
            case "balance" -> {
                BalanceSubcommand bal = new BalanceSubcommand(sender, args);
                bal.execute();
            }

            // Scoreboard sub-command
            // Show or hide the board for specific players.
            case "scoreboard" -> {
                ScoreboardSubcommand scoreboard = new ScoreboardSubcommand(sender, args);
                scoreboard.execute();
            }

            // Menu sub-command
            // Open the main menu or give the item
            case "menu" -> {
                MenuSubcommand menuSubcommand = new MenuSubcommand(sender, args);
                menuSubcommand.execute();
            }

            // Attraction sub-command
            // Edit attractions
            case "attraction" -> {
                AttractionSubcommand attractionSubcommand = new AttractionSubcommand(sender, args);
                attractionSubcommand.execute();
            }

            // Show sub-command
            // Edit shows
            case "show" -> {
                ShowSubcommand showSubcommand = new ShowSubcommand(sender, args);
                showSubcommand.execute();
            }

            // Game sub-command
            // Edit or start custom games
            case "game" -> {
                GameSubcommand gameSubcommand = new GameSubcommand(sender, args);
                gameSubcommand.execute();
            }

            // Reload sub-command
            // Reload the main config.yml file
            case "reload" -> {
                IngeniaMC.plugin.reloadConfig();
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully reloaded config.yml!"));
            }

            case "protect" -> {
                ProtectionSubcommand protectionSubcommand = new ProtectionSubcommand(sender, args);
                protectionSubcommand.execute();
            }


            /*case "backpack" -> {

                if(!(sender instanceof Player player)){
                    sender.sendMessage(Messages.noPlayer());
                    return false;
                }

                ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                stand.setMarker(true);
                stand.setBasePlate(false);
                stand.setInvisible(true);

                new BukkitRunnable(){

                    @Override
                    public void run(){
                        stand.setRotation(player.getLocation().getYaw(), 0);

                    }

                }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

                player.addPassenger(stand);

            }*/

            default -> sender.sendMessage(Messages.invalidSubcommand("ig"));
        }

        return false;
    }

}
