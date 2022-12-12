package me.m64diamondstar.ingeniamccore.utils.messages

object Messages {
    @JvmStatic
    fun noPlayer(): String {
        return Colors.format(MessageType.ERROR + "You can only execute this command as a player!")
    }

    @JvmStatic
    fun commandUsage(command: String): String {
        return Colors.format(MessageType.ERROR + "Please use: '/$command'.")
    }

    fun invalidNumber(): String {
        return Colors.format(MessageType.ERROR + "Please use a valid number.")
    }

    @JvmStatic
    fun invalidSubcommand(command: String): String {
        return Colors.format(MessageType.ERROR + "Please use a valid argument. To get help, use: '/$command help'.")
    }

    fun invalidPlayer(): String {
        return Colors.format(MessageType.ERROR + "Please enter a valid player.")
    }

    fun invalidAttractionFunction(): String {
        return Colors.format(MessageType.ERROR + "This function is not available for this attraction type.")
    }

    fun currentRidecount(ridecount: Int): String {
        return Colors.format(MessageType.PLAYER_UPDATE + "Your ridecount for this ride is now at $ridecount.")
    }

    fun rideCountdown(currentCount: Int): String {
        return if(currentCount != 1)
            "This ride will start in $currentCount seconds."
        else
            "This ride will start in $currentCount second."
    }
}