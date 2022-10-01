package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Utils.Colors;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import me.m64diamondstar.ingeniamccore.Wands.WandListener.WandListener;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Water {

    public Water(Player player){
        if(!((CraftPlayer) player).isOnGround()){
            player.sendMessage(Colors.format("&cYou can only use this wand on the ground!"));
            return;
        }

        WandListener.gliding.add(player);

        Location loc1 = player.getLocation().add(3,0,0);
        Location loc2 = player.getLocation().add(0,0,3);
        Location loc3 = player.getLocation().add(-3,0,0);
        Location loc4 = player.getLocation().add(0,0,-3);

        float walkspeed = player.getWalkSpeed();
        float flyspeed = player.getFlySpeed();

        player.setWalkSpeed(0f);
        player.setFlySpeed(0f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 50, 250, false));

        new BukkitRunnable(){

            long c = 0;

            @Override
            public void run() {

                if(c == 40){
                    this.cancel();
                    return;
                }

                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc1, 40, 0.1, 0.1, 0.1, 0D);
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc2, 40, 0.1, 0.1, 0.1, 0D);
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc3, 40, 0.1, 0.1, 0.1, 0D);
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc4, 40, 0.1, 0.1, 0.1, 0D);

                loc1.add(0,0.05,0);
                loc2.add(0,0.05,0);
                loc3.add(0,0.05,0);
                loc4.add(0,0.05,0);

                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

        new BukkitRunnable(){

            long c = 0;

            @Override
            public void run() {

                if(c == 10){
                    this.cancel();
                    return;
                }



                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc1, 40, 0.02, 0.02, 0.02, 0D);
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc2, 40, 0.02, 0.02, 0.02, 0D);
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc3, 40, 0.02, 0.02, 0.02, 0D);
                player.getWorld().spawnParticle(Particle.FALLING_WATER, loc4, 40, 0.02, 0.02, 0.02, 0D);

                loc1.add(-0.3,-0.1,0);
                loc2.add(0,-0.1,-0.3);
                loc3.add(0.3,-0.1,0);
                loc4.add(0,-0.1,0.3);

                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 40L, 1L);


        new BukkitRunnable(){

            public void run(){
                player.setGliding(true);
                player.setGravity(false);

                player.setWalkSpeed(walkspeed);
                player.setFlySpeed(flyspeed);
            }

        }.runTaskLater(Main.getPlugin(Main.class), 50L);

        new BukkitRunnable(){

            long c = 0;

            @Override
            public void run() {

                if(c == 100){
                    this.cancel();
                    WandListener.gliding.remove(player);
                    if(!player.hasGravity())
                        player.setGravity(true);
                    if(player.isGliding())
                        player.setGliding(false);
                    return;
                }

                player.getWorld().spawnParticle(Particle.WATER_SPLASH, player.getLocation(), 60, 0.2, 0.2, 0.2, 0D);
                player.setVelocity(player.getEyeLocation().getDirection().multiply(0.75));


                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 50L, 1L);




        Cooldowns.addPlayer(player, 7500L, 8500L, 9500L, 12000L);
    }

}
