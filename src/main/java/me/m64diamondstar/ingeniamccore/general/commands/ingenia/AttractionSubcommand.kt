package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.custom.FreeFall
import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.Particle
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.util.Vector

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

        if(args[1].equals("create", ignoreCase = true)){
            if(args.size == 5){

                val attraction = Attraction(args[2], args[3])
                attraction.createAttraction(AttractionType.valueOf(args[4]), player.world)

            }
        }

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
    }

    fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if (args.size == 2) {
            tabs.add("create")
            tabs.add("delete")
            tabs.add("leaderboard")
        }

        if(args.size == 3 && !(args[1].equals("create", true) || args[1].equals("delete", true)))
            AttractionUtils.getCategories().forEach { tabs.add(it.name) }

        if(args.size == 4 && !(args[1].equals("create", true) || args[1].equals("delete", true)))
            AttractionUtils.getAttractions(args[2]).forEach { tabs.add(it.name) }

        if (args.size == 5) {
            if(args[1].equals("leaderboard", ignoreCase = true)){
                tabs.add("reload")
                tabs.add("setlocation")
            }

            if(args[1].equals("create", ignoreCase = true)){
                AttractionType.values().forEach { tabs.add(it.toString()) }
            }
        }

        tabs.remove(".DS_Store")

        return tabs
    }
}