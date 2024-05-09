package me.m64diamondstar.ingeniamccore.npc.utils

import me.m64diamondstar.ingeniamccore.npc.Npc
import org.bukkit.entity.Player

object DialoguePlayerRegistry {

    private val dialoguePlayers = HashMap<Player, Npc>()
    private val playerCooldown = HashMap<Player, Long>()

    fun addDialoguePlayer(player: Player, npc: Npc){
        dialoguePlayers[player] = npc
    }

    fun getDialoguePlayer(player: Player): Npc?{
        return dialoguePlayers[player]
    }

    fun removeDialoguePlayer(player: Player){
        dialoguePlayers.remove(player)
    }

    fun contains(player: Player): Boolean{
        return dialoguePlayers.containsKey(player)
    }

    fun putOnCooldown(player: Player){
        playerCooldown[player] = System.currentTimeMillis() + 5000
    }

    fun isOnCooldown(player: Player): Boolean{
        return playerCooldown.containsKey(player) && playerCooldown[player]!! > System.currentTimeMillis()
    }

}