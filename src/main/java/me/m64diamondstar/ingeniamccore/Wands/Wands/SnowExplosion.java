package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class SnowExplosion {

    public SnowExplosion(Player player){

        FallingBlock fb = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Material.SNOW_BLOCK.createBlockData());

        fb.setVelocity(player.getLocation().getDirection().multiply(1.5));
        fb.setDropItem(false);
        fb.setCustomName("SEWand");

        Cooldowns.addPlayer(player, 0L, 500L, 1500L, 4000L);

    }

}
