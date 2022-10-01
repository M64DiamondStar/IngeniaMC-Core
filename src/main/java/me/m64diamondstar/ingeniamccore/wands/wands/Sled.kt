package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class Sled {


    public Sled(Player player){

        if(!((CraftPlayer) player).isOnGround()){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou can only use this on the ground!")));
            return;
        }

        Location spawnLoc = player.getLocation();
        spawnLoc.add(0, -1.6, 0);
        spawnLoc.setYaw(0);

        ArmorStand sled = (ArmorStand) player.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);
        sled.setGravity(false);
        sled.setInvisible(true);
        sled.setInvulnerable(true);

        spawnLoc.add(0, 0.3, 0);

        ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);
        seat.setGravity(false);
        seat.setInvisible(true);
        seat.setInvulnerable(true);
        seat.addPassenger(player);
        seat.getLocation().setYaw(0);

        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(8);
        item.setItemMeta(meta);

        Objects.requireNonNull(sled.getEquipment()).setHelmet(item);

        if(player.getLocation().getYaw() >= -45 && player.getLocation().getYaw() < 45){
            south(sled, seat, player);
            sled.setHeadPose(new EulerAngle(0, Math.toRadians(0), 0));
        }else if(player.getLocation().getYaw() >= 45 && player.getLocation().getYaw() < 135){
            west(sled, seat, player);
            sled.setHeadPose(new EulerAngle(0, Math.toRadians(90), 0));
        }else if(player.getLocation().getYaw() >= 135 || player.getLocation().getYaw() < -135){
            north(sled, seat, player);
            sled.setHeadPose(new EulerAngle(0, Math.toRadians(180), 0));
        }else if(player.getLocation().getYaw() >= -135 && player.getLocation().getYaw() < -45){
            east(sled, seat, player);
            sled.setHeadPose(new EulerAngle(0, Math.toRadians(-90), 0));
        }


        Cooldowns.addPlayer(player, 4000L, 7000L, 9000L, 12000L);

    }





    private void south(ArmorStand sled, ArmorStand seat, Player player){

        new BukkitRunnable(){

            int c = 0;

            public void run(){


                if(player.getLocation().add(0,1,1).getBlock().getType() != Material.AIR || player.getLocation().add(0,2,1).getBlock().getType() != Material.AIR){
                    if(!player.getLocation().add(0,1,1).getBlock().getType().toString().contains("BUTTON")
                            && !player.getLocation().add(0,1,1).getBlock().getType().toString().contains("PRESSURE_PLATE")) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!")));
                        sled.remove();
                        seat.remove();
                        player.teleport(player.getLocation().add(0, 0.5, 0));
                        this.cancel();
                        return;
                    }
                }


                //After 15 seconds
                if(c == 35){

                    pillar(player);
                    this.cancel();
                    seat.remove();
                    sled.removePassenger(player);
                    sled.setGravity(true);

                    Vec3D vec3D = new Vec3D(0, 2, 0);
                    ((CraftPlayer)player).getHandle().a(EnumMoveType.e, vec3D);
                    ((CraftEntity)sled).getHandle().a(EnumMoveType.e, vec3D);

                    player.setVelocity(new Vector(0,0.8,2));
                    sled.setVelocity(new Vector(0,0.8,2));

                    new BukkitRunnable(){

                        int c2 = 0;

                        public void run(){
                            if(sled.isOnGround()) {
                                sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                                sled.remove();
                                this.cancel();
                                return;
                            }
                            sled.setHeadPose(new EulerAngle(Math.toRadians((double) c2 * 1.5), 0, 0));
                            c2++;
                        }

                    }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                        if(!sled.isDead()) {
                            sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                            sled.remove();
                        }
                    }, 60L);

                    return;

                }


                Vec3D vec3D = new Vec3D(0, 0, (double) c / 90);

                ((CraftEntity) sled).getHandle().a(EnumMoveType.e, vec3D);
                ((CraftEntity) seat).getHandle().a(EnumMoveType.e, vec3D);

                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0, 0.3,0), 30, 0.3, 0, 0.3, 0D, Material.SNOW_BLOCK.createBlockData());

                ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(new Random().nextDouble() - 0.5,-1.6,0), EntityType.ARMOR_STAND);
                as.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15)));
                as.setGravity(false);
                as.setInvisible(true);
                Objects.requireNonNull(as.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), as::remove, 25L);

                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);
    }
















    private void west(ArmorStand sled, ArmorStand seat, Player player){

        new BukkitRunnable(){

            int c = 0;

            public void run(){


                if(player.getLocation().add(-1,1,0).getBlock().getType() != Material.AIR || player.getLocation().add(-1,2,0).getBlock().getType() != Material.AIR){
                    if(!player.getLocation().add(-1,1,0).getBlock().getType().toString().contains("BUTTON")
                            && !player.getLocation().add(-1,1,0).getBlock().getType().toString().contains("PRESSURE_PLATE")) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!")));
                        sled.remove();
                        seat.remove();
                        player.teleport(player.getLocation().add(0, 0.5, 0));
                        this.cancel();
                        return;
                    }
                }


                //After 15 seconds
                if(c == 35){

                    pillar(player);
                    this.cancel();
                    seat.remove();
                    sled.removePassenger(player);
                    sled.setGravity(true);

                    Vec3D vec3D = new Vec3D(0, 2, 0);
                    ((CraftPlayer)player).getHandle().a(EnumMoveType.e, vec3D);
                    ((CraftEntity)sled).getHandle().a(EnumMoveType.e, vec3D);

                    player.setVelocity(new Vector(-2,0.8,0));
                    sled.setVelocity(new Vector(-2,0.8,0));

                    new BukkitRunnable(){

                        int c2 = 0;

                        public void run(){
                            if(sled.isOnGround()) {
                                sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                                sled.remove();
                                this.cancel();
                                return;
                            }
                            sled.setHeadPose(new EulerAngle(Math.toRadians((double) c2 * 1.5), Math.toRadians(90), 0));
                            c2++;
                        }

                    }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                        if(!sled.isDead()) {
                            sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                            sled.remove();
                        }
                    }, 60L);

                    return;

                }


                Vec3D vec3D = new Vec3D(-((double) c / 90), 0, 0);

                ((CraftEntity) sled).getHandle().a(EnumMoveType.e, vec3D);
                ((CraftEntity) seat).getHandle().a(EnumMoveType.e, vec3D);

                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0, 0.3,0), 30, 0.3, 0, 0.3, 0D, Material.SNOW_BLOCK.createBlockData());

                ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0,-1.6,new Random().nextDouble() - 0.5), EntityType.ARMOR_STAND);
                as.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15)));
                as.setGravity(false);
                as.setInvisible(true);
                Objects.requireNonNull(as.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), as::remove, 25L);

                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

    }























    private void north(ArmorStand sled, ArmorStand seat, Player player){

        new BukkitRunnable(){

            int c = 0;

            public void run(){


                if(player.getLocation().add(0,1,-1).getBlock().getType() != Material.AIR || player.getLocation().add(0,2,-1).getBlock().getType() != Material.AIR){
                    if(!player.getLocation().add(0,1,-1).getBlock().getType().toString().contains("BUTTON")
                            && !player.getLocation().add(0,1,-1).getBlock().getType().toString().contains("PRESSURE_PLATE")) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!")));
                        sled.remove();
                        seat.remove();
                        player.teleport(player.getLocation().add(0, 0.5, 0));
                        this.cancel();
                        return;
                    }
                }


                //After 15 seconds
                if(c == 35){

                    pillar(player);
                    this.cancel();
                    seat.remove();
                    sled.removePassenger(player);
                    sled.setGravity(true);

                    Vec3D vec3D = new Vec3D(0, 2, 0);
                    ((CraftPlayer)player).getHandle().a(EnumMoveType.e, vec3D);
                    ((CraftEntity)sled).getHandle().a(EnumMoveType.e, vec3D);

                    player.setVelocity(new Vector(0,0.8,-2));
                    sled.setVelocity(new Vector(0,0.8,-2));

                    new BukkitRunnable(){

                        int c2 = 0;

                        public void run(){
                            if(sled.isOnGround()) {
                                sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                                sled.remove();
                                this.cancel();
                                return;
                            }
                            sled.setHeadPose(new EulerAngle(Math.toRadians((double) c2 * 1.5), Math.toRadians(180), 0));
                            c2++;
                        }

                    }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                        if(!sled.isDead()) {
                            sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                            sled.remove();
                        }
                    }, 60L);

                    return;

                }


                Vec3D vec3D = new Vec3D(0, 0, -((double) c / 90));

                ((CraftEntity) sled).getHandle().a(EnumMoveType.e, vec3D);
                ((CraftEntity) seat).getHandle().a(EnumMoveType.e, vec3D);

                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0, 0.3,0), 30, 0.3, 0, 0.3, 0D, Material.SNOW_BLOCK.createBlockData());

                ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(new Random().nextDouble() - 0.5,-1.6,0), EntityType.ARMOR_STAND);
                as.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15)));
                as.setGravity(false);
                as.setInvisible(true);
                Objects.requireNonNull(as.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), as::remove, 25L);

                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);
    }




















    private void east(ArmorStand sled, ArmorStand seat, Player player){

        new BukkitRunnable(){

            int c = 0;

            public void run(){


                if(player.getLocation().add(1,1,0).getBlock().getType() != Material.AIR || player.getLocation().add(1,2,0).getBlock().getType() != Material.AIR){
                    if(!player.getLocation().add(1,1,0).getBlock().getType().toString().contains("BUTTON")
                            && !player.getLocation().add(1,1,0).getBlock().getType().toString().contains("PRESSURE_PLATE")) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!")));
                        sled.remove();
                        seat.remove();
                        player.teleport(player.getLocation().add(0, 0.5, 0));
                        this.cancel();
                        return;
                    }
                }


                //After 15 seconds
                if(c == 35){

                    pillar(player);
                    this.cancel();
                    seat.remove();
                    sled.removePassenger(player);
                    sled.setGravity(true);

                    Vec3D vec3D = new Vec3D(0, 2, 0);
                    ((CraftPlayer)player).getHandle().a(EnumMoveType.e, vec3D);
                    ((CraftEntity)sled).getHandle().a(EnumMoveType.e, vec3D);

                    player.setVelocity(new Vector(2,0.8,0));
                    sled.setVelocity(new Vector(2,0.8,0));

                    new BukkitRunnable(){

                        int c2 = 0;

                        public void run(){
                            if(sled.isOnGround()) {
                                sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                                sled.remove();
                                this.cancel();
                                return;
                            }
                            sled.setHeadPose(new EulerAngle(Math.toRadians((double) c2 * 1.5), Math.toRadians(-90), 0));
                            c2++;
                        }

                    }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                        if(!sled.isDead()) {
                            sled.getWorld().spawnParticle(Particle.CLOUD, sled.getLocation().add(0,1.4,0), 100, 0.3, 0.3, 0.3, 0D);
                            sled.remove();
                        }
                    }, 60L);

                    return;

                }


                Vec3D vec3D = new Vec3D((double) c / 90, 0, 0);

                ((CraftEntity) sled).getHandle().a(EnumMoveType.e, vec3D);
                ((CraftEntity) seat).getHandle().a(EnumMoveType.e, vec3D);

                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0, 0.3,0), 30, 0.3, 0, 0.3, 0D, Material.SNOW_BLOCK.createBlockData());

                ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0,-1.6,new Random().nextDouble() - 0.5), EntityType.ARMOR_STAND);
                as.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15),Math.toRadians(Math.random() * (15 + 15) - 15)));
                as.setGravity(false);
                as.setInvisible(true);
                Objects.requireNonNull(as.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), as::remove, 25L);

                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

    }



















    private void pillar(Player player){

        for(int i = 0; i < 4; i++) {

            ArmorStand s1 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0.4, -3 + (i * 0.6), 0.4), EntityType.ARMOR_STAND);
            s1.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15)));
            s1.setGravity(false);
            s1.setInvisible(true);
            Objects.requireNonNull(s1.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));

            ArmorStand s2 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(-0.4, -3 + (i * 0.6), -0.4), EntityType.ARMOR_STAND);
            s2.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15)));
            s2.setGravity(false);
            s2.setInvisible(true);
            Objects.requireNonNull(s2.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));

            ArmorStand s3 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0.4, -3 + (i * 0.6), -0.4), EntityType.ARMOR_STAND);
            s3.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15)));
            s3.setGravity(false);
            s3.setInvisible(true);
            Objects.requireNonNull(s3.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));

            ArmorStand s4 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(-0.4, -3 + (i * 0.6), 0.4), EntityType.ARMOR_STAND);
            s4.setHeadPose(new EulerAngle(Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15), Math.toRadians(Math.random() * (15 + 15) - 15)));
            s4.setGravity(false);
            s4.setInvisible(true);
            Objects.requireNonNull(s4.getEquipment()).setHelmet(new ItemStack(Material.SNOW_BLOCK));



            new BukkitRunnable(){

                int c = 0;

                public void run(){

                    Vec3D vec3D = new Vec3D(0, 0.35, 0);

                    if(c <= 5) {
                        ((CraftEntity) s1).getHandle().a(EnumMoveType.e, vec3D);
                        ((CraftEntity) s2).getHandle().a(EnumMoveType.e, vec3D);
                        ((CraftEntity) s3).getHandle().a(EnumMoveType.e, vec3D);
                        ((CraftEntity) s4).getHandle().a(EnumMoveType.e, vec3D);
                    }


                    if(c > 40){
                        vec3D = new Vec3D(0, -0.15, 0);

                        ((CraftEntity) s1).getHandle().a(EnumMoveType.e, vec3D);
                        ((CraftEntity) s2).getHandle().a(EnumMoveType.e, vec3D);
                        ((CraftEntity) s3).getHandle().a(EnumMoveType.e, vec3D);
                        ((CraftEntity) s4).getHandle().a(EnumMoveType.e, vec3D);
                    }

                    if(c == 80){
                        this.cancel();
                        s1.remove();
                        s2.remove();
                        s3.remove();
                        s4.remove();
                        return;
                    }

                    c++;
                }

            }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);


        }
    }
}
