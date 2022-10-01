package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Fly {

    public Fly(Player player){
        player.setGravity(false);
        final int schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
            player.setVelocity(player.getLocation().getDirection().multiply(0.5));
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 3, 0D, 0D, 0D, 0D);
        }, 0L, 1L);


        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            Bukkit.getScheduler().cancelTask(schedule);
            player.setGravity(true);
        }, 160L);

        Cooldowns.addPlayer(player, 8000L, 9000L, 12000L, 15000L);
    }

}
