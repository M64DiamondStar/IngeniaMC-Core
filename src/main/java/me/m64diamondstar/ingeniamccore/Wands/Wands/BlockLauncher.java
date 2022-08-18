package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class BlockLauncher {

    public BlockLauncher(Player player){
        Location loc = player.getLocation().add(0,-1,0);

        Block block = loc.getBlock();

        FallingBlock fallingblock = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), block.getBlockData());
        fallingblock.setVelocity(player.getLocation().add(0,1,0).getDirection().multiply(1.5));
        fallingblock.setCustomName("FallingBlockWand");
        fallingblock.setDropItem(false);

        Cooldowns.addPlayer(player, 0L, 500L, 1500L, 3000L);
    }

}
