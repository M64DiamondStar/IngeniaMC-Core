package me.m64diamondstar.ingeniamccore;


import me.m64diamondstar.ingeniamccore.Database.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public MySQL SQL;

    @Override
    public void onEnable() {
        this.SQL = new MySQL(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
