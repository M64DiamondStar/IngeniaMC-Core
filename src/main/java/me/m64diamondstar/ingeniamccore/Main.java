package me.m64diamondstar.ingeniamccore;

import me.m64diamondstar.ingeniamccore.Database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public MySQL SQL;

    @Override
    public void onEnable() {

        Bukkit.getLogger().info("---------------------------");
        Bukkit.getLogger().info("IngeniaMC-Core is enabled!");
        Bukkit.getLogger().info("---------------------------");

        this.SQL = new MySQL(this);

        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
