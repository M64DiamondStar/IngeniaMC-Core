package me.m64diamondstar.ingeniamccore;

import me.m64diamondstar.ingeniamccore.Database.MySQL;
import me.m64diamondstar.ingeniamccore.Database.Tables.Table;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public MySQL SQL;

    @Override
    public void onEnable() {
        loadPlugin();
    }

    @Override
    public void onDisable() {
        unloadPlugin();
    }




    private void loadPlugin(){
        Bukkit.getLogger().info("---------------------------");
        Bukkit.getLogger().info("Started loading IngeniaMC-Core!");
        Bukkit.getLogger().info(" ");

        loadMainInstances();
        Bukkit.getLogger().info("Main instances loaded ✓");

        this.saveDefaultConfig();
        this.reloadConfig();
        Bukkit.getLogger().info("Config (re)loaded ✓");

        this.SQL = new MySQL(this);

        Bukkit.getLogger().info(" ");
        Bukkit.getLogger().info("Finished loading, IngeniaMC-Core is enabled!");
        Bukkit.getLogger().info("---------------------------");
    }


    private void unloadPlugin(){
        SQL.disconnect();
    }


    private void loadMainInstances(){
        new Table(this);
    }

}
