package me.m64diamondstar.ingeniamccore.general.commands.ingenia

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.regions.Polygonal2DRegion
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.utils.IngeniaSubcommand
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.Exception
import java.lang.NumberFormatException

class AreaSubcommand(private val sender: CommandSender, private val args: Array<String>): IngeniaSubcommand {

    override fun execute() {

        if(sender !is Player){
            sender.sendMessage(Messages.noPlayer())
            return
        }

        val player = sender


        if(args[1].equals("create", ignoreCase = true) && args.size == 4) {

            if(AreaUtils.existsArea(args[2], args[3])){
                player.sendMessage(Colors.format(MessageType.ERROR + "The area ${args[2]} already exists. " +
                        "If you want to reset it, please delete it and re-create it."))
                return
            }

            val area = Area(args[2], args[3])
            area.createArea(player.world)

        }

        if(args[1].equals("modify", ignoreCase = true) && args.size >= 4){

            if(!AreaUtils.existsArea(args[2], args[3])){
                player.sendMessage(Colors.format(MessageType.ERROR + "The area ${args[2]} doesn't exist!"))
                return
            }

            val area = Area(args[2], args[3])

            if(args.size == 5 && args[3].equals("setdisplayname", ignoreCase = true)){
                area.displayName = args[4].replace("_", " ").replace("-", " ")
                player.sendMessage(Colors.format(MessageType.SUCCESS + "The display name has successfully been changed to ${args[4]}."))
            }

            else if(args.size == 5 && args[4].equals("setarea", ignoreCase = true)){
                try{
                    BukkitAdapter.adapt(player).selection as Polygonal2DRegion
                }catch (e: Exception){
                    player.sendMessage(Colors.format(MessageType.ERROR + "You don't have a polygonal region selected with WorldEdit!"))
                    player.sendMessage(Colors.format(MessageType.ERROR + "Please use //sel poly and reselect the area!"))
                    return
                }

                area.area = BukkitAdapter.adapt(player).selection as Polygonal2DRegion
                player.sendMessage(Colors.format(MessageType.SUCCESS + "The area has successfully been changed to your current WorldEdit selection!"))
            }

            else if(args.size == 6 && args[4].equals("setweight", ignoreCase = true)){
                try {
                    area.weight = args[5].toInt()
                    player.sendMessage(Colors.format(MessageType.SUCCESS + "The weight has successfully been changed to ${args[5]}."))
                }catch (ex: NumberFormatException){
                    player.sendMessage(Messages.invalidNumber())
                }
            }

            else if(args.size == 7 && args[4].equals("sety", ignoreCase = true)){
                if(args[5].equals("min", ignoreCase = true)){
                    try {
                        area.minY = args[6].toInt()
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "The minimum Y has been changed to ${args[6]}."))
                    }catch (ex: NumberFormatException){
                        player.sendMessage(Messages.invalidNumber())
                    }
                }else if(args[5].equals("max", ignoreCase = true)){
                    try {
                        area.maxY = args[6].toInt()
                        player.sendMessage(Colors.format(MessageType.SUCCESS + "The maximum Y has been changed to ${args[6]}."))
                    }catch (ex: NumberFormatException){
                        player.sendMessage(Messages.invalidNumber())
                    }
                }else{
                    player.sendMessage(Messages.invalidSubcommand("/ig area modify <name> sety <min/max> <y>"))
                }
            }

            else if(args.size == 6 && args[4].equals("setMusic", ignoreCase = true)){

                if(!args[5].contains("ingeniamc.net/music") || !args[5].endsWith(".mp3", false)){
                    player.sendMessage(Colors.format(MessageType.ERROR + "Please give a valid ingeniamc.net/music link!"))
                    return
                }

                area.setMusic(args[5])
            }

        }

    }

    override fun getTabCompleters(): ArrayList<String> {
        val tabs = ArrayList<String>()

        if (args.size == 2) {
            tabs.add("modify")
            tabs.add("create")
        }

        if(args.size == 3)
            AreaUtils.getCategories().forEach { tabs.add(it.name) }
        if(args.size == 4)
            AreaUtils.getAllAreas().forEach { tabs.add(it.name) }

        if(args.size > 4 && args[1].equals("modify", ignoreCase = true)){
            if(!AreaUtils.existsArea(args[2], args[3])) {
                tabs.add("area_does_not_exist!")
                return tabs
            }
        }

        if(args.size == 5){
            if(args[1].equals("modify", ignoreCase = true)){
                tabs.add("setDisplayname")
                tabs.add("setArea")
                tabs.add("setweight")
                tabs.add("setY")
                tabs.add("setMusic")
            }
        }

        if(args.size == 6){
            if(args[1].equals("modify", ignoreCase = true)){
                if(args[4].equals("setY", ignoreCase = true)){
                    tabs.add("min")
                    tabs.add("max")
                }
            }
        }

        return tabs
    }
}