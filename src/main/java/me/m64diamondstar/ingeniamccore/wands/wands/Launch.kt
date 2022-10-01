package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Launch {

    public Launch(Player player){
        player.setVelocity(player.getLocation().getDirection().multiply(2.5));
        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation(), 300, 0, 0, 0);
        Cooldowns.addPlayer(player, 0L, 3000L, 4000L, 6000L);
    }

}
