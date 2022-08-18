package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AntiGravity {

    private List<ArmorStand> stands = new ArrayList<>();

    public AntiGravity(Player player){
        player.setGravity(false);
        player.setVelocity(new Vector(player.getVelocity().getX(), 0.2f, player.getVelocity().getZ()));

        for(int i = 0; i < 18; i++){
            Location loc = player.getLocation().add(0,-0.5,0);
            loc.setYaw(i * 20);
            ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setRightArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(90), Math.toRadians(90)));
            Objects.requireNonNull(armorStand.getEquipment()).setItemInMainHand(new ItemStack(Material.PINK_STAINED_GLASS));
            stands.add(armorStand);
        }

        int s = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {

            for(ArmorStand stand : stands){
                Location loc = player.getLocation().add(0,-0.5,0);
                loc.setYaw(stand.getLocation().getYaw() + 10);
                stand.teleport(loc);
            }

            player.getWorld().spawnParticle(Particle.CRIMSON_SPORE, player.getLocation(), 20, 0.3, 0, 0.3, 0D);

        }, 0L, 1L);


        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            player.setGravity(true);
            player.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(50, 1));
            Bukkit.getScheduler().cancelTask(s);

            for(ArmorStand stand : stands){
                stand.remove();
            }

        }, 100L);



        Cooldowns.addPlayer(player, 7500L, 8000L, 9500L, 12000L);
    }


}
