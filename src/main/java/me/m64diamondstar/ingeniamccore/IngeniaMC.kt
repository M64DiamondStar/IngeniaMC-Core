package me.m64diamondstar.ingeniamccore

import com.craftmend.openaudiomc.api.interfaces.AudioApi
import me.m64diamondstar.ingeniamccore.attractions.listeners.PlayerInteractEntityListener
import me.m64diamondstar.ingeniamccore.attractions.traincarts.SignRegistry
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveColor
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.webhook.DiscordWebhook
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWord
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWordListener
import me.m64diamondstar.ingeniamccore.games.presenthunt.PresentHuntUtils
import me.m64diamondstar.ingeniamccore.games.presenthunt.listeners.PlayerInteractListener
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import me.m64diamondstar.ingeniamccore.games.splashbattle.listeners.*
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.areas.listeners.AudioConnectListener
import me.m64diamondstar.ingeniamccore.general.areas.listeners.PlayerMoveListener
import me.m64diamondstar.ingeniamccore.general.commands.*
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.IngeniaCommand
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.AdminTabCompleter
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.AttractionTabCompleter
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.IngeniaTabCompleter
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.MessageTabCompleter
import me.m64diamondstar.ingeniamccore.general.listeners.*
import me.m64diamondstar.ingeniamccore.general.listeners.InteractListener
import me.m64diamondstar.ingeniamccore.general.listeners.LeaveListener
import me.m64diamondstar.ingeniamccore.general.listeners.helpers.BonemealListener
import me.m64diamondstar.ingeniamccore.general.listeners.protection.*
import me.m64diamondstar.ingeniamccore.general.listeners.protection.DamageListener
import me.m64diamondstar.ingeniamccore.general.warps.WarpUtils
import me.m64diamondstar.ingeniamccore.shops.listeners.ShopListener
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.TeamHandler
import me.m64diamondstar.ingeniamccore.utils.gui.GuiListener
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.utils.WandRegistry
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import org.bukkit.Bukkit
import org.bukkit.Location
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
        lateinit var spawn: Location
    }

    override fun onEnable() {

        plugin = this
        audioApi = AudioApi.getInstance()

        Bukkit.getLogger().info("---------------------------")
        Bukkit.getLogger().info("Started loading IngeniaMC-Core!")
        Bukkit.getLogger().info(" ")

        Bukkit.getLogger().info("Main instances loaded ✓")

        saveDefaultConfig()
        Bukkit.getLogger().info("Config (re)loaded ✓")

        loadDefaultConfigurations()
        Bukkit.getLogger().info("Default configurations loaded ✓")

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

        Bukkit.getLogger().info("Player Scoreboards loaded ✓")

        Bukkit.getScheduler().scheduleSyncDelayedTask(this) { AttractionUtils.spawnAllAttractions() }
        Bukkit.getLogger().info("Attractions loaded ✓")

        AreaUtils.getAllAreasFromData().forEach { AreaUtils.addArea(it) }
        Bukkit.getLogger().info("Areas loaded ✓")

        DiscordBot.start()
        Bukkit.getLogger().info("Discord Bot loaded ✓")

        loadTasks()
        Bukkit.getLogger().info("Tasks loaded ✓")

        WandRegistry.registerWands()
        Bukkit.getLogger().info("Wands loaded ✓")

        WarpUtils.reloadWarpList()
        PresentHuntUtils.loadActivePresents()
        TeamHandler.load()
        Bukkit.getLogger().info("" +
                "Small tasks ✓")

        Bukkit.getLogger().info(" ")
        Bukkit.getLogger().info("Finished loading, IngeniaMC-Core is enabled!")
        Bukkit.getLogger().info("---------------------------")

        // Send Discord Webhook
        if(plugin.config.getBoolean("Discord.Webhook.Enable")){
            val discordWebhook = DiscordWebhook(plugin.config.getString("Discord.Webhook.Chat"))

            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val timeNow = LocalDateTime.now()

            discordWebhook.addEmbed(
                DiscordWebhook.EmbedObject()
                    .setAuthor("Server Starting...", null, "https://ingeniamc.net/images/startup.gif")
                    .setFooter(
                        "Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                                "  ${dateTimeFormatter.format(timeNow)}", null
                    )
                    .setColor(Color.decode("#f4b734"))
            )

            discordWebhook.execute()
        }

        spawn = LocationUtils.getLocationFromString(plugin.config.getString("Spawn")) ?: Location(Bukkit.getWorlds().first(), 0.5, 52.0, 0.5)

    }

    override fun onDisable() {
        isDisabling = true

        // Basic shutdown properties
        AttractionUtils.despawnAllAttractions()
        SignRegistry.unregisterSigns()
        PresentHuntUtils.saveActivePresents()
        TeamHandler.unload()
        reloadConfig()
        saveConfig()
        SplashBattleUtils.players.forEach { SplashBattleUtils.leave(it) }

        if(plugin.config.getBoolean("Discord.Webhook.Enable")){// Send Discord Webhook
            val discordWebhook = DiscordWebhook(plugin.config.getString("Discord.Webhook.Chat"))

            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val timeNow = LocalDateTime.now()

            discordWebhook.addEmbed(
                DiscordWebhook.EmbedObject()
                    .setAuthor("Server Shutting Down...", null, "https://ingeniamc.net/images/shutdown.gif")
                    .setFooter(
                        "Online: ${Bukkit.getServer().onlinePlayers.size}/${Bukkit.getServer().maxPlayers}" +
                                "  ${dateTimeFormatter.format(timeNow)}", null
                    )
                    .setColor(Color.decode("#f4b734"))
            )

            discordWebhook.execute()
        }

        // Shut the discord but down
        DiscordBot.shutdown()
    }

    private fun loadDefaultConfigurations(){
        val joinLeaveColor = JoinLeaveColor()
        joinLeaveColor.create("default", MessageType.DEFAULT, "Default", false)

        val joinMessage = JoinLeaveMessage(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN)
        joinMessage.create("default", "%player%", "Default", false)

        val leaveMessage = JoinLeaveMessage(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE)
        leaveMessage.create("default", "%player%", "Default", false)
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
        Objects.requireNonNull(getCommand("hats"))?.setExecutor(CosmeticCommands())
        Objects.requireNonNull(getCommand("shirts"))?.setExecutor(CosmeticCommands())
        Objects.requireNonNull(getCommand("pants"))?.setExecutor(CosmeticCommands())
        Objects.requireNonNull(getCommand("shoes"))?.setExecutor(CosmeticCommands())

        Objects.requireNonNull(getCommand("rides"))?.setExecutor(MenuCommands())
        Objects.requireNonNull(getCommand("shops"))?.setExecutor(MenuCommands())


        Objects.requireNonNull(getCommand("msg"))?.setExecutor(MessageCommand())
        Objects.requireNonNull(getCommand("react"))?.setExecutor(MessageCommand())

        Objects.requireNonNull(getCommand("leave"))?.setExecutor(LeaveCommand())

        Objects.requireNonNull(getCommand("spawn"))?.setExecutor(SpawnCommand())

        Objects.requireNonNull(getCommand("link"))?.setExecutor(LinkCommand())
        Objects.requireNonNull(getCommand("unlink"))?.setExecutor(LinkCommand())

        Objects.requireNonNull(getCommand("dispatch"))?.setExecutor(AttractionCommands())
        Objects.requireNonNull(getCommand("operate"))?.setExecutor(AttractionCommands())

        Objects.requireNonNull(getCommand("editjoinmessage"))?.setExecutor(EditJoinLeaveCommand())
        Objects.requireNonNull(getCommand("editleavemessage"))?.setExecutor(EditJoinLeaveCommand())

        Objects.requireNonNull(getCommand("testgui"))?.setExecutor(TestGuiCommand())
    }

    private fun loadTabCompleters() {
        Objects.requireNonNull(getCommand("ingenia")?.setTabCompleter(IngeniaTabCompleter()))
        Objects.requireNonNull(getCommand("admin")?.setTabCompleter(AdminTabCompleter ()))

        Objects.requireNonNull(getCommand("msg")?.setTabCompleter(MessageTabCompleter()))
        Objects.requireNonNull(getCommand("react")?.setTabCompleter(MessageTabCompleter()))

        Objects.requireNonNull(getCommand("dispatch")?.setTabCompleter(AttractionTabCompleter()))
        Objects.requireNonNull(getCommand("operate")?.setTabCompleter(AttractionTabCompleter()))
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
        Bukkit.getServer().pluginManager.registerEvents(BoatListener(), this)

        /*
            Splash Battle Events
         */
        Bukkit.getServer().pluginManager.registerEvents(TeleportListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(CommandListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.games.splashbattle.listeners.InteractListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.games.splashbattle.listeners.LeaveListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.games.splashbattle.listeners.DamageListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(SnowballListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(GunListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(SneakListener(), this)

        /*
            Shop Events
         */
        Bukkit.getServer().pluginManager.registerEvents(ShopListener(), this)

        /*
            Audio Events
         */
        AudioConnectListener.startListeners()
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