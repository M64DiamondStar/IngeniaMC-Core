package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Bush {

    public Bush(Player player){
        ItemStack helmet = Objects.requireNonNull(player.getEquipment()).getHelmet();
        for(int i = 0; i < 65; i++) {
            ArmorStand as1 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(Math.random() * (1.5 + 1.5) - 1.5, Math.random() * (-0.3 + 1.6) - 2, Math.random() * (1.5 + 1.5) - 1.5), EntityType.ARMOR_STAND);
            Objects.requireNonNull(as1.getEquipment()).setHelmet(new ItemStack(Material.OAK_LEAVES));
            as1.setVisible(false);
            as1.setGravity(false);
            as1.setCustomName("IngeniaWandsBush" + player.getUniqueId());
            player.getEquipment().setHelmet(new ItemStack(Material.OAK_LEAVES));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                as1.remove();
                as1.getWorld().spawnParticle(Particle.CLOUD, as1.getLocation().add(0,1.5,0), 3, 0, 0, 0, 0D);
                if(player.getEquipment().getHelmet() != null) {
                    player.getEquipment().setHelmet(helmet);
                }else player.getEquipment().setHelmet(new ItemStack(Material.AIR));
            }, 100L);
        }

        Cooldowns.addPlayer(player, 0L, 10000L, 12000L, 15000L);
    }

}
