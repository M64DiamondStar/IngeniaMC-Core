package me.m64diamondstar.ingeniamccore.attractions.traincarts.signs

import com.bergerkiller.bukkit.tc.events.SignActionEvent
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent
import com.bergerkiller.bukkit.tc.signactions.SignAction
import com.bergerkiller.bukkit.tc.signactions.SignActionType
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions
import me.m64diamondstar.ingeniamccore.attractions.custom.Coaster
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import java.lang.NumberFormatException

class SignActionAttraction: SignAction() {

    override fun match(info: SignActionEvent): Boolean {
        return info.isType("attraction")
    }

    override fun execute(info: SignActionEvent) {

        if (!info.hasGroup()) return

        if(info.isAction(SignActionType.GROUP_ENTER)) {

            // Register some default variables
            val coaster = Coaster(info.getLine(2), info.getLine(3))
            val row = info.getLine(1).split(" ")[1].toInt()

            // Main station row (row 1)
            if (row == 1){
                coaster.setRowOccupied(row, true)
            }else{

                // Check if next row is occupied
                //
                // Row IS occupied
                if(coaster.isRowOccupied(row - 1)){
                    coaster.activateRow(row, false) // Deactivates station so train stops
                    coaster.setRowOccupied(row, true)
                }

                //Row IS NOT occupied
                else{
                    coaster.activateRow(row, true) // Activates station so train continues
                }
            }
        }

        if(info.isAction(SignActionType.GROUP_LEAVE)){

            // Register some default variables
            val coaster = Coaster(info.getLine(2), info.getLine(3))
            val row = info.getLine(1).split(" ")[1].toInt()

            // Main station row (row 1)
            if (coaster.existsRow(row + 1)){
                coaster.activateRow(row + 1, true) // Activates previous row so previous train continues to next row
                coaster.setRowOccupied(row, false)
            }
        }


    }

    override fun build(event: SignChangeActionEvent): Boolean {
        event.lines.forEach {
            if(it.isEmpty()) {
                event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use this format:"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "           [train]"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "attraction <row in station>"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "        <category>"))
                event.player.sendMessage(Colors.format(MessageType.ERROR + "           <name>"))
                return false
            }
        }

        try{
            event.getLine(1).split(" ")[1].toInt()
        }catch (e: NumberFormatException){
            event.player.sendMessage(Colors.format(MessageType.ERROR + "Please use a valid number as row."))
        }

        if(!AttractionUtils.existsCategory(event.getLine(2)) || !AttractionUtils.existsAttraction(event.getLine(2), event.getLine(3))){
            event.player.sendMessage(Colors.format(MessageType.ERROR + "This attraction or category does not exist."))
            return false
        }

        event.player.sendMessage(Colors.format(MessageType.WARNING + "Don't forget to create this rows station sign and register it in the attraction system!"))
        return SignBuildOptions.create()
            .setName("Attraction Manager")
            .setDescription("manage IngeniaMC's railed rides")
            .handle(event.player)
    }

}