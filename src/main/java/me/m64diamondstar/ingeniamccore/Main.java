package me.m64diamondstar.ingeniamccore;

import me.m64diamondstar.ingeniamccore.Database.MySQL;
import me.m64diamondstar.ingeniamccore.General.Commands.GamemodeCmd;
import me.m64diamondstar.ingeniamccore.General.Commands.Ingenia.IngeniaCmd;
import me.m64diamondstar.ingeniamccore.Wands.WandListener.WandListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    public MySQL SQL;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("---------------------------");
        Bukkit.getLogger().info("Started loading IngeniaMC-Core!");
        Bukkit.getLogger().info(" ");

        loadMainInstances();
        Bukkit.getLogger().info("Main instances loaded ✓");

        this.saveDefaultConfig();
        this.reloadConfig();
        Bukkit.getLogger().info("Config (re)loaded ✓");
        this.SQL = new MySQL(this);


        this.loadCommandExecutors();
        Bukkit.getLogger().info("Commands loaded ✓");

        this.loadTabCompleters();
        Bukkit.getLogger().info("Tab Completers loaded ✓");

        this.loadEventListeners();
        Bukkit.getLogger().info("Event Listeners loaded ✓");

        Bukkit.getLogger().info("Scoreboard Library loaded ✓");

        Bukkit.getLogger().info(" ");
        Bukkit.getLogger().info("Finished loading, IngeniaMC-Core is enabled!");
        Bukkit.getLogger().info("---------------------------");
    }



    @Override
    public void onDisable() {
        SQL.disconnect();
    }





    private void loadMainInstances(){

    }

    private void loadCommandExecutors(){
        Objects.requireNonNull(this.getCommand("gmc")).setExecutor(new GamemodeCmd());
        Objects.requireNonNull(this.getCommand("gms")).setExecutor(new GamemodeCmd());
        Objects.requireNonNull(this.getCommand("gma")).setExecutor(new GamemodeCmd());
        Objects.requireNonNull(this.getCommand("gmsp")).setExecutor(new GamemodeCmd());

        Objects.requireNonNull(this.getCommand("ingenia")).setExecutor(new IngeniaCmd());
    }

    private void loadTabCompleters(){

    }

    private void loadEventListeners(){
        Bukkit.getServer().getPluginManager().registerEvents(new WandListener(), this);
    }

}
