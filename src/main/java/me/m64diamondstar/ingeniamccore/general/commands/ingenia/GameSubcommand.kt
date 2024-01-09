package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWord
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWordUtils
import me.m64diamondstar.ingeniamccore.games.parkour.Parkour
import me.m64diamondstar.ingeniamccore.games.parkour.ParkourUtils
import me.m64diamondstar.ingeniamccore.games.presenthunt.PresentHunt
import me.m64diamondstar.ingeniamccore.games.presenthunt.PresentHuntUtils
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattle
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player

class GameSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        val player = sender

        if(args.size < 2){
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
        }

        if(args[1].equals("guesstheword", ignoreCase = true)){
            if(args.size == 4){
                if(args[2].equals("add", ignoreCase = true)){
                    GuessTheWordUtils.addWord(args[3])
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "The word has successfully been added to the list."))
                }

                else if(args[2].equals("remove", ignoreCase = true)){
                    val success = GuessTheWordUtils.removeWord(args[3])

                    if(success){
                        sender.sendMessage(Colors.format(MessageType.SUCCESS + "The word has successfully been removed from the list."))
                    }else{
                        sender.sendMessage(Colors.format(MessageType.ERROR + "This word is not on the list."))
                    }
                }
            }

            else if(args.size == 3){
                if(args[2].equals("start", ignoreCase = true)){
                    val guessTheWord = GuessTheWord()
                    guessTheWord.execute()
                    sender.sendMessage(Colors.format(MessageType.SUCCESS + "Started a new game of Guess The Word."))
                }
            }else{
                sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
            }
        }


        else if(args[1].equals("parkour", ignoreCase = true)){
            if(args[2].equals("create", ignoreCase = true)){
                if(ParkourUtils.existsParkour(args[3], args[4])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The parkour ${args[4]} already exists. " +
                            "If you want to reset it, please delete it and re-create it."))
                    return
                }

                val parkour = Parkour(args[3], args[4])
                parkour.create(player.world)

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Parkour has successfully been created!"))
            }

            else if(args[2].equals("modify", ignoreCase = true)){
                if(!ParkourUtils.existsParkour(args[3], args[4])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The parkour ${args[4]} doesn't exist!"))
                    return
                }

                val parkour = Parkour(args[3], args[4])

                if(args[5].equals("setDisplay", ignoreCase = true) && args.size == 7){
                    parkour.displayName = args[6].replace("_", " ").replace("-", " ")
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the display name of the parkour!"))
                }

                else if(args[5].equals("setStart", ignoreCase = true) && args.size == 6){
                    parkour.startLocation = player.location
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the start location of the parkour!"))
                }

                else if(args[5].equals("setEnd", ignoreCase = true) && args.size == 6){
                    parkour.endLocation = player.location
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the end location of the parkour!"))
                }

                else if(args[5].equals("setStartRadius", ignoreCase = true) && args.size == 7){
                    try {
                        parkour.startRadius = args[6].toDouble()
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the start radius of the parkour!"))
                    }catch (e: NumberFormatException){
                        player.sendMessage(Messages.invalidNumber())
                    }
                }

                else if(args[5].equals("setEndRadius", ignoreCase = true) && args.size == 7){
                    try {
                        parkour.endRadius = args[6].toDouble()
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the end radius of the parkour!"))
                    }catch (e: NumberFormatException){
                        player.sendMessage(Messages.invalidNumber())
                    }
                }

                /*
                    Leaderboard subcommand
                    Manage/reload the leaderboard of given splash battle
                 */
                if(args[5].equals("leaderboard", ignoreCase = true)){
                    if(args.size == 7 && args[6].equals("setlocation", true)){

                        player.getNearbyEntities(5.0, 5.0, 5.0).forEach {
                            if(it.type != EntityType.ITEM_FRAME)
                                return@forEach
                            it as ItemFrame

                            val toFrame = it.location.toVector().subtract(player.eyeLocation.toVector())
                            val dot = toFrame.normalize().dot(player.eyeLocation.direction)

                            //if the player looks at the direction of the itemframe
                            if(dot > 0.95){
                                it.remove()

                                player.sendMessage(Colors.format(MessageType.SUCCESS + "Leaderboard location has been set."))
                                player.spawnParticle(Particle.SMOKE_NORMAL, it.location, 100, 0.23, 0.23, 0.23, 0.0)

                                parkour.getLeaderboard().setLeaderboardEnabled(true)
                                parkour.getLeaderboard().setLeaderboardLocation(it.location.blockX, it.location.blockY, it.location.blockZ)
                                parkour.getLeaderboard().setLeaderboardDirection(it.facing.toString())
                                parkour.getLeaderboard().spawnSign()

                                return
                            }
                        }

                        player.sendMessage(Colors.format(MessageType.ERROR + "Didn't find an ItemFrame, please place an ItemFrame" +
                                " and look at it while executing this command."))


                    }else if(args.size == 7 && args[6].equals("reload", true)){
                        parkour.getLeaderboard().spawnSign()

                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Leaderboard has been reloaded."))
                    }
                }

            }

            else if(args[2].equals("reload", ignoreCase = true) && args.size == 5) {
                if (!ParkourUtils.existsParkour(args[3], args[4])) {
                    player.sendMessage(Colors.format(MessageType.ERROR + "The parkour ${args[4]} doesn't exist!"))
                    return
                }

                val parkour = Parkour(args[3], args[4])
                parkour.reload()
            }

            else{
                sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
            }
        }





        else if(args[1].equals("presenthunt", ignoreCase = true)){
            if(args[2].equals("create", ignoreCase = true)){
                if(PresentHuntUtils.existsPresentHunt(args[3], args[4])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The present hunt ${args[4]} already exists. " +
                            "If you want to reset it, please delete it and re-create it."))
                    return
                }

                val presentHunt = PresentHunt(args[3], args[4])
                presentHunt.createPresentHunt(player.world)

                player.sendMessage(Colors.format(MessageType.SUCCESS + "Present Hunt has successfully been created!"))
                player.sendMessage(Colors.format(MessageType.SUCCESS + "Please add presents by using:"))
                player.sendMessage(Colors.format(MessageType.SUCCESS + "/ig game presenthunt modify ${args[3]} ${args[4]} addPresent"))
                player.sendMessage(Colors.format(MessageType.SUCCESS + "While looking at a block!"))
            }

            else if(args[2].equals("modify", ignoreCase = true)) {

                if(!PresentHuntUtils.existsPresentHunt(args[3], args[4])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "The present hunt ${args[4]} doesn't exist!"))
                    return
                }

                val presentHunt = PresentHunt(args[3], args[4])

                if(args[5].equals("addPresent", ignoreCase = true) && args.size == 6){
                    val block = player.getTargetBlockExact(5)
                    if(block == null || block.type == Material.AIR){
                        player.sendMessage(Colors.format(MessageType.ERROR + "Please look at a block to set the location."))
                        return
                    }
                    val location = block.location

                    player.spawnParticle(
                        Particle.SMOKE_NORMAL, location.add(0.5, 0.5, 0.5),
                        100, 0.2, 0.2, 0.2, 0.0)
                    location.block.type = Material.AIR
                    presentHunt.addLocation(location)
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Added present location."))
                }

                else if(args[5].equals("removePresent", ignoreCase = true) && args.size == 6){
                    val block = player.getTargetBlockExact(5)
                    if(block == null || block.type == Material.AIR){
                        player.sendMessage(Colors.format(MessageType.ERROR + "Please look at a block to remove the location."))
                        return
                    }
                    val location = block.location

                    player.spawnParticle(
                        Particle.SMOKE_NORMAL, location.add(0.5, 0.5, 0.5),
                        100, 0.2, 0.2, 0.2, 0.0)
                    location.block.type = Material.AIR
                    val success = presentHunt.removeLocation(location)

                    if(!success)
                        player.sendMessage(Colors.format(MessageType.ERROR + "There was no present location here."))
                    else
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Removed present location."))

                    PresentHuntUtils.removeActivePresent(location)
                }

                else if(args[5].equals("spawnRandomPresent", ignoreCase = true) && args.size == 6){
                    presentHunt.spawnRandomPresent()
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "Forced random present spawn."))
                }

                else if(args[5].equals("despawnAllPresents", ignoreCase = true) && args.size == 6){
                    presentHunt.getLocations().forEach {
                        if(PresentHuntUtils.containsActivePresent(it)){
                            PresentHuntUtils.removeActivePresent(it)
                            it.block.type = Material.AIR
                        }
                    }
                }

            }
        }




        else if(args[1].equals("splashbattle", ignoreCase = true)) {
            if(args.size < 3){
                player.sendMessage(Colors.format(MessageType.ERROR + "Not enough arguments."))
                return
            }

            if(args.size == 4 && args[2].equals("create", ignoreCase = true)){
                val splashBattle = SplashBattle(args[3])
                splashBattle.create(player.world)
                player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully created the Splash Battle."))
            }

            if(args.size > 4 && args[2].equals("modify", ignoreCase = true)){
                if(!SplashBattleUtils.existsSplashBattle(args[3])){
                    player.sendMessage(Colors.format(MessageType.ERROR + "Splash Battle doesn't exist."))
                    return
                }

                val splashBattle = SplashBattle(args[3])

                if(args.size == 5) {

                    if (args[4].equals("addSpawnPoint", ignoreCase = true)) {
                        splashBattle.addSpawnPoint(player.location)
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Added Spawn Point."))
                    }

                    else if (args[4].equals("setJoinSign", ignoreCase = true)) {

                        if(player.getTargetBlockExact(5, FluidCollisionMode.NEVER) == null) {
                            player.sendMessage(Colors.format(MessageType.ERROR + "No block in radius. Please look at a sign."))
                            return
                        }

                        if(!player.getTargetBlockExact(5, FluidCollisionMode.NEVER)!!.type.toString().contains("SIGN")) {
                            player.sendMessage(Colors.format(MessageType.ERROR + "Please look at a sign."))
                            return
                        }

                        val location = player.getTargetBlockExact(5, FluidCollisionMode.NEVER)!!.location

                        splashBattle.joinSignLocation = location
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Set sign as join sign."))
                        player.spawnParticle(Particle.SMOKE_NORMAL, location.add(0.5, 0.5, 0.5),
                            100, 0.0, 0.2, 0.0, 0.0)
                    }

                }

                if(args.size == 6)
                    if(args[4].equals("setDisplay", ignoreCase = true)){
                        splashBattle.displayName = args[5].replace("_", " ").replace("-", " ")
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Successfully changed the display name of the Splash Battle!"))
                    }

                /*
                    Leaderboard subcommand
                    Manage/reload the leaderboard of given splash battle
                 */
                if(args[4].equals("leaderboard", ignoreCase = true)){
                    if(args.size == 6 && args[5].equals("setlocation", true)){

                        player.getNearbyEntities(5.0, 5.0, 5.0).forEach {
                            if(it.type != EntityType.ITEM_FRAME)
                                return@forEach
                            it as ItemFrame

                            val toFrame = it.location.toVector().subtract(player.eyeLocation.toVector())
                            val dot = toFrame.normalize().dot(player.eyeLocation.direction)

                            //if the player looks at the direction of the itemframe
                            if(dot > 0.95){
                                it.remove()

                                player.sendMessage(Colors.format(MessageType.SUCCESS + "Leaderboard location has been set."))
                                player.spawnParticle(Particle.SMOKE_NORMAL, it.location, 100, 0.23, 0.23, 0.23, 0.0)

                                splashBattle.getLeaderboard().setLeaderboardEnabled(true)
                                splashBattle.getLeaderboard().setLeaderboardLocation(it.location.blockX, it.location.blockY, it.location.blockZ)
                                splashBattle.getLeaderboard().setLeaderboardDirection(it.facing.toString())
                                splashBattle.getLeaderboard().spawnSoaksSign()

                                return
                            }
                        }

                        player.sendMessage(Colors.format(MessageType.ERROR + "Didn't find an ItemFrame, please place an ItemFrame" +
                                " and look at it while executing this command."))

                    }else if(args.size == 6 && args[5].equals("reload", true)){
                        splashBattle.getLeaderboard().spawnSoaksSign()

                        player.sendMessage(Colors.format(MessageType.SUCCESS + "Leaderboard has been reloaded."))
                    }
                }

            }

        }

        else{
            sender.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid sub-command! Check the auto tab-completion!"))
        }

    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()
        if(args.size == 2){
            tabs.add("guesstheword")
            tabs.add("parkour")
            tabs.add("presenthunt")
            tabs.add("splashbattle")
            tabs.add("leaderboard")
        }

        if(args.size == 3){
            if(args[1].equals("guesstheword", ignoreCase = true)){
                tabs.add("start")
                tabs.add("add")
                tabs.add("remove")
            }

            if(args[1].equals("parkour", ignoreCase = true) || args[1].equals("presenthunt", ignoreCase = true)
                || args[1].equals("splashbattle", ignoreCase = true)){
                tabs.add("create")
                tabs.add("modify")
            }
        }

        if(args.size == 4){
            if(args[1].equals("parkour", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    ParkourUtils.getCategories().forEach { tabs.add(it.name) }
                }
            }

            if(args[1].equals("presenthunt", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    PresentHuntUtils.getCategories().forEach { tabs.add(it.name) }
                }
            }

            if(args[1].equals("splashbattle", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    SplashBattleUtils.getSplashBattles().forEach { tabs.add(it.name) }
                }
            }
        }

        if(args.size == 5){
            if(args[1].equals("parkour", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    if(!ParkourUtils.existsCategory(args[3]))
                        tabs.add("CATEGORY_DOES_NOT_EXIST")
                    else
                        ParkourUtils.getParkours(args[3]).forEach { tabs.add(it.name) }
                }
            }

            if(args[1].equals("presenthunt", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    if(!PresentHuntUtils.existsCategory(args[3]))
                        tabs.add("CATEGORY_DOES_NOT_EXIST")
                    else
                        PresentHuntUtils.getPresentHunts(args[3]).forEach { tabs.add(it.name) }
                }
            }

            if(args[1].equals("splashbattle", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    if(!SplashBattleUtils.existsSplashBattle(args[3]))
                        tabs.add("BATTLE_DOES_NOT_EXIST")
                    else{
                        tabs.add("addSpawnPoint")
                        tabs.add("setDisplay")
                        tabs.add("setJoinSign")
                        tabs.add("leaderboard")
                    }
                }
            }
        }

        if(args.size == 6){
            if(args[1].equals("parkour", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    if(!ParkourUtils.existsParkour(args[3], args[4]))
                        tabs.add("PARKOUR_DOES_NOT_EXIST")
                    else {
                        tabs.add("setStart")
                        tabs.add("setEnd")
                        tabs.add("setStartRadius")
                        tabs.add("setEndRadius")
                        tabs.add("setDisplay")
                        tabs.add("leaderboard")
                    }
                }
            }

            if(args[1].equals("presenthunt", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true)){
                    if(!PresentHuntUtils.existsPresentHunt(args[3], args[4]))
                        tabs.add("PRESENT_HUNT_DOES_NOT_EXIST")
                    else {
                        tabs.add("addPresent")
                        tabs.add("removePresent")
                        tabs.add("spawnRandomPresent")
                        tabs.add("despawnAllPresents")
                    }
                }
            }

            if(args[1].equals("splashbattle", ignoreCase = true)){
                if(args[2].equals("modify", ignoreCase = true) && args[4].equals("leaderboard", ignoreCase = true)){
                    if(!SplashBattleUtils.existsSplashBattle(args[3]))
                        tabs.add("BATTLE_DOES_NOT_EXIST")
                    else{
                        tabs.add("reload")
                        tabs.add("setLocation")
                    }
                }
            }
        }

        if(args.size == 7) {
            if (args[1].equals("parkour", ignoreCase = true)) {
                if (args[2].equals("modify", ignoreCase = true)) {
                    if (!ParkourUtils.existsParkour(args[3], args[4]))
                        tabs.add("PARKOUR_DOES_NOT_EXIST")
                    else {
                        tabs.add("setLocation")
                        tabs.add("reload")
                    }
                }
            }
        }

        return tabs
    }
}