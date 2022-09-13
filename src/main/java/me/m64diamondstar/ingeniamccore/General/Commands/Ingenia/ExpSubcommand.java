package me.m64diamondstar.ingeniamccore.General.Commands.Ingenia;

import me.m64diamondstar.ingeniamccore.General.Player.IngeniaPlayer;
import me.m64diamondstar.ingeniamccore.Utils.Colors;
import me.m64diamondstar.ingeniamccore.Utils.MessageType;
import me.m64diamondstar.ingeniamccore.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ExpSubcommand {

    private CommandSender sender;
    private String[] args;

    public ExpSubcommand(CommandSender sender, String[] args){
        this.sender = sender;
        this.args = args;
    }



    public void execute(){
        if(args.length <= 2){
            sender.sendMessage(Colors.format(Messages.commandUsage("ig exp <add/set/subtract/get> <player> [amount]")));
        }

        else if(args.length == 3){

            if(args[1].equalsIgnoreCase("get")){
                IngeniaPlayer target = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
                sender.sendMessage(Colors.format(target.getName() + " has " + target.getExp() + " exp.", MessageType.INFO));
            }else{
                sender.sendMessage(Colors.format(Messages.commandUsage("ig exp <add/set/subtract/get> <player> [amount]")));
            }

        }

        else if(args.length == 4){

            long exp;

            try{
                exp = Long.parseLong(args[3]);
            }catch (NumberFormatException e){
                sender.sendMessage(Messages.invalidNumber());
                return;
            }

            if(args[1].equalsIgnoreCase("add")){
                IngeniaPlayer target = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
                target.addExp(exp);
                sender.sendMessage(Colors.format("Successfully added " + exp + " to " + target.getName() + ".", MessageType.SUCCESS));
            }else if(args[1].equalsIgnoreCase("set")){
                IngeniaPlayer target = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
                target.setExp(exp);
                sender.sendMessage(Colors.format("Successfully set " + target.getName() + "'s exp to " + exp + ".", MessageType.SUCCESS));
            }else if(args[1].equalsIgnoreCase("subtract")){
                IngeniaPlayer target = new IngeniaPlayer(Bukkit.getPlayer(args[2]));
                target.addExp(-exp);
                sender.sendMessage(Colors.format("Successfully subtracted " + exp + " exp from " + target.getName() + ".", MessageType.SUCCESS));
            }else{
                sender.sendMessage(Colors.format(Messages.commandUsage("ig exp <add/set/subtract/get> <player> [amount]")));
            }

        }
    }

}
