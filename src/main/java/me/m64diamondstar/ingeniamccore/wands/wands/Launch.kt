package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.wands.utils.Cooldowns
import me.m64diamondstar.ingeniamccore.wands.utils.Wand
import org.bukkit.Particle
import org.bukkit.entity.Player

class Launch: Wand {

    override fun getDisplayName(): String{
        return Colors.format("#247287&lL#338094&la#428fa1&lu#529dae&ln#61abbb&lc#70bac9&lh" +
                " #7fc8d6&lW#8fd6e3&la#9ee5f0&ln#adf3fd&ld")
    }

    override fun getCustomModelData(): Int {
        return 1
    }

    override fun hasPermission(player: Player): Boolean {
        return player.hasPermission("ingeniawands.launch")
    }

    override fun run(player: Player) {
        player.velocity = player.location.direction.multiply(2.5)
        player.world.spawnParticle(Particle.REVERSE_PORTAL, player.location, 300, 0.0, 0.0, 0.0)
        Cooldowns.addPlayer(player, 0L, 3000L, 4000L, 6000L)
    }
}