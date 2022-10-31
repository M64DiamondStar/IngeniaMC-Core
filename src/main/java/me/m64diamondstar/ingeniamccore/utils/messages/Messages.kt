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
}