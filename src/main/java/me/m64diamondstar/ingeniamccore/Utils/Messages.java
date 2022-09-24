package me.m64diamondstar.ingeniamccore.Utils;

public class Messages {

    public static String noPlayer(){
        return Colors.format("You can only execute this command as a player!", MessageType.ERROR);
    }

    public static String commandUsage(String command){
        return Colors.format("Please use: '/" + command + "'.", MessageType.ERROR);
    }

    public static String invalidNumber(){
        return Colors.format("Please use a valid number.", MessageType.ERROR);
    }

    public static String invalidSubcommand(String command){
        return Colors.format("Please use a valid argument. To get help, use: '/" + command + " help'.", MessageType.ERROR);
    }

    public static String invalidPlayer(){
        return Colors.format("Please enter a valid player.", MessageType.ERROR);
    }

}
