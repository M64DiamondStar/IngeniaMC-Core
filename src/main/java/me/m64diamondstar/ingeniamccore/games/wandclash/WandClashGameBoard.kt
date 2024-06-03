package me.m64diamondstar.ingeniamccore.games.wandclash

import fr.mrmicky.fastboard.adventure.FastBoard
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashGamePhase
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

class WandClashGameBoard(private val player: Player, private val game: WandClashGame) {

    private val fastBoard = FastBoard(player)
    private var shownPhase = WandClashGamePhase.DISABLED

    fun removeBoard(){
        fastBoard.delete()
    }

    /**
     * Sets the countdown board depending on the game phase
     * @param countdown the countdown time in seconds, when it is -1 it'll display "Not enough players"
     */
    fun setCountdownBoard(countdown: Int){
        val lines = ArrayList<Component>()

        when(game.arena.getGamePhase()){

            WandClashGamePhase.WAITING_FOR_PLAYERS -> {
                if(shownPhase != WandClashGamePhase.WAITING_FOR_PLAYERS){
                    lines.addAll(getFormattedLines(
                        listOf(
                            Component.text("${Font.convertToSmallText("Waiting for players")} ").color(TextColor.fromHexString("#a57eff")),
                            Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(Font.convertToSmallText("${game.getPlayerCount()}/${game.arena.getData().maxPlayers}")).color(TextColor.fromHexString(MessageType.DEFAULT))),
                            Component.empty(),
                            Component.text("${Font.convertToSmallText("Game Mode Vote in")} ").color(TextColor.fromHexString("#a57eff")),
                            Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(if(countdown == -1) Font.convertToSmallText("Not enough players") else Font.convertToSmallText("${countdown + 1}s")).color(TextColor.fromHexString(MessageType.DEFAULT))),
                        )
                    ))
                    shownPhase = WandClashGamePhase.WAITING_FOR_PLAYERS
                    fastBoard.updateLines(lines)
                    fastBoard.updateTitle(IngeniaMC.miniMessage.deserialize("\uE028\uF80B\uF809${'\uE029'.plus(lines.size - 2)}"))
                }else{
                    fastBoard.updateLine(5, getFormattedLine(5, Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                        .append(Component.text(if(countdown == -1) Font.convertToSmallText("Not enough players") else Font.convertToSmallText("${countdown + 1}s")).color(TextColor.fromHexString(MessageType.DEFAULT)))))
                }

            }

            WandClashGamePhase.MODE_VOTE -> {
                if(shownPhase != WandClashGamePhase.MODE_VOTE){
                    lines.addAll(getFormattedLines(
                        listOf(
                            Component.text("${Font.convertToSmallText("Vote Game Mode")} ").color(TextColor.fromHexString("#a57eff")),
                            Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(Font.convertToSmallText("aaaaaaaaaaaa")).color(TextColor.fromHexString(MessageType.DEFAULT)).decorate(TextDecoration.OBFUSCATED)),
                            Component.empty(),
                            Component.text("${Font.convertToSmallText("Vote Ends In")} ").color(TextColor.fromHexString("#a57eff")),
                            Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(if(countdown == -1) Font.convertToSmallText("Not enough players") else Font.convertToSmallText("${countdown + 1}s")).color(TextColor.fromHexString(MessageType.DEFAULT))),
                        )
                    ))
                    shownPhase = WandClashGamePhase.MODE_VOTE
                    fastBoard.updateLines(lines)
                    fastBoard.updateTitle(IngeniaMC.miniMessage.deserialize("\uE028\uF80B\uF809${'\uE029'.plus(lines.size - 2)}"))
                }else{
                    fastBoard.updateLine(5, getFormattedLine(5, Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                        .append(Component.text(if(countdown == -1) Font.convertToSmallText("Not enough players") else Font.convertToSmallText("${countdown + 1}s")).color(TextColor.fromHexString(MessageType.DEFAULT)))))
                }

            }

            WandClashGamePhase.TEAM_CHOOSE -> {
                if(shownPhase != WandClashGamePhase.TEAM_CHOOSE){
                    lines.addAll(getFormattedLines(
                        listOf(
                            Component.text("${Font.convertToSmallText("Choose Team")} ").color(TextColor.fromHexString("#a57eff")),
                            Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(
                                    if(game.getChosenTeam(player) == null)
                                        Component.text(Font.convertToSmallText("aaaaaaaaaaaa")).color(TextColor.fromHexString(MessageType.DEFAULT)).decorate(TextDecoration.OBFUSCATED)
                                    else
                                        game.getChosenTeam(player)!!.getComponentDisplayName()
                                ),
                            Component.empty(),
                            Component.text("${Font.convertToSmallText("Game Starts In")} ").color(TextColor.fromHexString("#a57eff")),
                            Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(if(countdown == -1) Font.convertToSmallText("Not enough players") else Font.convertToSmallText("${countdown + 1}s")).color(TextColor.fromHexString(MessageType.DEFAULT))),
                        )
                    ))
                    shownPhase = WandClashGamePhase.TEAM_CHOOSE
                    fastBoard.updateLines(lines)
                    fastBoard.updateTitle(IngeniaMC.miniMessage.deserialize("\uE028\uF80B\uF809${'\uE029'.plus(lines.size - 2)}"))
                }else{
                    fastBoard.updateLine(5, getFormattedLine(5, Component.text(" » ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                        .append(Component.text(if(countdown == -1) Font.convertToSmallText("Not enough players") else Font.convertToSmallText("${countdown + 1}s")).color(TextColor.fromHexString(MessageType.DEFAULT)))))
                }

            }

            else -> {}
        }
    }

    private fun getFormattedLines(lines: List<Component>): ArrayList<Component>{
        val newLines = ArrayList<Component>()
        newLines.add(Component.text("\uEF30"))

        for(i in lines.indices){
            newLines.add(
                Component.text("${'\uEF11'.plus(i)}\uF80C  ").color(TextColor.color(255, 255, 255))
                    .append(lines[i]))
        }
        newLines.add(Component.text("\uEF31\uF80C"))

        return newLines
    }

    private fun getFormattedLine(index: Int, line: Component): Component{
        return Component.text("${'\uEF11'.plus(index)}\uF80C  ").color(TextColor.color(255, 255, 255))
            .append(line)
    }

}