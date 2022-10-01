package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Earth {

    public Earth(Player player){

        player.setVelocity(new Vector(0,1,0));
        float walkspeed = player.getWalkSpeed();


        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            player.setVelocity(new Vector(0,-2,0));
            player.setWalkSpeed(0.8f);
            player.setFallDistance(0);
        }, 15L);


        final int schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
            ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0,-1.6,0), EntityType.ARMOR_STAND);
            as.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(Math.random() * (15 + 15) - 15)));
            as.setGravity(false);
            as.setVisible(false);
            Objects.requireNonNull(as.getEquipment()).setHelmet(new ItemStack(player.getLocation().add(0,-1,0).getBlock().getType()));
            as.getWorld().spawnParticle(Particle.BLOCK_CRACK, as.getLocation().add(0,2.2,0), 30, 0, 0, 0, 0D,
                    player.getLocation().add(0,-1,0).getBlock().getBlockData());
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 200));
            player.setFallDistance(0);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), as::remove, 15L);
        }, 25L, 1L);


        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            Bukkit.getScheduler().cancelTask(schedule);
            player.setWalkSpeed(walkspeed);
        }, 125L);


        Cooldowns.addPlayer(player, 6500L, 8000L, 10000L, 14000L);
    }

}
