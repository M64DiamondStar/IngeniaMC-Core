package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Waterlogged
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.scheduler.BukkitRunnable

class SneakListener: Listener {

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent){
        val player = event.player

        if(!event.isSneaking) return
        if(!SplashBattleUtils.players.contains(player)) return
        if(!isPlayerLookingAtWater(player)) return

        object: BukkitRunnable(){

            override fun run() {

                if(!player.isSneaking || !isPlayerLookingAtWater(player)) {
                    this.cancel()
                    return
                }

                if(SplashBattleUtils.getAmmo(player) >= 30){
                    player.sendMessage(Colors.format("#3671baYour buckets are full. Time to shoot!"))
                    this.cancel()
                    return
                }

                SplashBattleUtils.addAmmo(player, 1)

            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 2L)

    }

    private fun isPlayerLookingAtWater(player: Player): Boolean {
        val targetBlock = player.getTargetBlock(null, 5)

        if(targetBlock.type == Material.WATER || targetBlock.type == Material.SEAGRASS
            || targetBlock.type == Material.TALL_SEAGRASS || targetBlock.type == Material.KELP
            || targetBlock.type == Material.KELP_PLANT) return true

        val blockData: BlockData = targetBlock.blockData
        if (blockData is Waterlogged)
            if (blockData.isWaterlogged) return true


        return false
    }

}