package me.m64diamondstar.ingeniamccore.wands.wandlistener;

import me.m64diamondstar.ingeniamccore.IngeniaMC;
import me.m64diamondstar.ingeniamccore.wands.Cooldowns;
import me.m64diamondstar.ingeniamccore.wands.wands.*;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WandListener implements Listener {

    @EventHandler
    public void onUseWand(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD) &&
                    Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).hasCustomModelData() &&
            !Cooldowns.isOnCooldown(player)) {
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 1)
                    new Launch(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 2)
                    new Fly(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 3)
                    new SnowCannon(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 4)
                    new Music(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 5)
                    new BlockLauncher(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 6)
                    new HolyTomato(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 7)
                    new Happiness(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 8)
                    new TNT(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 9)
                    new Bush(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 10)
                    new Cloak(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 11)
                    new Water(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 12)
                    new Air(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 13)
                    new Fire(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 14)
                    new Earth(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 15)
                    new Speed(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 16)
                    new AntiGravity(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 17)
                    new Bouncer(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 18)
                    new SnowExplosion(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 19)
                    new Sled(player).run();
                if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 20)
                    new Grapple(player).run();
            }
        }
    }




    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent e) {
        Entity entity = e.getEntity();
        if(entity instanceof FallingBlock) {
            if(!(entity.getCustomName() == null)) {
                switch (entity.getCustomName()) {
                    case "FallingBlockWand" -> {
                        entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 20);
                        e.setCancelled(true);
                    }
                    case "TnTWand" -> {
                        entity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 10);
                        Location loc = entity.getLocation().add(0, -1, 0);
                        Block block = loc.getBlock();
                        e.setCancelled(true);
                        for (int i = 0; i < 6; i++) {
                            FallingBlock fb = entity.getWorld().spawnFallingBlock(entity.getLocation(), block.getBlockData());
                            fb.setVelocity(new Vector(Math.random() * (0.2 + 0.2) - 0.2, Math.random() * (0.6 - 0.85) + 0.85, Math.random() * (0.2 + 0.2) - 0.2));
                            fb.setDropItem(false);
                            fb.setCustomName("TnTWandBlock");
                        }
                    }
                    case "SEWand" -> {
                        entity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 10);
                        e.setCancelled(true);
                        for (int i = 0; i < 20; i++) {
                            Snowball sb = entity.getWorld().spawn(entity.getLocation(), Snowball.class);
                            sb.setVelocity(new Vector(Math.random() * (0.2 + 0.2) - 0.2, Math.random() * (0.6 - 0.85) + 0.85, Math.random() * (0.2 + 0.2) - 0.2));
                            new BukkitRunnable() {

                                public void run() {
                                    entity.getWorld().spawnParticle(Particle.REDSTONE,
                                            sb.getLocation(), 2, 0, 0, 0, 0D, new Particle.DustOptions(Color.WHITE, 1));
                                    if (sb.isDead()) this.cancel();
                                }

                            }.runTaskTimer(IngeniaMC.getPlugin(IngeniaMC.class), 0L, 1);
                        }
                    }
                    case "TnTWandBlock" -> e.setCancelled(true);
                    case "igwandsearthwand" -> e.setCancelled(true);
                }
            }
        }
    }



    public static List<Player> gliding = new ArrayList<>();


    @EventHandler
    public void onPlayerToggleGlide(EntityToggleGlideEvent e){
        if(!(e.getEntity() instanceof Player player)) return;

        if(gliding.contains(player))
            e.setCancelled(true);

    }



    @EventHandler()
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().equals(Material.BLAZE_ROD)) {
            if(Objects.requireNonNull(e.getItemDrop().getItemStack().getItemMeta()).hasCustomModelData()) {
                e.setCancelled(true);
            }
        }
    }

}
