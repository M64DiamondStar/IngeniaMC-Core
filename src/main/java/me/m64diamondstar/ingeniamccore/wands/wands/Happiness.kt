package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Happiness {

    public Happiness(Player player){
        double maxDist = 5;
        for(Player other : Bukkit.getOnlinePlayers()) {
            if(other.getLocation().distance(player.getLocation()) <= maxDist) {
                final int schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
                    Location loc = other.getLocation().add(0,2.5,0);
                    other.getWorld().spawnParticle(Particle.HEART, loc, 2, 0.5, 0.5, 0.5, 0D);
                    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0,2,0), 5, 2, 2, 2, 0D);
                }, 0L, 3L);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> Bukkit.getScheduler().cancelTask(schedule), 80L);

            }
        }


        Cooldowns.addPlayer(player, 0L, 5000L, 6000L, 8000L);
    }

}
