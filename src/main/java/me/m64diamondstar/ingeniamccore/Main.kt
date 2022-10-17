package me.m64diamondstar.ingeniamccore

import me.m64diamondstar.ingeniamccore.cosmetics.inventory.CosmeticsListener
import me.m64diamondstar.ingeniamccore.general.commands.CosmeticCommand
import me.m64diamondstar.ingeniamccore.general.commands.GamemodeCmd
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.IngeniaCommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.WandCommand
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.IngeniaTabCompleter
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import me.m64diamondstar.ingeniamccore.general.listeners.JoinListener
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import javax.swing.text.StyledEditorKit

class Main : JavaPlugin() {

    companion object {
        lateinit var plugin: Main
        var isDisabling: Boolean = false
    }

    override fun onEnable() {

        plugin = this

        Bukkit.getLogger().info("---------------------------")
        Bukkit.getLogger().info("Started loading IngeniaMC-Core!")
        Bukkit.getLogger().info(" ")

        loadMainInstances()
        Bukkit.getLogger().info("Main instances loaded ✓")

        saveDefaultConfig()
        reloadConfig()
        Bukkit.getLogger().info("Config (re)loaded ✓")

        loadCommandExecutors()
        Bukkit.getLogger().info("Commands loaded ✓")

        loadTabCompleters()
        Bukkit.getLogger().info("Tab Completers loaded ✓")

        loadEventListeners()
        Bukkit.getLogger().info("Event Listeners loaded ✓")

        for(player in Bukkit.getOnlinePlayers()){
            val ingeniaPlayer = IngeniaPlayer(player)
            ingeniaPlayer.setScoreboard(true)
        }
        Bukkit.getLogger().info("Player Scoreboards loaded ✓")

        Bukkit.getLogger().info(" ")
        Bukkit.getLogger().info("Finished loading, IngeniaMC-Core is enabled!")
        Bukkit.getLogger().info("---------------------------")
    }

    override fun onDisable() {
        isDisabling = true
    }

    private fun loadMainInstances() {}

    private fun loadCommandExecutors() {
        Objects.requireNonNull(getCommand("gmc"))?.setExecutor(GamemodeCmd())
        Objects.requireNonNull(getCommand("gms"))?.setExecutor(GamemodeCmd())
        Objects.requireNonNull(getCommand("gma"))?.setExecutor(GamemodeCmd())
        Objects.requireNonNull(getCommand("gmsp"))?.setExecutor(GamemodeCmd())

        Objects.requireNonNull(getCommand("ingenia"))?.setExecutor(IngeniaCommand())

        Objects.requireNonNull(getCommand("cosmetics"))?.setExecutor(CosmeticCommand())

        Objects.requireNonNull(getCommand("wand"))?.setExecutor(WandCommand())
    }

    private fun loadTabCompleters() {
        Objects.requireNonNull(getCommand("ig")?.setTabCompleter(IngeniaTabCompleter()))
    }

    private fun loadEventListeners() {
        Bukkit.getServer().pluginManager.registerEvents(WandListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(JoinListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(CosmeticsListener(), this)
    }
}