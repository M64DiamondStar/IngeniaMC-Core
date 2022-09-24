package me.m64diamondstar.ingeniamccore.General.Commands.Ingenia;

import me.m64diamondstar.ingeniamccore.General.Player.IngeniaPlayer;
import me.m64diamondstar.ingeniamccore.Utils.Colors;
import me.m64diamondstar.ingeniamccore.Utils.MessageType;
import me.m64diamondstar.ingeniamccore.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BalanceSubcommand {

    private CommandSender sender;
    private String[] args;

    public BalanceSubcommand(CommandSender sender, String[] args){
        this.sender = sender;
        this.args = args;
    }

    /**
     * Execute the command
     */
    public void execute(){
        if(args.length <= 2){
            sender.sendMessage(Colors.format(Messages.commandUsage("ig balance <add/set/subtract/get> <player> [amount]")));
        }

        else if(args.length == 3){

            if(args[1].equalsIgnoreCase("get")){
                IngeniaPlayer target = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
                if(target.getPlayer() == null){
                    sender.sendMessage(Messages.invalidPlayer());
                    return;
                }
                sender.sendMessage(Colors.format(target.getName() + " has " + target.getBal() + ":gs:.", MessageType.INFO));
            }else{
                sender.sendMessage(Colors.format(Messages.commandUsage("ig balance <add/set/subtract/get> <player> [amount]")));
            }

        }

        else if(args.length == 4){

            long gs;

            try{
                gs = Long.parseLong(args[3]);
            }catch (NumberFormatException e){
                sender.sendMessage(Messages.invalidNumber());
                return;
            }

            IngeniaPlayer target = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
            if(target.getPlayer() == null){
                sender.sendMessage(Messages.invalidPlayer());
                return;
            }

            if(args[1].equalsIgnoreCase("add")){
                target.addBal(gs);
                sender.sendMessage(Colors.format("Successfully added " + gs + ":gs: to " + target.getName() + ".", MessageType.SUCCESS));
            }else if(args[1].equalsIgnoreCase("set")){
                target.setBal(gs);
                sender.sendMessage(Colors.format("Successfully set " + target.getName() + "'s balance to " + gs + ":gs:.", MessageType.SUCCESS));
            }else if(args[1].equalsIgnoreCase("subtract")){
                target.addBal(-gs);
                sender.sendMessage(Colors.format("Successfully subtracted " + gs + ":gs: from " + target.getName() + ".", MessageType.SUCCESS));
            }else{
                sender.sendMessage(Colors.format(Messages.commandUsage("ig balance <add/set/subtract/get> <player> [amount]")));
            }

        }
    }

}
