package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.attraction.FreefallSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList

class AttractionSubcommand(private val sender: CommandSender, private val args: Array<String>) {

    /**
     * Execute the command
     */
    fun execute() {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        val player = sender

        if(args.size < 2){
            player.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
        }

        /*
        Create subcommand
        Create new attractions or/and new categories
         */
        if(args[1].equals("create", ignoreCase = true)){
            if(args.size == 5){

                val attraction = Attraction(args[2], args[3])
                attraction.createAttraction(AttractionType.valueOf(args[4]), player.world)
                player.sendMessage(Colors.format(MessageType.SUCCESS + "Attraction has been created!"))

            }else
                player.sendMessage(Messages.commandUsage("ig attraction create <category> <name> <type>"))
        }

        /*
        Leaderboard subcommand
        Manage/reload the leaderboard of given attraction
         */
        if(args[1].equals("leaderboard", ignoreCase = true)){
            if(args.size == 5 && args[4].equals("setlocation", true)){
                if(!AttractionUtils.existsCategory(args[2])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[2]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }
                if(!AttractionUtils.existsAttraction(args[2], args[3])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }

                val attraction = Attraction(args[2], args[3])
                var found = false

                player.getNearbyEntities(5.0, 5.0, 5.0).forEach {
                    if(it.type != EntityType.ITEM_FRAME)
                        return@forEach
                    it as ItemFrame

                    val toFrame = it.location.toVector().subtract(player.eyeLocation.toVector())
                    val dot = toFrame.normalize().dot(player.eyeLocation.direction)

                    //if the player looks at the direction of the itemframe
                    if(dot > 0.95){
                        it.remove()

                        found = true
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Leaderboard location has been set."))
                        player.spawnParticle(Particle.SMOKE_NORMAL, it.location, 100, 0.23, 0.23, 0.23, 0.0)

                        attraction.setLeaderboardEnabled(true)
                        attraction.setLeaderboardLocation(it.location.blockX, it.location.blockY, it.location.blockZ)
                        attraction.setLeaderboardDirection(it.facing.toString())
                        attraction.spawnRidecountSign()

                        return
                    }
                }

                if(!found){
                    player.sendMessage(Colors.format(MessageType.ERROR + "Didn't find an ItemFrame, please place an ItemFrame" +
                            " and look at it while executing this command."))
                }

            }else if(args.size == 5 && args[4].equals("reload", true)){
                if(!AttractionUtils.existsCategory(args[2])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[2]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }
                if(!AttractionUtils.existsAttraction(args[2], args[3])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }

                val attraction = Attraction(args[2], args[3])
                attraction.spawnRidecountSign()

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Leaderboard has been reloaded."))
            }
        }

        /*
        Ridecount subcommand
        Get/set/add or remove ridecount of a player from an attraction
         */
        if(args[1].equals("ridecount", ignoreCase = true)){
            if(args.size > 4){
                if(!AttractionUtils.existsCategory(args[2])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[2]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }
                if(!AttractionUtils.existsAttraction(args[2], args[3])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }

                val attraction = Attraction(args[2], args[3])

                // Get ridecount of player
                if(args.size == 6 && args[4].equals("get", ignoreCase = true)){
                    if(attraction.getRidecountInMap().containsKey(args[5])){
                        player.sendMessage(Colors.format(MessageType.INFO + "${args[5]}'s ridecount for " +
                                "${args[3].replace(".yml", "")} is &n${attraction.getRidecountInMap()[args[5]]}&r${MessageType.ERROR}."))
                    }else{
                        player.sendMessage(Colors.format(MessageType.ERROR + "${args[5]}'s ridecount for " +
                                "${args[3].replace(".yml", "")} is &n0&r${MessageType.ERROR} or the player does not exist."))
                    }
                }

                //Add, set or remove ridecount to/from player
                else if(args.size == 7 && (args[4].equals("set", ignoreCase = true)
                            || args[4].equals("add", ignoreCase = true)
                            || args[4].equals("remove", ignoreCase = true))){

                    if(attraction.getRidecountInMap().containsKey(args[5])){
                        var ridecount = 0
                        var uuid = UUID.randomUUID()
                        attraction.getRidecountInMapUuid().forEach { (t, v) -> if(Bukkit.getOfflinePlayer(t).name.equals(args[5], ignoreCase = true)) {
                                ridecount = v
                                uuid = t
                            }
                        }

                        try {

                            if (args[4].equals("set", ignoreCase = true)) {
                                attraction.getConfig().set("Data.Ridecount.$uuid.Count", Integer.parseInt(args[6]))
                                attraction.reloadConfig()
                                player.sendMessage("${MessageType.SUCCESS}${args[5]}'s ridecount has been set to ${args[6]}.")
                            }

                            if (args[4].equals("add", ignoreCase = true)) {
                                attraction.getConfig().set("Data.Ridecount.$uuid.Count", ridecount + Integer.parseInt(args[6]))
                                attraction.reloadConfig()
                                player.sendMessage("${MessageType.SUCCESS}${args[5]}'s ridecount has been set to ${args[6]}.")
                            }

                            if (args[4].equals("remove", ignoreCase = true)) {
                                attraction.getConfig().set("Data.Ridecount.$uuid.Count", ridecount - Integer.parseInt(args[6]))
                                attraction.reloadConfig()
                                player.sendMessage("${MessageType.SUCCESS}${args[5]}'s ridecount has been set to ${args[6]}.")
                            }

                        }catch (e: NumberFormatException){
                            player.sendMessage(Colors.format("${MessageType.ERROR}&n${args[6]}&r ${MessageType.ERROR}is not a valid number."))
                        }
                    }else{
                        player.sendMessage(Colors.format("${MessageType.ERROR}This player is not registered yet. " +
                                "You can only change ridecount of players who have already ridden this ride."))
                    }

                }else
                    player.sendMessage(Messages.commandUsage("ig attraction ridecount <category> <attraction> <get/add/set/remove> <player> [amount]"))

            }
        }

        /*
        subcommand to manage warp settings
        set location or set item
         */
        if(args[1].equals("warp", ignoreCase = true)){
            if(args.size == 5){
                if(!AttractionUtils.existsCategory(args[2])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The category &o${args[2]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }
                if(!AttractionUtils.existsAttraction(args[2], args[3])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The attraction &o${args[3]}&r ${MessageType.ERROR}doesn't exist!"))
                    return
                }

                val attraction = Attraction(args[2], args[3])

                var complete = false

                if(args[4].equals("setlocation", ignoreCase = true)){
                    complete = attraction.setWarpLocation(player.location)
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "The warp location has been set to your current location."))
                }else if(args[4].equals("setitem", ignoreCase = true)){
                    if(player.inventory.itemInMainHand.type == Material.AIR){
                        player.sendMessage(Colors.format(MessageType.ERROR + "Item cannot be air. Please hold another item in your hand"))
                    }else{
                        complete = attraction.setWarpItem(player.inventory.itemInMainHand)
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "The warp item has been set to the item in your hand."))
                    }
                }else
                    player.sendMessage(Messages.commandUsage("ig attraction warp <category> <attraction> <setlocation/setitem>"))

