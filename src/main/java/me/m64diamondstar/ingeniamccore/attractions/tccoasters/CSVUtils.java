package me.m64diamondstar.ingeniamccore.attractions.tccoasters;

import com.bergerkiller.bukkit.coasters.csv.TrackCSV;
import me.m64diamondstar.ingeniamccore.attractions.tccoasters.objects.TrackObjectTypeParticle;

public class CSVUtils {

    public static void load() {
        TrackCSV.registerEntry(TrackObjectTypeParticle.CSVEntry::new);
    }

}
