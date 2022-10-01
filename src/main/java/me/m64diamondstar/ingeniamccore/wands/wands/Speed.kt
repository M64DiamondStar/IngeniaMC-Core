package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Speed {

    public Speed(Player player){
        player.setWalkSpeed(0.5f);
        int s = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
            Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(60, 153, 176), Color.fromRGB(56, 205, 255), 1.5F);
            player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation().add(0,1,0), 30, 0.2, 0.5, 0.2, 0D, dustTransition);
        }, 0L, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            player.setWalkSpeed(0.2f);
            Bukkit.getScheduler().cancelTask(s);
        }, 100L);

        Cooldowns.addPlayer(player, 5000L, 5000L, 6000L, 8000L);
    }

}