                if(complete)
                    player.sendMessage(Colors.format(MessageType.INFO + "All the details are set. The warp has automatically been enabled."))
            }
        }

        /*
        Attraction Settings
        Set the settings for custom attractions like freefall towers or top spins
         */
        if(args[1].equals("freefall", ignoreCase = true)){
            val freefallSubcommand = FreefallSubcommand(sender, args, player)
            freefallSubcommand.execute()
        }
    }

    fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if (args.size == 2) {
            tabs.add("create")
            tabs.add("delete")
            tabs.add("leaderboard")
            tabs.add("ridecount")
            tabs.add("warp")
            tabs.add("freefall")
        }

        //Global tab completer for attraction categories and names except for create and delete subcommand
        if(args.size == 3 && !(args[1].equals("create", true) || args[1].equals("delete", true)))
            AttractionUtils.getCategories().forEach { tabs.add(it.name) }

        if(args.size == 4 && !(args[1].equals("create", true) || args[1].equals("delete", true) || args[1].equals("freefall", true)))
            AttractionUtils.getAttractions(args[2]).forEach { tabs.add(it.name) }

        //FreeFall tab completer to make sure only freefall attractions get added
        if(args.size == 4 && args[1].equals("freefall", ignoreCase = true)) {
            var exists = false
            AttractionUtils.getAttractions(args[2]).forEach {
                val attraction = Attraction(args[2], it.name)
                if (attraction.getType() == AttractionType.FREEFALL) {
                    tabs.add(it.name)
                    exists = true
                }
            }
            if(!exists)
                tabs.add("none")
        }

        //Subcommands after the category and name
        if (args.size == 5) {
            if(args[1].equals("leaderboard", ignoreCase = true)){
                tabs.add("reload")
                tabs.add("setlocation")
            }

            if(args[1].equals("create", ignoreCase = true)){
                AttractionType.values().forEach { tabs.add(it.toString()) }
            }

            if(args[1].equals("ridecount", ignoreCase = true)){
                tabs.add("get")
                tabs.add("set")
                tabs.add("remove")
                tabs.add("add")
            }

            if(args[1].equals("warp", ignoreCase = true)){
                tabs.add("setlocation")
                tabs.add("setitem")
            }

            if(args[1].equals("freefall", ignoreCase = true)){
                tabs.add("setspawn")
                tabs.add("spawn")
                tabs.add("despawn")
            }
        }

        if(args.size == 6){
            if(args[1].equals("ridecount", ignoreCase = true)){
                Bukkit.getOnlinePlayers().forEach { tabs.add(it.name) }
            }
        }

        if(args.size == 7){
            if(args[1].equals("ridecount", ignoreCase = true) && !args[4].equals("get", ignoreCase = true)){
                tabs.add("1")
                tabs.add("2")
                tabs.add("3")
                tabs.add("4")
                tabs.add("5")
                tabs.add("6")
                tabs.add("7")
                tabs.add("8")
                tabs.add("9")
            }
        }

        //Removes unnecessary file settings
        tabs.remove(".DS_Store")

        return tabs
    }
}