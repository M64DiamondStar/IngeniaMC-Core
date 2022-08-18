package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

public class SnowCannon {

    public SnowCannon(Player player){
        Snowball snowball = player.launchProjectile(Snowball.class, player.getLocation().getDirection());

        new BukkitRunnable(){

            public void run(){
                player.getWorld().spawnParticle(Particle.REDSTONE, snowball.getLocation(), 2, 0, 0, 0, 0D, new Particle.DustOptions(Color.WHITE, 1));
                if(snowball.isDead()) this.cancel();
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);
    }

}
