package me.m64diamondstar.ingeniamccore.general.commands

import me.m64diamondstar.ingeniamccore.utils.messages.Messages.noPlayer
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class GamemodeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage(noPlayer())
            return false
        }

        val player = IngeniaPlayer(sender)

        if (label.equals("gmc", ignoreCase = true)) {
            setGameMode(GameMode.CREATIVE, player, args)
        }
        if (label.equals("gms", ignoreCase = true)) {
            setGameMode(GameMode.SURVIVAL, player, args)
        }
        if (label.equals("gma", ignoreCase = true)) {
            setGameMode(GameMode.ADVENTURE, player, args)
        }
        if (label.equals("gmsp", ignoreCase = true)) {
            setGameMode(GameMode.SPECTATOR, player, args)
        }

        return false
    }

    private fun setGameMode(gameMode: GameMode, player: IngeniaPlayer, args: Array<String>) {
        if (args.isEmpty()) {
            player.setGameMode(gameMode)
        } else if (args.size == 1) {
            val target = IngeniaPlayer(
                Objects.requireNonNull(
                    Bukkit.getPlayer(
                        args[0]
                    )
                )
            )
            if (target.player == null) {
                player.sendMessage("This player is not online!", MessageType.ERROR)
                return
            }
            target.setGameMode(gameMode)
            player.sendMessage(
                "Successfully changed " + args[0] + "'s gamemode to " + gameMode.toString()
                    .lowercase(Locale.getDefault()), MessageType.SUCCESS
            )
        } else {
            player.sendMessage("Please use '/gm(c/a/s/sp) [player]'", MessageType.ERROR)
        }
    }
}