package me.m64diamondstar.ingeniamccore

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.craftmend.openaudiomc.api.interfaces.AudioApi
import me.m56738.smoothcoasters.api.SmoothCoastersAPI
import me.m64diamondstar.ingeniamccore.attractions.listeners.PlayerInteractEntityListener
import me.m64diamondstar.ingeniamccore.attractions.traincarts.SignRegistry
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveColor
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordBot
import me.m64diamondstar.ingeniamccore.discord.listeners.minecraft.ReceiveExpListener
import me.m64diamondstar.ingeniamccore.discord.listeners.minecraft.ReceiveGoldenStarsListener
import me.m64diamondstar.ingeniamccore.discord.listeners.minecraft.ReceiveRidecountListener
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWord
import me.m64diamondstar.ingeniamccore.games.guesstheword.GuessTheWordListener
import me.m64diamondstar.ingeniamccore.games.presenthunt.PresentHuntUtils
import me.m64diamondstar.ingeniamccore.games.presenthunt.listeners.PlayerInteractListener
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import me.m64diamondstar.ingeniamccore.games.splashbattle.listeners.*
import me.m64diamondstar.ingeniamccore.games.wandclash.WandClashRegistry
import me.m64diamondstar.ingeniamccore.games.wandclash.listeners.ClashWandListener
import me.m64diamondstar.ingeniamccore.games.wandclash.util.ClashWandRegistry
import me.m64diamondstar.ingeniamccore.general.areas.AreaUtils
import me.m64diamondstar.ingeniamccore.general.areas.listeners.AudioConnectListener
import me.m64diamondstar.ingeniamccore.general.areas.listeners.PlayerMoveListener
import me.m64diamondstar.ingeniamccore.general.commands.*
import me.m64diamondstar.ingeniamccore.general.commands.ingenia.IngeniaCommand
import me.m64diamondstar.ingeniamccore.general.commands.tabcompleters.*
import me.m64diamondstar.ingeniamccore.general.levels.listeners.ExpListener
import me.m64diamondstar.ingeniamccore.general.listeners.*
import me.m64diamondstar.ingeniamccore.general.listeners.ChatListener
import me.m64diamondstar.ingeniamccore.general.listeners.InteractListener
import me.m64diamondstar.ingeniamccore.general.listeners.InventoryListener
import me.m64diamondstar.ingeniamccore.general.listeners.LeaveListener
import me.m64diamondstar.ingeniamccore.general.listeners.helpers.BonemealListener
import me.m64diamondstar.ingeniamccore.npc.commands.NpcCommand
import me.m64diamondstar.ingeniamccore.npc.listeners.NpcListener
import me.m64diamondstar.ingeniamccore.npc.utils.NpcRegistry
import me.m64diamondstar.ingeniamccore.protect.listeners.*
import me.m64diamondstar.ingeniamccore.protect.listeners.DamageListener
import me.m64diamondstar.ingeniamccore.protect.moderation.ModerationRegistry
import me.m64diamondstar.ingeniamccore.shops.listeners.ShopListener
import me.m64diamondstar.ingeniamccore.utils.EmojiUtils
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.TeamHandler
import me.m64diamondstar.ingeniamccore.utils.gui.GuiListener
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.utils.WandRegistry
import me.m64diamondstar.ingeniamccore.wands.wandlistener.WandListener
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class IngeniaMC : JavaPlugin() {

    companion object {
        lateinit var plugin: IngeniaMC
        lateinit var audioApi: AudioApi
        lateinit var smoothCoastersAPI: SmoothCoastersAPI
        lateinit var miniMessage: MiniMessage
        lateinit var protocolManager: ProtocolManager
        var isDisabling: Boolean = false
        lateinit var spawn: Location
    }

    override fun onEnable() {

        plugin = this
        audioApi = AudioApi.getInstance()
        smoothCoastersAPI = SmoothCoastersAPI(this)
        miniMessage = MiniMessage.miniMessage()
        protocolManager = ProtocolLibrary.getProtocolManager()

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

        WandClashRegistry.registerAllArenas()
        Bukkit.getLogger().info("Game Arenas Loaded ✓")

        EmojiUtils.loadEmojis()

        loadTasks()
        Bukkit.getLogger().info("Tasks loaded ✓")

        WandRegistry.registerWands()
        ClashWandRegistry.registerClashWands()
        Bukkit.getLogger().info("Wands loaded ✓")

        ModerationRegistry.registerBlockedWords()
        Bukkit.getLogger().info("Blocked Words loaded ✓")

        PresentHuntUtils.loadActivePresents()
        TeamHandler.load()
        Bukkit.getLogger().info("" +
                "Small tasks ✓")

        Bukkit.getLogger().info(" ")
        Bukkit.getLogger().info("Finished loading, IngeniaMC-Core is enabled!")
        Bukkit.getLogger().info("---------------------------")

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
        smoothCoastersAPI.unregister()
        SplashBattleUtils.players.forEach { SplashBattleUtils.leave(it) }
        NpcRegistry.deleteAll()

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

        Objects.requireNonNull(getCommand("warp"))?.setExecutor(WarpCommand())

        Objects.requireNonNull(getCommand("feature"))?.setExecutor(FeatureCommand())

        Objects.requireNonNull(getCommand("fixaudio"))?.setExecutor(FixAudioCommand())
        Objects.requireNonNull(getCommand("audiocredits"))?.setExecutor(AudioCreditsCommand())
        Objects.requireNonNull(getCommand("rules"))?.setExecutor(RulesCommand())
        Objects.requireNonNull(getCommand("playtime"))?.setExecutor(PlaytimeCommand())

        Objects.requireNonNull(getCommand("npc"))?.setExecutor(NpcCommand())
    }

    private fun loadTabCompleters() {
        Objects.requireNonNull(getCommand("ingenia")?.setTabCompleter(IngeniaTabCompleter()))
        Objects.requireNonNull(getCommand("admin")?.setTabCompleter(AdminTabCompleter ()))

        Objects.requireNonNull(getCommand("msg")?.setTabCompleter(MessageTabCompleter()))
        Objects.requireNonNull(getCommand("react")?.setTabCompleter(MessageTabCompleter()))

        Objects.requireNonNull(getCommand("dispatch")?.setTabCompleter(AttractionTabCompleter()))
        Objects.requireNonNull(getCommand("operate")?.setTabCompleter(AttractionTabCompleter()))

        Objects.requireNonNull(getCommand("warp")?.setTabCompleter(WarpTabCompleter()))

        Objects.requireNonNull(getCommand("feature")?.setTabCompleter(FeatureTabCompleter()))
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
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.protect.listeners.InteractListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(DamageListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(HungerListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(EntityDismountListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(BoatListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(EntityListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerListeners(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.protect.listeners.ChatListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(EquipmentListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(me.m64diamondstar.ingeniamccore.protect.listeners.InventoryListener(), this)

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
            Clash Wand Events
         */
        Bukkit.getServer().pluginManager.registerEvents(ClashWandListener(), this)

        /*
            Shop Events
         */
        Bukkit.getServer().pluginManager.registerEvents(ShopListener(), this)

        /*
            Audio Events
         */
        AudioConnectListener.startListeners()

        /*
            Discord Events
         */
        Bukkit.getServer().pluginManager.registerEvents(ReceiveExpListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(ReceiveGoldenStarsListener(), this)
        Bukkit.getServer().pluginManager.registerEvents(ReceiveRidecountListener(), this)

        /*
            Player Stat Events
         */
        Bukkit.getServer().pluginManager.registerEvents(ExpListener(), this)

        /*
            NPC Events
         */
        Bukkit.getServer().pluginManager.registerEvents(NpcListener(), this)
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