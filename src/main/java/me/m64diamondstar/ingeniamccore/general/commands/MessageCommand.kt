package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MessageCommand: CommandExecutor {

    companion object {
        private var msgplayers: HashMap<Player, Player> = HashMap()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false

        if ((label.equals("msg", ignoreCase = true) || label.equals("tell", ignoreCase = true)) && args!!.size >= 2) {
            val sb = StringBuilder()

            for (LoopArgs in 1 until args.size) {
                sb.append(args[LoopArgs]).append(" ")
            }

            val target = Bukkit.getPlayer(args[0])
            if (target == null) {
                sender.sendMessage(Colors.format(MessageType.ERROR + "This player is not online!"))
                return true
            }
            if (sender.uniqueId === target.uniqueId) {
                sender.sendMessage(Colors.format(MessageType.ERROR + "You can't message yourself!"))
                return false
            }

            target.sendMessage(Colors.format("#ffcb52" + sender.name + " » You: " + sb))
            sender.sendMessage(Colors.format("#ffcb52You » " + target.name + ": " + sb))

            msgplayers.remove(sender)
            msgplayers[sender] = target
            msgplayers[target] = sender
        } else if (label.equals("msg", ignoreCase = true) && args!!.size < 2) {
            sender.sendMessage(Messages.commandUsage("msg <player> <message>"))
        }



        if ((label.equals("r", ignoreCase = true) || label.equals("react", ignoreCase = true)) && args!!.isNotEmpty()) {

            val sb = StringBuilder()

            for (LoopArgs in args.indices) {
                sb.append(args[LoopArgs]).append(" ")
            }

            val target: Player? = msgplayers[sender]

            if (target == null) {
                sender.sendMessage(Colors.format(MessageType.ERROR + "You have no one to react to."))
                return true
            }

            msgplayers.remove(target)
            msgplayers[target] = sender
            target.sendMessage(Colors.format("#ffcb52" + sender.name + " » You: " + sb))
            sender.sendMessage(Colors.format("#ffcb52You » " + target.name + ": " + sb))
        } else if (label.equals("r", ignoreCase = true) && args!!.isEmpty()) {
            sender.sendMessage(Messages.commandUsage("r <message>"))
        }

        return false
    }

}