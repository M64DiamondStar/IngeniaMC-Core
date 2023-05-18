package me.m64diamondstar.ingeniamccore

import com.craftmend.openaudiomc.api.interfaces.AudioApi
import me.m64diamondstar.ingeniamccore.attractions.listeners.PlayerInteractEntityListener
import me.m64diamondstar.ingeniamccore.attractions.traincarts.SignRegistry
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.webhook.DiscordWebhook
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWord
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWordListener
import me.m64diamondstar.ingeniamccore.games.presenthunt.PresentHuntUtils
import me.m64diamondstar.ingeniamccore.games.presenthunt.listeners.PlayerInteractListener
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.areas.listeners.PlayerMoveListener
import me.m64diamondstar.ingeniamccore.general.commands.*
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.IngeniaCommand
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.AdminTabCompleter
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.IngeniaTabCompleter
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.MessageTabCompleter
import me.m64diamondstar.ingeniamccore.general.listeners.*
import me.m64diamondstar.ingeniamccore.general.listeners.helpers.BonemealListener
import me.m64diamondstar.ingeniamccore.general.listeners.protection.BlockListener
import me.m64diamondstar.ingeniamccore.general.listeners.protection.DamageListener
import me.m64diamondstar.ingeniamccore.general.listeners.protection.EntityDismountListener
import me.m64diamondstar.ingeniamccore.general.listeners.protection.HungerListener
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.general.warps.WarpUtils
import me.m64diamondstar.ingeniamccore.shows.listeners.EntityChangeBlockListener
import me.m64diamondstar.ingeniamccore.utils.TeamHandler
import me.m64diamondstar.ingeniamccore.utils.gui.GuiListener
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class IngeniaMC : JavaPlugin() {

    companion object {
        lateinit var plugin: IngeniaMC
        lateinit var audioApi: AudioApi
        var isDisabling: Boolean = false
    }

    override fun onEnable() {

        audioApi = AudioApi.getInstance()

        plugin = this

        Bukkit.getLogger().info("---------------------------")
        Bukkit.getLogger().info("Started loading IngeniaMC-Core!")
        Bukkit.getLogger().info(" ")

        Bukkit.getLogger().info("Main instances loaded ✓")

        saveDefaultConfig()
        Bukkit.getLogger().info("Config (re)loaded ✓")

        loadCommandExecutors()
        Bukkit.getLogger().info("Commands loaded ✓")

        loadTabCompleters()
        Bukkit.getLogger().info("Tab Completers loaded ✓")

        loadEventListeners()
        Bukkit.getLogger().info("Event Listeners loaded ✓")

        loadPacketListeners()
        Bukkit.getLogger().info("Packet Listeners loaded ✓")

        SignRegistry.registerSigns()
        Bukkit.getLogger().info("TrainCarts Signs loaded ✓")

        for(player in Bukkit.getOnlinePlayers()){
            val ingeniaPlayer = IngeniaPlayer(player)
            ingeniaPlayer.setScoreboard(true)
        }
        Bukkit.getLogger().info("Player Scoreboards loaded ✓")

        Bukkit.getScheduler().scheduleSyncDelayedTask(this) { AttractionUtils.spawnAllAttractions() }
        Bukkit.getLogger().info("Attractions loaded ✓")

        AreaUtils.getAllAreasFromData().forEach { AreaUtils.addArea(it) }
        Bukkit.getLogger().info("Areas loaded ✓")

        DiscordBot.start()
        Bukkit.getLogger().info("Discord Bot loaded ✓")

        loadTasks()
        Bukkit.getLogger().info("Tasks loaded ✓")

        WarpUtils.reloadWarpList()
        PresentHuntUtils.loadActivePresents()
        TeamHandler.load()
        Bukkit.getLogger().info("" +
                "Small tasks ✓")

        Bukkit.getLogger().info(" ")
        Bukkit.getLogger().info("Finished loading, IngeniaMC-Core is enabled!")
        Bukkit.getLogger().info("---------------------------")

        // Send Discord Webhook
        val discordWebhook = DiscordWebhook(plugin.config.getString("Discord.Webhook.Chat"))

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val timeNow = LocalDateTime.now()

        discordWebhook.addEmbed(
            DiscordWebhook.EmbedObject()
            .setAuthor("Server Starting...", null, "https://ingeniamc.net/images/startup.gif")
            .setFooter("Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                    "  ${dateTimeFormatter.format(timeNow)}", null)
            .setColor(Color.decode("#f4b734"))
        )

        discordWebhook.execute()

    }

    override fun onDisable() {
        isDisabling = true

        // Basic shutdown properties
        AttractionUtils.despawnAllAttractions()
        SignRegistry.unregisterSigns()
        PresentHuntUtils.saveActivePresents()
        TeamHandler.unload()
        saveConfig()

        // Send Discord Webhook
        val discordWebhook = DiscordWebhook(plugin.config.getString("Discord.Webhook.Chat"))

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val timeNow = LocalDateTime.now()

        discordWebhook.addEmbed(
            DiscordWebhook.EmbedObject()
                .setAuthor("Server Shutting Down...", null, "https://ingeniamc.net/images/shutdown.gif")
                .setFooter("Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                        "  ${dateTimeFormatter.format(timeNow)}", null)
                .setColor(Color.decode("#f4b734"))
        )

        discordWebhook.execute()

        // Shut the discord but down
        DiscordBot.shutdown()
    }

    private fun loadCommandExecutors() {
        Objects.requireNonNull(getCommand("gmc"))?.setExecutor(GamemodeCommand())
        Objects.requireNonNull(getCommand("gms"))?.setExecutor(GamemodeCommand())
        Objects.requireNonNull(getCommand("gma"))?.setExecutor(GamemodeCommand())
        Objects.requireNonNull(getCommand("gmsp"))?.setExecutor(GamemodeCommand())


        Objects.requireNonNull(getCommand("ingenia"))?.setExecutor(IngeniaCommand())
        Objects.requireNonNull(getCommand("admin"))?.setExecutor(AdminCommand())


        Objects.requireNonNull(getCommand("cosmetics"))?.setExecutor(MenuCommands())
        Objects.requireNonNull(getCommand("wand"))?.setExecutor(MenuCommands())
        Objects.requireNonNull(getCommand("rides"))?.setExecutor(MenuCommands())
        Objects.requireNonNull(getCommand("shops"))?.setExecutor(MenuCommands())


        Objects.requireNonNull(getCommand("msg"))?.setExecutor(MessageCommand())
        Objects.requireNonNull(getCommand("react"))?.setExecutor(MessageCommand())

        Objects.requireNonNull(getCommand("leave"))?.setExecutor(LeaveCommand())

        Objects.requireNonNull(getCommand("spawn"))?.setExecutor(SpawnCommand())

        Objects.requireNonNull(getCommand("link"))?.setExecutor(LinkCommand())
        Objects.requireNonNull(getCommand("unlink"))?.setExecutor(LinkCommand())
    }

    private fun loadTabCompleters() {
        Objects.requireNonNull(getCommand("ingenia")?.setTabCompleter(IngeniaTabCompleter()))
        Objects.requireNonNull(getCommand("admin")?.setTabCompleter(AdminTabCompleter ()))
        Objects.requireNonNull(getCommand("msg")?.setTabCompleter(MessageTabCompleter()))
        Objects.requireNonNull(getCommand("react")?.setTabCompleter(MessageTabCompleter()))
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
        Bukkit.getServer().pluginManager.registerEvents(GuessTheWordListener(), this)

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
            Show Events
         */
        Bukkit.getServer().pluginManager.registerEvents(EntityChangeBlockListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.shows.editor.listeners.ChatListener(), this)

        /*
            Attraction Events
         */
        Bukkit.getServer().pluginManager.registerEvents(PlayerInteractEntityListener(), this)

        /*
            Move Events
         */
        Bukkit.getServer().pluginManager.registerEvents(MoveListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerMoveListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.games.parkour.listeners.PlayerMoveListener(), this)

        /*
            Game Events
         */
        Bukkit.getServer().pluginManager.registerEvents(PlayerInteractListener(), this)

        /*
            Protection Events
         */
        Bukkit.getServer().pluginManager.registerEvents(BlockListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.general.listeners.protection.InteractListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(DamageListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(HungerListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(EntityDismountListener(), this)
    }

    private fun loadPacketListeners(){

    }

    private fun loadTasks(){
        object: BukkitRunnable(){
            override fun run() {
                val guessTheWord = GuessTheWord()
                guessTheWord.execute()
            }
        }.runTaskTimer(this, 200L, 18000L)
    }
}