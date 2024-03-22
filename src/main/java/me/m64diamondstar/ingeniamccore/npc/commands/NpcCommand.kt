package me.m64diamondstar.ingeniamccore.npc.commands

import com.ticxo.modelengine.api.ModelEngineAPI
import me.m64diamondstar.ingeniamccore.npc.Npc
import me.m64diamondstar.ingeniamccore.npc.utils.DialogueBackdropType
import me.m64diamondstar.ingeniamccore.npc.utils.NpcRegistry
import me.m64diamondstar.ingeniamccore.npc.utils.NpcUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class NpcCommand: TabExecutor {
    override fun onTabComplete(sender: CommandSender, command: Command, string: String, args: Array<out String>): List<String> {
        val tc = ArrayList<String>()

        if(args.size == 1){
            tc.addAll(listOf("create", "delete", "setlocation", "setmodel", "spawn", "despawn", "respawn", "reload", "dialogue"))
        }
        else if(args.size == 2 && !args[0].equals("create", ignoreCase = true)){
            tc.addAll(NpcUtils.getNPCFiles().map { it.name.replace(".yml", "") })
        }
        else if(args.size == 3){
            if(args[0].equals("setmodel", ignoreCase = true)){
                tc.addAll(ModelEngineAPI.getAPI().modelRegistry.keys.toList())
            }
            else if(args[0].equals("setdialoguebackdrop", ignoreCase = true)){
                tc.addAll(DialogueBackdropType.values().map { it.name })
            }
            else if(args[0].equals("dialogue", ignoreCase = true)){
                tc.addAll(listOf("set", "remove", "backdrop", "backdropcolor"))
            }
        }
        else if(args.size == 4){
            if(args[0].equals("dialogue", ignoreCase = true)){
                if(args[2].equals("backdrop", ignoreCase = true)) {
                    tc.addAll(DialogueBackdropType.values().map { it.name })
                }
                else if(args[2].equals("backdropcolor", ignoreCase = true)) {
                    tc.addAll(listOf("#ffffff", "#000000"))
                }
                else if(args[2].equals("set", ignoreCase = true) || args[2].equals("remove", ignoreCase = true)) {
                    if(NpcRegistry.getNpc(args[1]) != null){
                        tc.addAll(NpcRegistry.getNpc(args[1])!!.getData().getBranches())
                    }
                }
            }
        }
        else if(args.size == 5){
            if(args[0].equals("dialogue", ignoreCase = true)){
                if(args[2].equals("set", ignoreCase = true) || args[2].equals("remove", ignoreCase = true)) {
                    if(NpcRegistry.getNpc(args[1]) != null){
                        tc.addAll(NpcRegistry.getNpc(args[1])!!.getData().getIndexes(args[3]).map { it.toString() })
                    }
                }
            }
        }

        val result = ArrayList<String>()
        for(a in tc){
            if(a.lowercase().startsWith(args[args.size - 1].lowercase()))
                result.add(a)
        }

        return result
    }

    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        when(args[0].lowercase()){
            "create" -> {
                if(args.size == 2){
                    if(NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc already exists. Please delete it first if you want to re-create it."))
                        return false
                    }

                    val npc = Npc(args[1]).init()

                    if(sender is Player){
                        npc.getData().setLocation(sender.location)
                    }
                }
            }

            "delete" -> {
                if(args.size == 2){
                    if(!NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                        return false
                    }

                    val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1])
                    npc.delete()
                }
            }

            "setlocation" -> {
                if(args.size == 2){
                    if(!NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                        return false
                    }

                    if(sender is Player) {
                        val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                        npc.getData().setLocation(sender.location)
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc location has been set."))
                        npc.respawn()
                    }else
                        sender.sendMessage(Colors.format(MessageType.ERROR + "You must be a player to execute this command."))
                }
            }

            "setmodel" -> {
                if(args.size == 3){
                    if(!NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                        return false
                    }

                    val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                    npc.getData().setModel(args[2])
                    npc.respawn()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc model has been set."))
                }
            }

            "respawn" -> {
                if(args.size == 2){
                    if(!NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                        return false
                    }

                    val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                    npc.respawn()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc has been respawned."))
                }
            }

            "despawn" -> {
                if(args.size == 2){
                    if(!NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                        return false
                    }

                    val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                    npc.despawn()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc has been despawned."))
                }
            }

            "spawn" -> {
                if(args.size == 2){
                    if(!NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                        return false
                    }

                    val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                    npc.spawn()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc has been spawned."))
                }
            }

            "reload" -> {
                if(args.size == 2){
                    if(!NpcUtils.existsNpc(args[1])){
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                        return false
                    }

                    val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                    npc.getData().reload()
                    npc.respawn()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc has been reloaded."))
                }
            }

            "dialogue" -> {
                if(args.size < 3){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "Not enough arguments."))
                    return false
                }

                if(!NpcUtils.existsNpc(args[1])){
                    sender.sendMessage(Colors.format(MessageType.ERROR + "This npc does not exist."))
                    return false
                }

                when(args[2].lowercase()){
                    "set" -> { // /npc dialogue <npc> set <branch> <index> <message...>
                        if(args.size < 6){
                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set <branch> <index> <message...>"))
                            sender.sendMessage(Colors.format(MessageType.ERROR + "Use '\\n' for a new line."))
                            return false
                        }

                        val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                        npc.getData().setDialogue(args[3], args[4].toInt(), listOf(*args.copyOfRange(5, args.size)).joinToString(" "))
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue has been changed."))
                    }

                    "remove" -> { // /npc dialogue <npc> remove <branch> <index>
                        if(args.size != 5){
                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> remove <branch> <index>"))
                            return false
                        }

                        val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                        npc.getData().setDialogue(args[3], args[4].toInt(), null)
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue has been deleted."))
                    }

                    "backdrop" -> { // /npc dialogue <npc> backdrop <backdrop type>
                        if (args.size != 4) {
                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> backdrop <backdrop>"))
                            return false
                        }

                        if(!DialogueBackdropType.values().map { it.name }.contains(args[3].uppercase())){
                            sender.sendMessage(Colors.format(MessageType.ERROR + "This backdrop does not exist."))
                            return false
                        }

                        val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                        npc.getData().setDialogueBackdrop(DialogueBackdropType.valueOf(args[3].uppercase()))
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue backdrop has been set."))
                    }

                    "backdropcolor" -> { // /npc dialogue <npc> backdropcolor <HEX color>
                        if (args.size != 4) {
                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> backdropcolor <color>"))
                            return false
                        }

                        if(TextColor.fromHexString(args[3]) == null){
                            sender.sendMessage(Colors.format(MessageType.ERROR + "This color does not exist. Please enter a HEX string like #ffffff."))
                            return false
                        }

                        val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()
                        npc.getData().setDialogueBackdropColor(TextColor.fromHexString(args[3])!!)
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue color has been set."))
                    }
                }

            }
        }

        return false
    }
}