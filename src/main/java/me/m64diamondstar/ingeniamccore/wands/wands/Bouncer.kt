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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bouncer {

    private List<ArmorStand> stands1 = new ArrayList<>();
    private List<ArmorStand> stands2 = new ArrayList<>();

    private int y = 0;

    public Bouncer(Player player){
        player.addPotionEffect(PotionEffectType.JUMP.createEffect(200, 5));

        for(int i = 0; i < 2; i++){
            Location loc = player.getLocation().add(0,-0.5,0);
            loc.setYaw(i * 180);
            ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setRightArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(90), Math.toRadians(90)));
            Objects.requireNonNull(armorStand.getEquipment()).setItemInMainHand(new ItemStack(Material.GREEN_STAINED_GLASS));
            stands1.add(armorStand);
        }

        for(int i = 0; i < 2; i++){
            Location loc = player.getLocation().add(0,-0.5,0);
            loc.setYaw(i * 180 - 90);
            ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setRightArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(90), Math.toRadians(90)));
            Objects.requireNonNull(armorStand.getEquipment()).setItemInMainHand(new ItemStack(Material.LIME_STAINED_GLASS));
            stands2.add(armorStand);
        }



        int s = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {

            for(ArmorStand stand : stands1){
                Location loc = player.getLocation().add(0,-0.5 +
                                (Math.sin((double) y / 18 * Math.PI) / 2)
                        ,0);
                loc.setYaw(stand.getLocation().getYaw() + 10);
                stand.teleport(loc);
            }

            for(ArmorStand stand : stands2){
                Location loc = player.getLocation().add(0,-0.5 +
                                (Math.cos(((double) y + 9) / 18 * Math.PI) / 2)
                        ,0);
                loc.setYaw(stand.getLocation().getYaw() + 10);
                stand.teleport(loc);
            }

            player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 20, 0.3, 0, 0.3, 0D, Material.SLIME_BLOCK.createBlockData());
            y++;
        }, 0L, 1L);



        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            Bukkit.getScheduler().cancelTask(s);

            for(ArmorStand stand : stands1)
                stand.remove();
            for(ArmorStand stand : stands2)
                stand.remove();

        }, 200L);


        Cooldowns.addPlayer(player, 10000L, 11000L, 13000L, 15000L);

    }

}
