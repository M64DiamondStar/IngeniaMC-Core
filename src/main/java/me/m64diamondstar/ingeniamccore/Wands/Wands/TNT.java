package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Wands.Cooldowns;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class TNT {

    public TNT(Player player){
        FallingBlock fb = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Material.TNT.createBlockData());

        fb.setVelocity(player.getLocation().getDirection().multiply(1.5));
        fb.setDropItem(false);
        fb.setCustomName("TnTWand");

        Cooldowns.addPlayer(player, 0L, 1000L, 3000L, 8000L);
    }

}
