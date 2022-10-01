package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Music {

    public Music(Player player){
        player.getWorld().spawnParticle(Particle.NOTE, player.getLocation(), 50, 2, 1, 2);
        ItemStack disc = new ItemStack(Material.MUSIC_DISC_CAT);



        ArmorStand db = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0,2.5,0), EntityType.ARMOR_STAND);
        db.setVisible(false);
        Objects.requireNonNull(db.getEquipment()).setHelmet(new ItemStack(Material.RED_STAINED_GLASS));
        db.setSilent(true);
        db.setGravity(false);


        final int schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> db.teleport(player.getLocation().add(0,2.5,0)), 0L, 1L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.RED_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 10L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.ORANGE_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 20L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.YELLOW_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 30L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.LIME_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 40L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.GREEN_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 50L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.BLUE_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 60L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 70L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.PINK_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 80L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.MAGENTA_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 90L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.PURPLE_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 100L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.getEquipment().setHelmet(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS));
            db.getWorld().spawnParticle(Particle.NOTE, db.getLocation().add(0,-3,0), 50, 2, 1, 2);
            Item dropitem = player.getWorld().dropItem(player.getLocation().add(0,2,0), disc);
            dropitem.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), dropitem::remove, 20L);
        }, 110L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            db.remove();
            db.getWorld().spawnParticle(Particle.CLOUD, db.getLocation().add(0,1.5,0), 10, 0, 0, 0, 0D);
            Bukkit.getScheduler().cancelTask(schedule);
        }, 120L);


        Cooldowns.addPlayer(player, 6000L, 7000L, 8000L, 10000L);
    }

}
