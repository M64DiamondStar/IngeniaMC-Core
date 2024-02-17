package me.m64diamondstar.ingeniamccore.utils.messages

import me.m64diamondstar.ingeniamccore.IngeniaMC
import net.kyori.adventure.text.Component

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

    fun featureDisabled(): String {
        return Colors.format(MessageType.ERROR + "This feature is currently disabled. Please notify a team member if you think this is an error.")
    }

    fun requiresVIP(): String {
        return Colors.format(MessageType.ERROR + "This feature requires VIP.")
    }

    fun ingeniaMCComponent(): Component{
        return IngeniaMC.miniMessage.deserialize("<#f4b734>IngeniaMC")
    }

}