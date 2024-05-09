package me.m64diamondstar.ingeniamccore.general.areas.listeners

import com.bergerkiller.bukkit.tc.controller.MinecartMember
import com.craftmend.openaudiomc.api.ClientApi
import com.craftmend.openaudiomc.api.MediaApi
import com.craftmend.openaudiomc.api.media.Media
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.areas.AreaAudioManager
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent

class PlayerMoveListener: Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent){
        runCheck(event)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent){
        runCheck(event)
    }

    private fun runCheck(event: PlayerMoveEvent){
        if(event.from.blockX == event.to.blockX && event.from.blockY == event.to.blockY && event.from.blockZ == event.to.blockZ)
            return

        val player = event.player
        val ingeniaPlayer = IngeniaPlayer(player)

        if(!(player.hasPermission("ingenia.admin") || player.isOp) && (event.to.blockX >= 500 || event.to.blockX <= -500 ||
                    event.to.blockY >= 319 || event.to.blockY <= -64 ||
                    event.to.blockZ >= 500 || event.to.blockZ <= -500)){

            player.spawnParticle(Particle.BLOCK_MARKER, player.eyeLocation, 1, 0.5, 0.5, 0.5, 0.0, Material.BARRIER.createBlockData())
            event.isCancelled = true
        }

        // Won't run area check when the player is in a physical game
        if(ingeniaPlayer.isInGame)
            return

        // Won't run area check when the player is inside a vehicle from TrainCarts
        if(player.isInsideVehicle){
            if(player.vehicle is MinecartMember<*>) return
        }

        var currentArea: Area? = null

        for(area in AreaUtils.getAllAreas()){
            if(area.area != null) {
                if (area.area!!.contains(event.to.blockX, event.to.blockZ) &&
                    event.to.blockY in area.minY..area.maxY
                ) {
                    if (currentArea == null)
                        currentArea = area
                    else {
                        if (area.weight > currentArea.weight)
                            currentArea = area
                    }
                }
            }
        }

        val client = ClientApi.getInstance().getClient(player.uniqueId)!!

        if(currentArea == null){
            ingeniaPlayer.currentAreaName = null
            MediaApi.getInstance().stopFor(client)
            return
        }

        //Check if player's saved area is null
        if(ingeniaPlayer.currentAreaName != null)
        //If current player area and move area are the same, code has to stop
            if(ingeniaPlayer.currentAreaName == "${currentArea.category}/${currentArea.name}")
                return

        AreaUtils.getAllAreasInFormat().forEach {
            MediaApi.getInstance().stopFor("${it}_music", client)
        }

        (player as Audience).sendActionBar(MiniMessage.miniMessage().deserialize("<${MessageType.PLAYER_UPDATE}>${currentArea.displayName}"))
        ingeniaPlayer.currentAreaName = "${currentArea.category}/${currentArea.name}"

        val music = currentArea.getMusic()
        if(music != null) {
            val media = Media(music)
            media.mediaId = "${ingeniaPlayer.currentAreaName}_music"
            media.isDoPickup = true
            media.volume = 100
            media.fadeTime = 2
            media.isLoop = true
            media.keepTimeout = 3600000
            //media.startAtMillis = AreaAudioManager.getStartMillis(area = currentArea).toInt()
            media.startInstant = AreaAudioManager.getStartTime()
            MediaApi.getInstance().playFor(media, client)
        }
    }


}