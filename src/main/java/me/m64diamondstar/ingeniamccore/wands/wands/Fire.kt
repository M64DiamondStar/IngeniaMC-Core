package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Fire {

    public Fire(Player player){
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            Location location1 = player.getEyeLocation();
            int particles = 50;
            float radius = 0.7f;
            for (int i = 0; i < particles; i++) {
                double angle, x, z;
                angle = 2 * Math.PI * i / particles;
                x = Math.cos(angle) * radius;
                z = Math.sin(angle) * radius;
                location1.add(x, 0, z);
                player.getWorld().spawnParticle(Particle.FLAME, location1, 1, 0, 0, 0, 0D);
                location1.subtract(x, 0, z);
            }
        }, 3L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            Location location2 = player.getEyeLocation();
            int particles = 50;
            float radius = 0.7f;
            for (int i = 0; i < particles; i++) {
                double angle, x, z;
                angle = 2 * Math.PI * i / particles;
                x = Math.cos(angle) * radius;
                z = Math.sin(angle) * radius;
                location2.add(x, -0.66, z);
                player.getWorld().spawnParticle(Particle.FLAME, location2, 1, 0, 0, 0, 0D);
                location2.subtract(x, -0.66, z);
            }
        }, 5L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            Location location3 = player.getEyeLocation();
            int particles = 50;
            float radius = 0.7f;
            for (int i = 0; i < particles; i++) {
                double angle, x, z;
                angle = 2 * Math.PI * i / particles;
                x = Math.cos(angle) * radius;
                z = Math.sin(angle) * radius;
                location3.add(x, -1.33, z);
                player.getWorld().spawnParticle(Particle.FLAME, location3, 1, 0, 0, 0, 0D);
                location3.subtract(x, -1.33, z);
            }
        }, 8L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            Block block = player.getTargetBlock(null, 21);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 150, 0.35, 0.7, 0.35, 0D);
            player.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 30, 0.2, 0.7, 0.2, 0D);
            player.teleport(block.getLocation().setDirection(player.getLocation().getDirection()));
            player.teleport(player.getLocation().add(player.getLocation().getDirection().normalize().multiply(-1)));
            player.teleport(player.getLocation().add(0.5,0,0.5));
            if(player.getLocation().getBlock().getType() != Material.AIR)player.teleport(player.getLocation().add(0,1,0));
            player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 100, 0.3, 0.5, 0.3, 0D);
            player.getWorld().spawnParticle(Particle.LAVA, player.getLocation().add(0,1,0), 30, 0.3, 0.5, 0.3, 0D);
        }, 10L);


        Cooldowns.addPlayer(player, 0L, 3000L, 4000L, 6000L);
    }

}
