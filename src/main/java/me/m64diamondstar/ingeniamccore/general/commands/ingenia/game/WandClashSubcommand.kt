package me.m64diamondstar.ingeniamccore.general.commands.ingenia.game

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.wandclash.WandClashArena
import me.m64diamondstar.ingeniamccore.games.wandclash.WandClashArenaManager
import me.m64diamondstar.ingeniamccore.games.wandclash.WandClashRegistry
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandRegistry
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashGamePhase
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashTeam
import me.m64diamondstar.ingeniamccore.utils.Subcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class WandClashSubcommand(private val args: Array<String>, private val player: Player): Subcommand {

    override fun execute() {

        when(args[2].lowercase()){

            "create" -> {
                if(args.size == 4) {
                    val arena = WandClashArena(args[3])
                    arena.getData().displayName = args[3]
                    arena.getData().minPlayers = 2
                    arena.getData().maxPlayers = 8
                    arena.getData().playersCountdown = 45
                    arena.getData().gameModeCountdown = 15
                    arena.getData().teamCountdown = 15
                    arena.getData().lobbySpawnLocation = player.location
                    WandClashRegistry.registerArena(arena)

                    player.sendMessage(Colors.format(MessageType.SUCCESS + "The arena '${args[3]}' has been created."))
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Please use /ig game wandclash modify ${args[3]}... to modify it."))
                }else
                    player.sendMessage(Messages.commandUsage("ig game wandclash create <arena>"))
            }

            "delete" -> {
                if(args.size == 4) {
                    if(WandClashRegistry.getArenas().map{it.name.lowercase()}.contains(args[3].lowercase())){
                        WandClashRegistry.getArena(args[3].lowercase())?.delete()
                        WandClashRegistry.unregisterArena(args[3].lowercase())
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "The arena '${args[3]}' has been deleted."))
                    }else
                        player.sendMessage(Colors.format(MessageType.ERROR + "This arena doesn't exist."))
                }else
                    player.sendMessage(Messages.commandUsage("ig game wandclash delete <arena>"))
            }

            "join" -> {
                if(args.size == 4) {
                    if(WandClashRegistry.existsArena(args[3])){
                        val arena = WandClashRegistry.getArena(args[3])!!
                        if(!arena.getGamePhase().isJoinable()){
                            player.sendMessage(Colors.format(MessageType.ERROR + "The arena '${args[3]}' is not open or the game has already started."))
                            return
                        }else{
                            WandClashArenaManager.join(player, args[3])
                        }
                    }else
                        player.sendMessage(Colors.format(MessageType.ERROR + "This arena doesn't exist."))
                }else
                    player.sendMessage(Messages.commandUsage("ig game wandclash join <arena>"))
            }

            "setphase" -> {
                if(args.size == 5) {
                    if(WandClashGamePhase.values().map { it.toString() }.contains(args[4].uppercase()))
                    if(WandClashRegistry.existsArena(args[3])) {
                        val arena = WandClashRegistry.getArena(args[3])!!
                        arena.setGamePhase(WandClashGamePhase.valueOf(args[4].uppercase()))
                    }
                }
            }

            "wand" -> {
                when(args[3].lowercase()){
                    "get" -> {
                        if(args.size == 5){
                            val clashWand = ClashWandRegistry.getClashWand(args[4])
                            if(clashWand == null){
                                player.sendMessage(Colors.format(MessageType.ERROR + "This is not a valid clash wand"))
                                return
                            }

                            val item = ItemStack(Material.AMETHYST_SHARD)
                            val meta = item.itemMeta!!

                            meta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "clash-wand"), PersistentDataType.STRING, clashWand.getID())
                            meta.displayName(MiniMessage.miniMessage().deserialize("<#c47443>${clashWand.getDisplayName()}"))
                            meta.lore(listOf(MiniMessage.miniMessage().deserialize("<#878787>Work In Progress...")))
                            item.itemMeta = meta

                            player.inventory.addItem(item)
                        }else
                            player.sendMessage(Messages.commandUsage("ig game wandclash get <clash-wand>"))
                    }
                }
            }

            "modify" -> {
                if(args.size < 5){
                    player.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
                    return
                }

                val wandClashArena = WandClashArena(args[3])

                when(args[4].lowercase()){
                    "displayname" -> {
                        if(args.size == 6) {
                            wandClashArena.getData().displayName = args[5].replace("_", " ")
                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The display name has been set to " + args[5].replace("_", " ")))
                        }else{
                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> displayname <name>"))
                        }
                    }

                    "setleavelocation" -> {
                        if(args.size == 5) {
                            wandClashArena.getData().leaveLocation = player.location
                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The leave location has been set."))
                        }else{
                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> setleavelocation"))
                        }
                    }

                    "maxplayers" -> {
                        if(args.size == 6) {
                            val amount = args[5].toIntOrNull()
                            if(amount == null){
                                player.sendMessage(Messages.invalidNumber())
                            }
                            wandClashArena.getData().maxPlayers = amount!!
                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The maximum amount of players has been set to " + args[5]))
                        }else{
                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> maxplayers <amount>"))
                        }
                    }

                    "minplayers" -> {
                        if(args.size == 6) {
                            val amount = args[5].toIntOrNull()
                            if(amount == null){
                                player.sendMessage(Messages.invalidNumber())
                            }
                            wandClashArena.getData().minPlayers = amount!!
                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The minimum amount of players has been set to " + args[5]))
                        }else{
                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> minplayers <amount>"))
                        }
                    }

                    "lobby" -> {
                        when(args[5].lowercase()){
                            "setspawn" -> {
                                wandClashArena.getData().lobbySpawnLocation = player.location
                                player.sendMessage(Colors.format(MessageType.SUCCESS + "The spawn location of the lobby has been set to your location."))
                            }

                            "setgamemodecountdown" -> {
                                val amount = args[5].toIntOrNull()
                                if(amount == null){
                                    player.sendMessage(Messages.invalidNumber())
                                }
                                wandClashArena.getData().gameModeCountdown = amount!!
                                player.sendMessage(Colors.format(MessageType.SUCCESS + "The game mode countdown has been set to " + args[5] + " seconds."))
                            }

                            "setteamcountdown" -> {
                                val amount = args[5].toIntOrNull()
                                if(amount == null){
                                    player.sendMessage(Messages.invalidNumber())
                                }
                                wandClashArena.getData().teamCountdown = amount!!
                                player.sendMessage(Colors.format(MessageType.SUCCESS + "The team countdown has been set to " + args[5] + " seconds."))
                            }

                            "setplayercountdown" -> {
                                val amount = args[5].toIntOrNull()
                                if(amount == null){
                                    player.sendMessage(Messages.invalidNumber())
                                }
                                wandClashArena.getData().playersCountdown = amount!!
                                player.sendMessage(Colors.format(MessageType.SUCCESS + "The player waiting countdown has been set to " + args[5] + " seconds."))
                            }
                        }
                    }

                    "gamemode" -> {
                        when(args[5].lowercase()){
                            "tdm" -> {
                                when(args[6].lowercase()){
                                    "setspawn" -> {
                                        if(args.size == 8){
                                            if(!WandClashTeam.values().map { it.toString() }.contains(args[7].uppercase())){
                                                player.sendMessage(Colors.format(MessageType.ERROR + "The team '${args[7]}' is not a valid team."))
                                                player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode tdm setspawn <team>"))
                                            }
                                            wandClashArena.getData().GameModeSettings().setTDMSpawn(player.location, WandClashTeam.valueOf(args[7]))
                                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The spawn location of the TD Game Mode has been set to your location."))
                                        }else
                                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode tdm setspawn <team>"))
                                    }

                                    "setnecessarykills" -> {
                                        if(args.size == 8){
                                            val amount = args[7].toIntOrNull()
                                            if(amount == null){
                                                player.sendMessage(Messages.invalidNumber())
                                            }
                                            wandClashArena.getData().GameModeSettings().necessaryTDMKills = amount!!
                                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The necessary kills for the TD Game Mode has been set to " + args[7]))
                                        }else
                                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode tdm setnecessarykills <amount>"))
                                    }
                                }
                            }

                            "ffa" -> {
                                when(args[6].lowercase()){
                                    "addspawn" -> {
                                        wandClashArena.getData().GameModeSettings().addFFASpawn(player.location)
                                        player.sendMessage(Colors.format(MessageType.SUCCESS + "A new spawn location of the FFA Game Mode has been set to your location."))
                                    }

                                    "removeallspawns" -> {
                                        wandClashArena.getData().GameModeSettings().removeAllFFASpawns()
                                        player.sendMessage(Colors.format(MessageType.SUCCESS + "All spawn locations of the FFA Game Mode have been removed."))
                                    }

                                    "setnecessarykills" -> {
                                        if(args.size == 8){
                                            val amount = args[7].toIntOrNull()
                                            if(amount == null){
                                                player.sendMessage(Messages.invalidNumber())
                                            }
                                            wandClashArena.getData().GameModeSettings().necessaryFFAKills = amount!!
                                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The necessary kills for the FFA Game Mode has been set to " + args[7]))
                                        }else
                                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode ffa setnecessarykills <amount>"))
                                    }
                                }
                            }

                            "cp" -> {
                                when(args[6].lowercase()){
                                    "setspawn" -> {
                                        if(args.size == 8){
                                            if(!WandClashTeam.values().map { it.toString() }.contains(args[7].uppercase())){
                                                player.sendMessage(Colors.format(MessageType.ERROR + "The team '${args[7]}' is not a valid team."))
                                                player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode cp setspawn <team>"))
                                            }
                                            wandClashArena.getData().GameModeSettings().setCPSpawn(player.location, WandClashTeam.valueOf(args[7]))
                                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The spawn location of the CP Game Mode has been set to your location."))
                                        }else
                                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode cp setspawn <team>"))
                                    }

                                    "setnecessarypoints" -> {
                                        if(args.size == 8){
                                            val amount = args[7].toIntOrNull()
                                            if(amount == null){
                                                player.sendMessage(Messages.invalidNumber())
                                            }
                                            wandClashArena.getData().GameModeSettings().necessaryCPPoints = amount!!
                                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The necessary points for the CP Game Mode has been set to " + args[7]))
                                        }else
                                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode cp setnecessarypoints <amount>"))
                                    }
                                }
                            }

                            "mr" -> {
                                when(args[6].lowercase()){
                                    "setspawn" -> {
                                        if(args.size == 8){
                                            if(!WandClashTeam.values().map { it.toString() }.contains(args[7].uppercase())){
                                                player.sendMessage(Colors.format(MessageType.ERROR + "The team '${args[7]}' is not a valid team."))
                                                player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode mr setspawn <team>"))
                                            }
                                            wandClashArena.getData().GameModeSettings().setMRSpawn(player.location, WandClashTeam.valueOf(args[7]))
                                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The spawn location of the MR Game Mode has been set to your location."))
                                        }else
                                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode mr setspawn <team>"))
                                    }

                                    "setmanareactordurability" -> {
                                        if(args.size == 8){
                                            val amount = args[7].toIntOrNull()
                                            if(amount == null){
                                                player.sendMessage(Messages.invalidNumber())
                                            }
                                            wandClashArena.getData().GameModeSettings().manaReactorDurability = amount!!
                                            player.sendMessage(Colors.format(MessageType.SUCCESS + "The mana reactor durability for the MR Game Mode has been set to " + args[7]))
                                        }else
                                            player.sendMessage(Messages.commandUsage("ig game wandclash modify <arena> gamemode mr setmanareactordurability <durability>"))
                                    }
                                }
                            }
                        }
                    }
                }

            }


            else -> player.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
        }


    }

}