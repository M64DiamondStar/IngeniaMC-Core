package me.m64diamondstar.ingeniamccore.games.presenthunt.listeners

import me.m64diamondstar.ingeniamccore.games.presenthunt.PresentHuntUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.wands.SnowCannon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

class PlayerInteractListener: Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.clickedBlock!!.type != Material.PLAYER_HEAD) return
        if (!PresentHuntUtils.containsActivePresent(event.clickedBlock!!.location)) return

        val player = event.player
        val location = event.clickedBlock!!.location
        PresentHuntUtils.getActivePresent(location = location)?.spawnRandomPresent()

        PresentHuntUtils.removeActivePresent(location = location)

        val randomExp = Random.nextInt(3, 9)
        val randomStars = Random.nextInt(2, 5)
        val randomGolden = Random.nextInt(0, 400)

        var totalExp = randomExp
        var totalStars = randomStars

        player.sendMessage(Colors.format(MessageType.SUCCESS + "You found a present!"))
        player.sendMessage(Colors.format(MessageType.SUCCESS + "You gained $randomExp exp and $randomStars:gs:!"))

        if (randomGolden <= 2) { //Extra balance & exp
            val randomExtra = Random.nextInt(100, 150)
            player.sendMessage(Colors.format(MessageType.INGENIA + "Wait, what is that? You found $randomExtra extra exp and $randomExtra:gs:!"))

            totalExp += randomExtra
            totalStars += randomExtra
        }

        if(randomGolden == 3 && !player.hasPermission("ingeniawands.snowcannon")){
            player.sendMessage(Colors.format(MessageType.INGENIA + "Wow! You just unlocked " + SnowCannon().getDisplayName() +
                    "&r${MessageType.INGENIA} from this present!"))
            IngeniaPlayer(player).givePermission("ingeniawands.snowcannon")
        }

        IngeniaPlayer(player).addExp(totalExp.toLong()) // Add total exp (maybe affected by random extra
        IngeniaPlayer(player).addBal(totalStars.toLong()) // Add total gs (maybe affected by random extra)

        player.world.spawnParticle(
            Particle.SNOWFLAKE, location.add(0.5, 0.35, 0.5),
            300, 0.15, 0.15, 0.15, 0.0
        )
        location.block.type = Material.AIR
    }

}