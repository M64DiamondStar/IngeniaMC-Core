package me.m64diamondstar.ingeniamccore.general.commands

import com.ticxo.modelengine.api.ModelEngineAPI
import me.m64diamondstar.ingeniamccore.npc.Npc
import me.m64diamondstar.ingeniamccore.npc.utils.*
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.util.UUID

class NpcCommand: TabExecutor {

    private val playerLocations = HashMap<UUID, Pair<Location?, Location?>>()

    override fun onTabComplete(sender: CommandSender, command: Command, string: String, args: Array<out String>): List<String> {
        val tc = ArrayList<String>()

        if(args.size == 1){
            tc.addAll(listOf("create", "delete", "setlocation", "setmodel", "spawn", "despawn", "respawn", "reload", "dialogue", "pos1", "pos2"))
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
                    tc.addAll(listOf("text", "option", "action"))
                }
            }
        }

        else if(args.size == 5){
            if(args[0].equals("dialogue", ignoreCase = true)){
                if(args[2].equals("set", ignoreCase = true) || args[2].equals("remove", ignoreCase = true)) {
                    if(NpcRegistry.getNpc(args[1]) != null){
                        tc.addAll(NpcRegistry.getNpc(args[1])!!.getData().getBranches())
                    }
                }
            }
        }

        else if(args.size == 6){
            if(args[0].equals("dialogue", ignoreCase = true)){
                if(args[2].equals("set", ignoreCase = true) || args[2].equals("remove", ignoreCase = true)) {
                    if(NpcRegistry.getNpc(args[1]) != null){
                        tc.addAll(NpcRegistry.getNpc(args[1])!!.getData().getIndexes(args[4]).map { it.toString() })
                    }
                }
            }
        }

        else if(args.size == 7){
            if(args[0].equals("dialogue", ignoreCase = true)){
                if(args[2].equals("set", ignoreCase = true)) {
                    if(NpcRegistry.getNpc(args[1]) != null){
                        if(args[3].equals("action", ignoreCase = true)){
                            tc.addAll(DialogueAction.values().map { it.name })
                        }else if(args[3].equals("option", ignoreCase = true)){
                            tc.addAll(listOf("setdisplay", "settype", "setdata"))
                        }
                    }
                }
                else if(args[2].equals("remove", ignoreCase = true)) {
                    if(NpcRegistry.getNpc(args[1]) != null){
                        if(args[3].equals("option", ignoreCase = true)){
                            if(args[5].toIntOrNull() != null)
                                tc.addAll(NpcRegistry.getNpc(args[1])!!.getData().getOptionIndexes(args[4], args[5].toInt()).map { it.toString() })
                        }
                    }
                }
            }
        }

        else if(args.size == 8) {
            if (args[0].equals("dialogue", ignoreCase = true)) {
                if (args[2].equals("set", ignoreCase = true) || args[2].equals("remove", ignoreCase = true)) {
                    if (NpcRegistry.getNpc(args[1]) != null) {
                        if (args[3].equals("option", ignoreCase = true)) {
                            if(args[5].toIntOrNull() != null)
                                tc.addAll(NpcRegistry.getNpc(args[1])!!.getData().getOptionIndexes(args[4], args[5].toInt()).map { it.toString() })
                        }
                    }
                }
            }
        }

        else if(args.size == 9) {
            if (args[0].equals("dialogue", ignoreCase = true)) {
                if (args[2].equals("set", ignoreCase = true) || args[2].equals("remove", ignoreCase = true)) {
                    if (NpcRegistry.getNpc(args[1]) != null) {
                        if(args[6].equals("settype", ignoreCase = true)) {
                            if (args[3].equals("option", ignoreCase = true)) {
                                tc.addAll(DialogueOptionType.values().map { it.name })
                            }
                        }
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
                        if(args.size < 7){
                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set <text/action/option> <branch> <index> <args...>"))
                            sender.sendMessage(Colors.format(MessageType.ERROR + "Use '\\n' for a new line."))
                            return false
                        }

                        val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()

                        when(args[3]){

                            "text" -> {
                                if(args[5].toIntOrNull() == null){
                                    sender.sendMessage(Messages.invalidNumber())
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set text <branch> <index> <text>"))
                                    sender.sendMessage(Colors.format(MessageType.ERROR + "Use '\\n' for a new line."))
                                    return false
                                }
                                npc.getData().setDialogue(args[4], args[5].toInt(), listOf(*args.copyOfRange(6, args.size)).joinToString(" "))
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue has been changed."))
                            }

                            "action" -> {
                                if(args.size < 8){
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set action <branch> <index> <action type> <args...>"))
                                    sender.sendMessage(Colors.format(MessageType.ERROR + "Use '\\n' for a new line."))
                                    return false
                                }
                                if(!DialogueAction.values().map { it.name }.contains(args[6].uppercase())){
                                    sender.sendMessage(Colors.format(MessageType.ERROR + "The action does not exist."))
                                    return false
                                }
                                if(args[5].toIntOrNull() == null){
                                    sender.sendMessage(Messages.invalidNumber())
                                    return false
                                }
                                npc.getData().setActionType(args[4], args[5].toInt(), DialogueAction.valueOf(args[6].uppercase()))
                                npc.getData().setActionData(args[4], args[5].toInt(), listOf(*args.copyOfRange(7, args.size)).joinToString(" "))
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue has been changed."))
                            }

                            "option" -> {
                                if(args.size < 8){
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> <setdisplay/settype/setdata> <args...>"))
                                    return false
                                }
                                when(args[6].lowercase()){
                                    "setdisplay" -> {
                                        if(args.size < 9){
                                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> setdisplay <option index> <display...>"))
                                            return false
                                        }
                                        if(args[5].toIntOrNull() == null || args[7].toIntOrNull() == null){
                                            sender.sendMessage(Messages.invalidNumber())
                                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> setdisplay <option index> <display...>"))
                                            return false
                                        }
                                        npc.getData().setOptionDisplay(args[4], args[5].toInt(), args[7].toInt(), listOf(*args.copyOfRange(8, args.size)).joinToString(" "))
                                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The display has been set to '" + listOf(*args.copyOfRange(8, args.size)).joinToString(" ") + "'."))
                                    }
                                    "settype" -> {
                                        if(args.size < 9){
                                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> settype <option index> <type>"))
                                            return false
                                        }
                                        if(!DialogueOptionType.values().map { it.name }.contains(args[8].uppercase())){
                                            sender.sendMessage(Colors.format(MessageType.ERROR + "The action does not exist."))
                                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> settype <option index> <type>"))
                                            return false
                                        }
                                        if(args[5].toIntOrNull() == null || args[7].toIntOrNull() == null){
                                            sender.sendMessage(Messages.invalidNumber())
                                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> settype <option index> <type>"))
                                            return false
                                        }
                                        npc.getData().setOptionType(args[4], args[5].toInt(), args[7].toInt(), DialogueOptionType.valueOf(args[8].uppercase()))
                                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The option type has been set to " + args[8].uppercase() + "."))
                                    }
                                    "setdata" -> {
                                        if(args.size < 9){
                                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> setdata <option index> <data...>"))
                                            return false
                                        }
                                        if(args[5].toIntOrNull() == null || args[7].toIntOrNull() == null){
                                            sender.sendMessage(Messages.invalidNumber())
                                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set option <branch> <index> setdata <option index> <data...>"))
                                            return false
                                        }
                                        npc.getData().setOptionData(args[4], args[5].toInt(), args[7].toInt(), listOf(*args.copyOfRange(8, args.size)).joinToString(" "))
                                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The option data has been set to " + listOf(*args.copyOfRange(8, args.size)).joinToString(" ") + "."))
                                    }
                                }
                            }
                        }
                    }

                    "remove" -> { // /npc dialogue <npc> remove <branch> <index>
                        if(args.size < 6){
                            sender.sendMessage(Messages.commandUsage("npc dialogue <npc> set <text/action/option> <branch> <index> <args...>"))
                            sender.sendMessage(Colors.format(MessageType.ERROR + "Use '\\n' for a new line."))
                            return false
                        }

                        val npc = NpcRegistry.getNpc(args[1]) ?: Npc(args[1]).init()

                        when(args[3]){
                            "text" -> {
                                if(args.size != 6){
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> remove text <branch> <index>"))
                                    return false
                                }
                                if(args[5].toIntOrNull() == null){
                                    sender.sendMessage(Messages.invalidNumber())
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> remove text <branch> <index>"))
                                    return false
                                }

                                npc.getData().setDialogue(args[4], args[5].toInt(), null)
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue has been deleted."))
                            }
                            "action" -> {
                                if(args.size != 6){
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> remove action <branch> <index>"))
                                    return false
                                }
                                if(args[5].toIntOrNull() == null){
                                    sender.sendMessage(Messages.invalidNumber())
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> remove action <branch> <index>"))
                                    return false
                                }
                                npc.getData().removeAction(args[4], args[5].toInt())
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue action has been deleted."))
                            }
                            "option" -> {
                                if(args.size != 7){
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> remove option <branch> <index> <option index>"))
                                }

                                if(args[5].toIntOrNull() == null || args[6].toIntOrNull() == null){
                                    sender.sendMessage(Messages.invalidNumber())
                                    sender.sendMessage(Messages.commandUsage("npc dialogue <npc> remove option <branch> <index> <option index>"))
                                    return false
                                }
                                npc.getData().removeOption(args[4], args[5].toInt(), args[6].toInt())
                                sender.sendMessage(Colors.format(MessageType.SUCCESS + "The npc dialogue option has been deleted."))
                            }
                        }
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

            "pos1" -> {
                if(sender !is Player){
                    sender.sendMessage(Messages.noPlayer())
                    return false
                }

                playerLocations[sender.uniqueId] = Pair(sender.location, playerLocations[sender.uniqueId]?.second)
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Set first position to " +
                        "${LocationUtils.getStringFromLocation(sender.location)}."))
                if(playerLocations[sender.uniqueId]!!.second != null){
                    val firstLocation = LocationUtils.getStringFromLocation(playerLocations[sender.uniqueId]!!.first!!)
                    val secondLocation = LocationUtils.getStringFromLocation(playerLocations[sender.uniqueId]!!.second!!)
                    (sender as Audience).sendMessage(MiniMessage.miniMessage().deserialize(
                        "<click:copy_to_clipboard:$firstLocation|$secondLocation|10>" +
                                "<${MessageType.INGENIA}>Click to copy cinematic data.")
                    )
                }
            }

            "pos2" -> {
                if(sender !is Player){
                    sender.sendMessage(Messages.noPlayer())
                    return false
                }

                playerLocations[sender.uniqueId] = Pair(playerLocations[sender.uniqueId]?.first, sender.location)
                sender.sendMessage(Colors.format(MessageType.SUCCESS + "Set second position to " +
                        "${LocationUtils.getStringFromLocation(sender.location)}."))
                if(playerLocations[sender.uniqueId]!!.first != null){
                    val firstLocation = LocationUtils.getStringFromLocation(playerLocations[sender.uniqueId]!!.first!!)
                    val secondLocation = LocationUtils.getStringFromLocation(playerLocations[sender.uniqueId]!!.second!!)
                    (sender as Audience).sendMessage(MiniMessage.miniMessage().deserialize(
                        "<click:copy_to_clipboard:$firstLocation|$secondLocation|10>" +
                                "<${MessageType.INGENIA}>Click to copy cinematic data.")
                    )
                }
            }
        }

        return false
    }
}