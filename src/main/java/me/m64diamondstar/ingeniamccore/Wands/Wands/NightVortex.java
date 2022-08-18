package me.m64diamondstar.ingeniamccore.Wands.Wands;

import me.m64diamondstar.ingeniamccore.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

public class NightVortex {

    private Location front;
    private Location right;
    private Location back;
    private Location left;

    private final HashMap<Location, Double> frontLocations = new HashMap<>();
    private final HashMap<Location, Double> rightLocations = new HashMap<>();
    private final HashMap<Location, Double> backLocations = new HashMap<>();
    private final HashMap<Location, Double> leftLocations = new HashMap<>();

    private final Particle particle = Particle.REDSTONE;
    private final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);

    public NightVortex(Player player) {

        front = player.getLocation().add(0, 2, 0);
        right = player.getLocation().add(0, 2, 0);
        back = player.getLocation().add(0, 2, 0);
        left = player.getLocation().add(0, 2, 0);


        new BukkitRunnable() {

            long c = 0;

            @Override
            public void run() {

                if(c == 200)
                    this.cancel();

                for (Location loc : frontLocations.keySet()) {
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(particle, loc, 1, 0, 0, 0, 0D, dustOptions, false);
                }

                for (Location loc : rightLocations.keySet()) {
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(particle, loc, 1, 0, 0, 0, 0D, dustOptions, false);
                }

                for (Location loc : backLocations.keySet()) {
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(particle, loc, 1, 0, 0, 0, 0D, dustOptions, false);
                }

                for (Location loc : leftLocations.keySet()) {
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(particle, loc, 1, 0, 0, 0, 0D, dustOptions, false);
                }

                c++;
            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 0L);


        new BukkitRunnable() {

            long c = 0;

            public void run() {
                if (c == 160) {
                    this.cancel();
                    return;
                }

                if(c < 80){
                    first(c);
                }else if(c > 100 && c < 160)
                    second(c);
                c++;

            }

        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);


    }




    private void first(long c){
        double x = Math.sin((double) c / 75 * Math.PI) / 10;
        double y = Math.cos((double) c / 75 * Math.PI) / 2.5;
        double z = Math.sin((double) c / 75 * Math.PI) / 10;

        front.add(x, y, 0);
        right.add(0, y, z);
        back.add(-x, y, 0);
        left.add(0, y, -z);

        frontLocations.put(new Location(front.getWorld(), front.getX(), front.getY(), front.getZ()), x);
        rightLocations.put(new Location(right.getWorld(), right.getX(), right.getY(), right.getZ()), z);
        backLocations.put(new Location(back.getWorld(), back.getX(), back.getY(), back.getZ()), -x);
        leftLocations.put(new Location(left.getWorld(), left.getX(), left.getY(), left.getZ()), -z);
    }




    private void second(long c){

        for(Location loc : frontLocations.keySet())
            front.add(0, frontLocations.get(loc) / 15, 0);

        for(Location loc : rightLocations.keySet())
            front.add(0, rightLocations.get(loc) / 15, 0);

        for(Location loc : backLocations.keySet())
            front.add(0, backLocations.get(loc) / 15, 0);

        for(Location loc : leftLocations.keySet())
            front.add(0, leftLocations.get(loc) / 15, 0);

        frontLocations.put(new Location(front.getWorld(), front.getX(), front.getY(), front.getZ()), frontLocations.get(front));
        rightLocations.put(new Location(right.getWorld(), right.getX(), right.getY(), right.getZ()), rightLocations.get(right));
        backLocations.put(new Location(back.getWorld(), back.getX(), back.getY(), back.getZ()), backLocations.get(back));
        leftLocations.put(new Location(left.getWorld(), left.getX(), left.getY(), left.getZ()), leftLocations.get(left));
    }



}
