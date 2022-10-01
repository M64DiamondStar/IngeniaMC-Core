package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import me.m64diamondstar.ingeniamccore.Wands.WandListener.WandListener;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Air {

    public Air(Player player){
        player.setVelocity(new Vector(0,1,0));
        player.getWorld().spawnParticle(Particle.SPELL, player.getLocation(), 100, 0.5, 0.5, 0.5, 0.01D);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            player.setVelocity(player.getLocation().getDirection().multiply(1.4));
            WandListener.gliding.add(player);
            player.setGliding(true);
        }, 15L);

        new BukkitRunnable(){

            int c = 0;

            @Override
            public void run() {


                if(!((CraftPlayer) player).isOnGround())
                    player.getWorld().spawnParticle(Particle.SPELL, player.getLocation(), 30, 0.1, 0.1, 0.1, 0D);

                if(c == 300 || ((CraftPlayer) player).isOnGround()){
                    WandListener.gliding.remove(player);

                    this.cancel();
                    return;
                }

                c++;

            }
        }.runTaskTimer(Main.getPlugin(Main.class), 16L, 1L);


        Cooldowns.addPlayer(player, 15000L, 15000L, 18000L, 21000L);
    }

}
