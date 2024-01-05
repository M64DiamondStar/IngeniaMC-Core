package me.m64diamondstar.ingeniamccore.games.parkour.listeners

import me.m64diamondstar.ingeniamccore.games.parkour.ParkourUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.protect.FeatureManager
import me.m64diamondstar.ingeniamccore.protect.FeatureType
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener: Listener {

    private val featureDisabledCooldown = HashMap<Player, Long>()

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent){
        val player = event.player
        val ingeniaPlayer = IngeniaPlayer(player)
        if(ingeniaPlayer.isInGame) return
        for(parkour in ParkourUtils.getAllParkours()){
            if(parkour.isInstalled()) {
                if (player.location.distanceSquared(parkour.startLocation!!) <= parkour.startRadius){

                    val featureManager = FeatureManager()
                    if(!featureManager.isFeatureEnabled(FeatureType.PARKOURS) && !player.hasPermission("ingenia.admin")){
                        if(!featureDisabledCooldown.containsKey(player) || featureDisabledCooldown[player]!! < System.currentTimeMillis()){
                            player.sendMessage(Messages.featureDisabled())
                            featureDisabledCooldown[player] = System.currentTimeMillis() + 2000
                        }
                        return
                    }

                    //Start Parkour, everything that happens next is controlled from this method
                    ingeniaPlayer.startParkour(parkour)
                }
            }
        }
    }

}