package me.m64diamondstar.ingeniamccore.games.wandclash

import fr.mrmicky.fastboard.adventure.FastBoard
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashGamePhase
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
                    fastBoard.updateTitle(IngeniaMC.miniMessage.deserialize("<bold><gradient:#6E08FB:#BB88EE:#6E08FB>Wand Clash"))
                    lines.addAll(
                        listOf(
                            Component.empty(),
                            Component.text("Waiting for players").color(TextColor.fromHexString("#7247d6")),
                            Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text("${game.getPlayerCount()}/${game.arena.getData().maxPlayers}").color(TextColor.fromHexString(MessageType.DEFAULT))),
                            Component.empty(),
                            Component.text("Game Mode Vote in").color(TextColor.fromHexString("#7247d6")),
                            Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(if(countdown == -1) "Not enough players" else "${countdown + 1}s").color(TextColor.fromHexString(MessageType.DEFAULT))),
                            Component.empty()
                        )
                    )
                    shownPhase = WandClashGamePhase.WAITING_FOR_PLAYERS
                    fastBoard.updateLines(lines)
                }else{
                    fastBoard.updateLine(5, Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                        .append(Component.text(if(countdown == -1) "Not enough players" else "${countdown + 1}s").color(TextColor.fromHexString(MessageType.DEFAULT))))
                }

            }

            WandClashGamePhase.MODE_VOTE -> {
                if(shownPhase != WandClashGamePhase.MODE_VOTE){
                    fastBoard.updateTitle(IngeniaMC.miniMessage.deserialize("<bold><gradient:#6E08FB:#BB88EE:#6E08FB>Wand Clash"))
                    lines.addAll(
                        listOf(
                            Component.empty(),
                            Component.text("Vote for Game Mode").color(TextColor.fromHexString("#7247d6")),
                            Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text("aaaaaaaaaaaa").color(TextColor.fromHexString(MessageType.DEFAULT)).decorate(TextDecoration.OBFUSCATED)),
                            Component.empty(),
                            Component.text("Vote Ends in").color(TextColor.fromHexString("#7247d6")),
                            Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(if(countdown == -1) "Not enough players" else "${countdown + 1}s").color(TextColor.fromHexString(MessageType.DEFAULT))),
                            Component.empty()
                        )
                    )
                    shownPhase = WandClashGamePhase.MODE_VOTE
                    fastBoard.updateLines(lines)
                }else{
                    fastBoard.updateLine(5, Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                        .append(Component.text(if(countdown == -1) "Not enough players" else "${countdown + 1}s").color(TextColor.fromHexString(MessageType.DEFAULT))))
                }

            }

            WandClashGamePhase.TEAM_CHOOSE -> {
                if(shownPhase != WandClashGamePhase.MODE_VOTE){
                    fastBoard.updateTitle(IngeniaMC.miniMessage.deserialize("<bold><gradient:#6E08FB:#BB88EE:#6E08FB>Wand Clash"))
                    lines.addAll(
                        listOf(
                            Component.empty(),
                            Component.text("Choose Team").color(TextColor.fromHexString("#7247d6")),
                            Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(
                                    if(game.getChosenTeam(player) == null)
                                        Component.text("aaaaaaaaaaaa").color(TextColor.fromHexString(MessageType.DEFAULT)).decorate(TextDecoration.OBFUSCATED)
                                    else
                                        game.getChosenTeam(player)!!.getComponentDisplayName()
                                ),
                            Component.empty(),
                            Component.text("Choosing Ends in").color(TextColor.fromHexString("#7247d6")),
                            Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                                .append(Component.text(if(countdown == -1) "Not enough players" else "${countdown + 1}s").color(TextColor.fromHexString(MessageType.DEFAULT))),
                            Component.empty()
                        )
                    )
                    shownPhase = WandClashGamePhase.MODE_VOTE
                    fastBoard.updateLines(lines)
                }else{
                    fastBoard.updateLine(5, Component.text("» ").color(TextColor.fromHexString(MessageType.BACKGROUND))
                        .append(Component.text(if(countdown == -1) "Not enough players" else "${countdown + 1}s").color(TextColor.fromHexString(MessageType.DEFAULT))))
                }

            }

            else -> {}
        }
    }

}