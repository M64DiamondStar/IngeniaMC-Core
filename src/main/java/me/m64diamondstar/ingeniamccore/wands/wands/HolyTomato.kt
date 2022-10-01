package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HolyTomato {

    public HolyTomato(Player player){
        Location loc = player.getLocation().add(0,1,0);

        ItemStack item = new ItemStack(Material.BAKED_POTATO);
        ItemMeta meta = item.getItemMeta();

        player.getWorld().spawnParticle(Particle.REDSTONE, loc, 100, 1, 1, 1, 0D, new Particle.DustOptions(Color.RED, 1));

        assert meta != null;
        meta.setCustomModelData(1);
        item.setItemMeta(meta);

        final int schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> player.getWorld().spawnParticle(Particle.REDSTONE, loc, 50, 1, 1, 1, 0D, new Particle.DustOptions(Color.RED, 1)), 0L, 10L);

        for (int i = 0; i < 15; i++) {

            Item ti = player.getWorld().dropItem(loc, item);
            ti.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                player.getWorld().spawnParticle(Particle.CLOUD, ti.getLocation(), 1, 0, 0, 0, 0D);
                ti.remove();
                Bukkit.getScheduler().cancelTask(schedule);
            }, 40L);

        }

        Cooldowns.addPlayer(player, 1000L, 3000L, 4000L, 6000L);
    }

}
