package me.m64diamondstar.ingeniamccore

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import me.m64diamondstar.ingeniamccore.attractions.listeners.PlayerInteractEntityListener
import me.m64diamondstar.ingeniamccore.general.commands.CosmeticCommand
import me.m64diamondstar.ingeniamccore.general.commands.GamemodeCommand
import me.m64diamondstar.ingeniamccore.general.commands.MessageCommand
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.IngeniaCommand
import me.m64diamondstar.ingeniamccore.general.commands.WandCommand
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.IngeniaTabCompleter
import me.m64diamondstar.ingeniamccore.general.listeners.*
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import me.m64diamondstar.ingeniamccore.general.listeners.helpers.BonemealListener
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.gui.GuiListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Main : JavaPlugin() {

    private lateinit var protocolManager: ProtocolManager

    companion object {
        lateinit var plugin: Main
        var isDisabling: Boolean = false
    }

    override fun onEnable() {

        plugin = this
        protocolManager = ProtocolLibrary.getProtocolManager()

        Bukkit.getLogger().info("---------------------------")
        Bukkit.getLogger().info("Started loading IngeniaMC-Core!")
        Bukkit.getLogger().info(" ")

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

        loadPacketListeners()
        Bukkit.getLogger().info("Packet Listeners loaded ✓")

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

    private fun loadCommandExecutors() {
        Objects.requireNonNull(getCommand("gmc"))?.setExecutor(GamemodeCommand())
        Objects.requireNonNull(getCommand("gms"))?.setExecutor(GamemodeCommand())
        Objects.requireNonNull(getCommand("gma"))?.setExecutor(GamemodeCommand())
        Objects.requireNonNull(getCommand("gmsp"))?.setExecutor(GamemodeCommand())

        Objects.requireNonNull(getCommand("ingenia"))?.setExecutor(IngeniaCommand())

        Objects.requireNonNull(getCommand("cosmetics"))?.setExecutor(CosmeticCommand())

        Objects.requireNonNull(getCommand("wand"))?.setExecutor(WandCommand())

        Objects.requireNonNull(getCommand("msg"))?.setExecutor(MessageCommand())
        Objects.requireNonNull(getCommand("tell"))?.setExecutor(MessageCommand())
        Objects.requireNonNull(getCommand("r"))?.setExecutor(MessageCommand())
        Objects.requireNonNull(getCommand("react"))?.setExecutor(MessageCommand())
    }

    private fun loadTabCompleters() {
        Objects.requireNonNull(getCommand("ig")?.setTabCompleter(IngeniaTabCompleter()))
    }

    private fun loadEventListeners() {
        /*
            All wand events
         */
        Bukkit.getServer().pluginManager.registerEvents(WandListener(), this)

        /*
            Join/leave events
         */
        Bukkit.getServer().pluginManager.registerEvents(JoinListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(LeaveListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(ServerListPingListener(), this)

        /*
            Chat events
         */
        Bukkit.getServer().pluginManager.registerEvents(ChatListener(), this)

        /*
            Inventory/GUI open events
         */
        Bukkit.getServer().pluginManager.registerEvents(InteractListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(InventoryListener(), this)

        /*
            All GUI events
         */
        Bukkit.getServer().pluginManager.registerEvents(GuiListener(), this)

        /*
            Help Events
         */
        Bukkit.getServer().pluginManager.registerEvents(BonemealListener(), this)

        /*
            Attraction Events
         */
        Bukkit.getServer().pluginManager.registerEvents(PlayerInteractEntityListener(), this)
    }

    private fun loadPacketListeners(){

    }
}