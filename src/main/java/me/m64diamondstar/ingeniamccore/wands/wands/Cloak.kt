package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Cloak {

    private int c = 0;
    private Map<Player, ItemStack[]> armorInv = new HashMap<>();

    public Cloak(Player player){
        Location loc = player.getEyeLocation();
        Location nLoc = player.getLocation();
        int particles = 10;
        float radius = 0.7f;
        player.setWalkSpeed(0.0f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 200));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
        loc.add(0, -1.33, 0);
        player.teleport(nLoc);
        armorInv.put(player, player.getInventory().getArmorContents());
        int s = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {

            double angle, x, z;
            angle = 2 * Math.PI * c / particles;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            loc.add(x, 0.1, z);
            player.getWorld().spawnParticle(Particle.GLOW, loc, 10, 0.01, 0.01, 0.01, 0D);
            loc.subtract(x, 0, z);
            player.teleport(nLoc);
            c++;

        }, 0L, 1L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            Bukkit.getScheduler().cancelTask(s);
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation().add(0, 1, 0), 50, 0.3, 1, 0.3, 0D);
            player.setWalkSpeed(0.6f);
            player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(100, 1));
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
        }, 20L);

        int s2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> player.getInventory().setHeldItemSlot(8), 0L, 1L);


        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation().add(0, 1, 0), 50, 0.3, 1, 0.3, 0D);
            player.setWalkSpeed(0.2f);
            Bukkit.getScheduler().cancelTask(s2);
            player.getInventory().setHeldItemSlot(5);
            player.getInventory().setArmorContents(armorInv.get(player));
        }, 120L);


        Cooldowns.addPlayer(player, 6000L, 7000L, 9000L, 12000L);
    }
}
