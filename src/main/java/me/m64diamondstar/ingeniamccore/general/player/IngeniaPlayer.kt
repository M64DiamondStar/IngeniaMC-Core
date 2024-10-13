package me.m64diamondstar.ingeniamccore.general.player

import gg.flyte.twilight.scheduler.delay
import io.papermc.paper.entity.TeleportFlag
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.games.PhysicalGameType
import me.m64diamondstar.ingeniamccore.games.parkour.Parkour
import me.m64diamondstar.ingeniamccore.games.parkour.ParkourUtils
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import me.m64diamondstar.ingeniamccore.general.areas.Area
import me.m64diamondstar.ingeniamccore.general.bossbar.BossBarIndex
import me.m64diamondstar.ingeniamccore.general.bossbar.BossBarPlayerRegistry
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.getLevel
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.getLevelUpLevels
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.getRewards
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.isLevelUp
import me.m64diamondstar.ingeniamccore.general.player.data.PlayerConfig
import me.m64diamondstar.ingeniamccore.npc.utils.CharWidth
import me.m64diamondstar.ingeniamccore.utils.EmojiUtils
import me.m64diamondstar.ingeniamccore.utils.LocationUtils.getLocationFromString
import me.m64diamondstar.ingeniamccore.utils.Times
import me.m64diamondstar.ingeniamccore.utils.event.player.ReceiveExpEvent
import me.m64diamondstar.ingeniamccore.utils.event.player.ReceiveGoldenStarsEvent
import me.m64diamondstar.ingeniamccore.utils.messages.ChatIcons
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.utils.Wands.getAccessibleWands
import me.m64diamondstar.ingeniamccore.warps.WarpUtils
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class IngeniaPlayer(val player: Player) {
    private var previousInventory: Inventory? = null
    val playerConfig: PlayerConfig = PlayerConfig(player.uniqueId)

    /**
     * The player startup, this is run whenever a player joins the server.
     */
    fun startUp() {
        // Fix default player properties
        game = null
        allowDamage = false
        player.setGravity(true)
        player.walkSpeed = 0.2f
        player.fallDistance = 0f
        Bukkit.getScheduler().runTask(IngeniaMC.plugin, Runnable {
            player.removePotionEffect(PotionEffectType.SLOWNESS)
        })

        // Teleport player to the nearest warp
        player.teleportAsync(WarpUtils.getNearestLocation(player), TeleportCause.PLUGIN, TeleportFlag.EntityState.RETAIN_PASSENGERS)

        // Add emoji tab completions
        val tabCompletions = ArrayList<String>()
        tabCompletions.addAll(EmojiUtils.getAllEmojiKeys())
        tabCompletions.addAll(Bukkit.getOnlinePlayers().map { it.name })
        player.setCustomChatCompletions(tabCompletions)

        // Give menu items
        giveMenuItem()
        giveRidesItem()
        giveShopsItem()

        // Create all bossbars / HUD's
        val bossBar = BossBar.bossBar(Component.empty(), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
        BossBarPlayerRegistry.addPlayer(player, BossBarIndex.FIRST, bossBar)
        updateMainBossBar()

        val invisibleBossBar1 = BossBar.bossBar(Component.empty(), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
        BossBarPlayerRegistry.addPlayer(player, BossBarIndex.SECOND, invisibleBossBar1)
        val invisibleBossBar2 = BossBar.bossBar(Component.empty(), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
        BossBarPlayerRegistry.addPlayer(player, BossBarIndex.THIRD, invisibleBossBar2)

        // Only show HUD if player has setting enabled
        if(playerConfig.getShowHud()) {
            (player as Audience).showBossBar(bossBar)
            (player as Audience).showBossBar(invisibleBossBar1)
            (player as Audience).showBossBar(invisibleBossBar2)
        }

        // Fix player level
        val currentLevel = getLevel()
        val necessaryExp = LevelUtils.getExpRequirement(currentLevel + 1) - LevelUtils.getExpRequirement(currentLevel)
        val currentExp = exp - LevelUtils.getExpRequirement(currentLevel)
        player.level = getLevel()
        val visualExp = currentExp.toFloat() / necessaryExp
        player.exp = if(0 < visualExp && visualExp < 1) currentExp.toFloat() / necessaryExp else 0.01f

        if (isLevelUp(playerConfig.getLevel(), playerConfig.getExp()))
            levelUp(playerConfig.getLevel(), playerConfig.getExp())

        // Fix join and leave messages when null
        if(joinColor == null)
            joinColor = "default"
        if(leaveColor == null)
            leaveColor = "default"
        if(joinMessage == null)
            joinMessage = "default"
        if(leaveMessage == null)
            leaveMessage = "default"

        // Spawn all top signs
        AttractionUtils.getAllAttractions().forEach { it.spawnRidecountSign(player) }
        SplashBattleUtils.getAllSplashBattles().forEach { it.getLeaderboard().spawnSoaksSign(player) }
        ParkourUtils.getAllParkours().forEach { it.getLeaderboard().spawnSign(player) }

        // Put player into right game mode
        if(!(player.hasPermission("ingenia.team") || player.hasPermission("ingenia.team-trial")) && !player.isOp)
            player.gameMode = GameMode.ADVENTURE
    }

    /**
     * The name of the player.
     */
    val name: String
        get() = player.name

    /**
     * The prefix of the player. This is only the icon (so it requires the resourcepack).
     **/
    val prefix: String
        get() = if (player.isOp)
                    Colors.format("\uE045")
                else if (player.hasPermission("ingenia.team"))
                    Colors.format("\uE046")
                else if (player.hasPermission("ingenia.teamtrial"))
                    Colors.format("\uE046")
                else if (player.hasPermission("ingenia.vip+"))
                    Colors.format("\uE047")
                else if (player.hasPermission("ingenia.vip"))
                    Colors.format("\uE048")
                else
                    Colors.format("\uE049")

    /**
     * The prefix icon as a Component.
     * @see Component
     */
    val componentIconPrefix: Component
        get() = if(player.isOp)
                    Component.text("\uE045")
                else if(player.hasPermission("ingenia.team"))
                    Component.text("\uE046")
                else if(player.hasPermission("ingenia.teamtrial"))
                    Component.text("\uE046")
                else if(player.hasPermission("ingenia.vip+"))
                    Component.text("\uE047")
                else if(player.hasPermission("ingenia.vip"))
                    Component.text("\uE048")
                else
                    Component.text("\uE049")

    /**
     * The prefix of the player as a Component.
     * @see Component
     **/
    val componentPrefix: Component
        get() = if(player.isOp)
                    Component.text("Lead").color(TextColor.fromHexString("#c43535")).decorate(TextDecoration.BOLD)
                else if(player.hasPermission("ingenia.team"))
                    Component.text("Team").color(TextColor.fromHexString("#4180bf")).decorate(TextDecoration.BOLD)
                else if(player.hasPermission("ingenia.teamtrial"))
                    Component.text("Team Trial").color(TextColor.fromHexString("#4180bf")).decorate(TextDecoration.BOLD)
                else if(player.hasPermission("ingenia.vip+"))
                    Component.text("VIP+").color(TextColor.fromHexString("#9054b0")).decorate(TextDecoration.BOLD)
                else if(player.hasPermission("ingenia.vip"))
                    Component.text("VIP").color(TextColor.fromHexString("#54b0b0")).decorate(TextDecoration.BOLD)
                else
                    Component.text("Visitor").color(TextColor.fromHexString("#a1a1a1")).decorate(TextDecoration.BOLD)

    /**
     * The raw prefix of a player without any text decorations.
     */
    val rawPrefix: String
        get() = if (player.isOp)
                    "Lead"
                else if (player.hasPermission("ingenia.team"))
                    "Team"
                else if (player.hasPermission("ingenia.teamtrial"))
                    "Team Trial"
                else if (player.hasPermission("ingenia.vip+"))
                    "VIP+"
                else if (player.hasPermission("ingenia.vip"))
                    "VIP"
                else
                    "Visitor"

    /**
     * The name of the player as a Component with the color based on the rank.
     */
    val nameLightColored: Component
        get() = if(player.isOp)
            Component.text(name).color(TextColor.fromHexString("#ffdede"))
        else if(player.hasPermission("ingenia.team"))
            Component.text(name).color(TextColor.fromHexString("#deefff"))
        else if(player.hasPermission("ingenia.teamtrial"))
            Component.text(name).color(TextColor.fromHexString("#deefff"))
        else if(player.hasPermission("ingenia.vip+"))
            Component.text(name).color(TextColor.fromHexString("#f9deff"))
        else if(player.hasPermission("ingenia.vip"))
            Component.text(name).color(TextColor.fromHexString("#defdff"))
        else
            Component.text(name).color(TextColor.fromHexString("#cccccc"))

    /**
     * The area the player is currently in, formatted as "category,area".
     */
    var currentAreaName: String?
        get() {
            val container = player.persistentDataContainer
            val name = container.get(NamespacedKey(IngeniaMC.plugin, "current-area"), PersistentDataType.STRING)
            if(name.equals("null", ignoreCase = true))
                return null
            return name
        }
        set(value) {
            val container = player.persistentDataContainer
            if(value == null)
                container.set(
                    NamespacedKey(IngeniaMC.plugin, "current-area"), PersistentDataType.STRING, "null")
            else
                container.set(NamespacedKey(IngeniaMC.plugin, "current-area"), PersistentDataType.STRING, value)
        }

    /**
     * Teleports the player to the given location.
     * Retains passengers and vehicles
     * Takes fancy teleport setting into account when teleporting.
     */
    fun teleport(location: Location) {
        if(!playerConfig.getFancyTeleport())
            player.teleport(location, TeleportFlag.EntityState.RETAIN_PASSENGERS, TeleportFlag.EntityState.RETAIN_VEHICLE)
        else {
            val times = Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(500), Duration.ofMillis(1000))
            val title = Title.title(Component.text(Font.Characters.COLOR_SCREEN).color(TextColor.color(0, 0, 0)), Component.empty(), times)
            (player as Audience).showTitle(title)
            player.world.spawnParticle(Particle.PORTAL, location.clone().add(0.0, 1.0, 0.0), 300, 0.2, 0.8, 0.2, 1.0)
            delay(30){
                player.teleport(location, TeleportFlag.EntityState.RETAIN_PASSENGERS, TeleportFlag.EntityState.RETAIN_VEHICLE)
            }
        }
    }

    /**
     * Sets a new link attempt for the player, giving them a cooldown.
     */
    fun setNewLinkAttempt(){
        val container = player.persistentDataContainer
        container.set(
            NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"), PersistentDataType.LONG, System.currentTimeMillis())
    }

    /**
     * Checks if a new linking attempt is available, or if the player is still on cooldown
     * @return true if the player can attempt a new linking session
     */
    fun isNewLinkingAttemptAvailable(): Boolean{
        val container = player.persistentDataContainer
        val cooldown = container.get(NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"), PersistentDataType.LONG)
            ?: return true
        if(System.currentTimeMillis() - cooldown < 82800000)
            return false // Still on cooldown
        return true // Difference in time is bigger than 1 day -> May attempt new linking session
    }

    /**
     * Gets the time left on the linking cooldown
     */
    fun getLinkingCooldown(): String{
        val container = player.persistentDataContainer
        val cooldown = container.get(NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"), PersistentDataType.LONG)!! + 82800000 - System.currentTimeMillis()
        val format = SimpleDateFormat("kk:mm:ss")
        val args = format.format(Date(cooldown)).split(":")
        return "${args[0]}h ${args[1]}m ${args[2]}s"
    }

    /**
     * Resets the linking cooldown
     */
    fun resetLinkingCooldown(){
        val container = player.persistentDataContainer
        container.remove(NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"),)
    }

    /**
     * Send a message to the player which is formatted.
     */
    fun sendMessage(string: String) {
        player.sendMessage(Colors.format(string))
    }

    /**
     * Send a message to the player which is formatted with a specific message type.
     */
    fun sendMessage(string: String, messageType: MessageType) {
        player.sendMessage(Colors.format(string, messageType))
    }

    /**
     * Send a message to the player which is formatted with a specific message location.
     */
    fun sendMessage(string: String, messageLocation: MessageLocation) {
        if (messageLocation == MessageLocation.CHAT) player.sendMessage(Colors.format(string))
        if (messageLocation == MessageLocation.HOTBAR) player.spigot().sendMessage(
            ChatMessageType.ACTION_BAR, TextComponent(
                Colors.format(string)
            )
        )
        if (messageLocation == MessageLocation.TITLE) player.sendTitle(Colors.format(string), "", 10, 50, 10)
        if (messageLocation == MessageLocation.SUBTITLE) player.sendTitle("", Colors.format(string), 10, 50, 10)
    }

    /**
     * Send a message to the player which is formatted with a specific color.
     */
    fun sendMessage(s: String, color: String) {
        this.sendMessage(Colors.format(color + s))
    }

    /**
     * Sets the player's gamemode and sends a gamemode message.
     */
    fun setGameMode(gameMode: GameMode) {
        player.gameMode = gameMode
        this.sendMessage(
            "Your gamemode has changed to: " + gameMode.toString().lowercase(Locale.getDefault()),
            MessageType.BACKGROUND
        )
    }

    /**
     * Checks if the player is in a game.
     */
    val isInGame: Boolean
        get() {
            val container = player.persistentDataContainer
            val name = container.get(NamespacedKey(IngeniaMC.plugin, "current-game"), PersistentDataType.STRING) ?: return false
            try{
                PhysicalGameType.valueOf(name)
            }catch (ex: Exception){
                return false
            }
            return true
        }

    /**
     * Gets and sets the player's current game.
     */
    var game: PhysicalGameType?
        get(){
            val container = player.persistentDataContainer
            val name =
                container.get(NamespacedKey(IngeniaMC.plugin, "current-game"), PersistentDataType.STRING) ?: return null
            return try {
                PhysicalGameType.valueOf(name)
            }catch (_: IllegalArgumentException){
                null
            }
        }
        set(value) {
            val container = player.persistentDataContainer
            container.set(
                NamespacedKey(IngeniaMC.plugin, "current-game"), PersistentDataType.STRING, value.toString()
            )
        }

    /**
     * Checks if the player is in a game leaving state. Or sets the state.
     */
    var isInGameLeavingState: Boolean
        get() {
            val container = player.persistentDataContainer
            val name = container.get(NamespacedKey(IngeniaMC.plugin, "is-in-game-leaving-state"), PersistentDataType.STRING)
            return name.toBoolean()
        }
        set(value) {
            val container = player.persistentDataContainer
            container.set(
                NamespacedKey(IngeniaMC.plugin, "is-in-game-leaving-state"), PersistentDataType.STRING, value.toString()
            )
        }

    /**
     * Gets and sets the player's experience.
     */
    var exp: Long
        get() = playerConfig.getExp()
        set(l) {
            if (isLevelUp(exp, l)) levelUp(exp, l)
            playerConfig.setExp(l)
            val receiveExpEvent = ReceiveExpEvent(player, l - exp)
            Bukkit.getPluginManager().callEvent(receiveExpEvent)
        }

    /**
     * Adds experience to the player.
     */
    fun addExp(l: Long) {
        if (isLevelUp(exp, exp + l)) levelUp(exp, exp + l)
        val newExp = l + exp
        playerConfig.setExp(newExp)
        val receiveExpEvent = ReceiveExpEvent(player, l)
        Bukkit.getPluginManager().callEvent(receiveExpEvent)
    }

    /**
     * Gets and sets the player's balance.
     */
    var bal: Long
        get() = playerConfig.getBal()
        set(l) {
            playerConfig.setBal(l)
            val receiveGoldenStarsEvent = ReceiveGoldenStarsEvent(player, l - bal)
            Bukkit.getPluginManager().callEvent(receiveGoldenStarsEvent)
        }

    /**
     * Adds balance to the player.
     */
    fun addBal(l: Long) {
        playerConfig.setBal(l + bal)
        val receiveGoldenStarsEvent = ReceiveGoldenStarsEvent(player, l)
        Bukkit.getPluginManager().callEvent(receiveGoldenStarsEvent)
    }

    /**
     * Gets the player's level.
     */
    fun getLevel(): Int{
        return getLevel(exp)
    }

    /**
     * Updates the player's year and playtime.
     */
    fun updateYearPlaytime(){
        val year = Calendar.getInstance().get(Calendar.YEAR)
        var reducer = 0
        var i = 1
        while(playerConfig.getYearPlaytime(year - i) != 0){
            reducer += playerConfig.getYearPlaytime(year - i)
            i++
        }

        playerConfig.setYearPlaytime(year, player.getStatistic(Statistic.PLAY_ONE_MINUTE) - reducer)
    }

    /**
     * Updates the player's playtime.
     */
    fun updatePlaytime(){
        playerConfig.setPlaytime(player.getStatistic(Statistic.PLAY_ONE_MINUTE))
    }

    /**
     * Checks if the player can level up, and if so, execute all the things that need to be executed.
     */
    private fun levelUp(previousExp: Long, newExp: Long) {
        for (level in getLevelUpLevels(previousExp, newExp)) {
            for (reward in getRewards(level)) {
                reward.execute(this)
            }
        }

        var rewardDisplay = ""
        for(reward in getRewards(getLevel(newExp))){
            if(rewardDisplay == ""){
                rewardDisplay = reward.getDisplay()
            }else{
                rewardDisplay += "${MessageType.BACKGROUND}, ${reward.getDisplay()}"
            }
        }

        playerConfig.setLevel(getLevel(newExp))

        player.sendMessage(Colors.format(ChatIcons.LEVEL_UP))
        player.sendMessage(Colors.format("               ${MessageType.SUCCESS}&lLevel Up"))
        player.sendMessage(Colors.format("               ${MessageType.BACKGROUND}Level ${getLevel(previousExp)} ➜ ${getLevel(newExp)}"))
        player.sendMessage(Colors.format("               ${MessageType.BACKGROUND}Rewards: "))
        player.sendMessage(Colors.format("               $rewardDisplay"))
        player.sendMessage(" ")

    }

    /**
     * Checks if the player can level up, and if so, execute all the things that need to be executed.
     */
    private fun levelUp(previousLevel: Int, newExp: Long) {
        levelUp(LevelUtils.getExpRequirement(previousLevel), newExp)
    }

    /**
     * Gets and sets if the player can damage.
     */
    var allowDamage: Boolean
        get() {
            val container = player.persistentDataContainer
            return container.get(NamespacedKey(IngeniaMC.plugin, "allow-damage"), PersistentDataType.STRING).toBoolean()
        }
        set(value) {
            val container = player.persistentDataContainer
            container.set(
                NamespacedKey(IngeniaMC.plugin, "allow-damage"), PersistentDataType.STRING, "$value")
        }

    /**
     * Gets the player's wands.
     */
    val wands: List<ItemStack>
        get() = getAccessibleWands(player)

    /**
     * Sets the currently selected wand.
     */
    fun setWand(item: ItemStack?) {
        player.inventory.setItem(3, item)
    }

    /**
     * Gets and sets the player's join message.
     */
    var joinMessage: String?
        get() = playerConfig.getJoinMessage()
        set(id) {
            playerConfig.setJoinMessage(id)
        }

    /**
     * Gets and sets the player's leave message.
     */
    var leaveMessage: String?
        get() = playerConfig.getLeaveMessage()
        set(id) {
            playerConfig.setLeaveMessage(id)
        }

    /**
     * Gets and sets the player's join color.
     */
    var joinColor: String?
        get() = playerConfig.getJoinColor()
        set(id) {
            playerConfig.setJoinColor(id)
        }

    /**
     * Gets and sets the player's leave color.
     */
    var leaveColor: String?
        get() = playerConfig.getLeaveColor()
        set(id) {
            playerConfig.setLeaveColor(id)
        }

    /**
     * Gets and sets the player's bodywear.
     */
    var bodyWearId: String?
        get() = playerConfig.getBodyWearId()
        set(id) {
            playerConfig.setBodyWearId(id)
        }

    /**
     * Opens an inventory and saves the previous one in a variable.
     */
    fun openInventory(inventory: Inventory?) {
        Bukkit.getScheduler().runTask(IngeniaMC.plugin, Runnable {
            if (player.openInventory.type != InventoryType.CRAFTING) previousInventory =
                player.openInventory.topInventory
            player.openInventory(inventory!!)
        })
    }

    /**
     * Give the Ingenia menu item to the player.
     */
    fun giveMenuItem() {
        val itemStack = ItemStack(Material.NETHER_STAR)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.setDisplayName(Colors.format("#f4b734&lɪɴɢᴇɴɪᴀᴍᴄ ᴍᴇɴᴜ"))
        itemMeta.lore =
            listOf(Colors.format(MessageType.LORE + "Click to open the IngeniaMC menu."))
        itemMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING, "main")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(0, itemStack)
    }

    /**
     * Give the rides item to the player.
     */
    fun giveRidesItem() {
        val itemStack = ItemStack(Material.MINECART)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.setDisplayName(Colors.format("#f4b734&lʀɪᴅᴇѕ"))
        itemMeta.lore =
            listOf(Colors.format(MessageType.LORE + "Click to open the rides menu."))
        itemMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING, "rides")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(1, itemStack)
    }

    /**
     * Give the shops item to the player.
     */
    fun giveShopsItem() {
        val itemStack = ItemStack(Material.ENDER_CHEST)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.setDisplayName(Colors.format("#f4b734&lѕʜᴏᴘѕ"))
        itemMeta.lore =
            listOf(Colors.format(MessageType.LORE + "Click to open the shops menu."))
        itemMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING, "shops")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(2, itemStack)
    }

    /**
     * Set the player's previous location. (Used for /back)
     */
    fun setPreviousLocation(location: Location) {
        val container = player.persistentDataContainer
        container.set(
            NamespacedKey(IngeniaMC.plugin, "previous-location"), PersistentDataType.STRING,
            location.world!!.name + ", " +
                    location.x + ", " +
                    location.y + ", " +
                    location.z + ", " +
                    location.yaw + ", " +
                    location.pitch
        )
    }

    /**
     * Gets the player's previous location. (Used for /back)
     */
    val previousLocation: Location?
        get() {
            val container = player.persistentDataContainer
            val string = container.get(NamespacedKey(IngeniaMC.plugin, "previous-location"), PersistentDataType.STRING)
            return if (string != null) {
                getLocationFromString(string)
            } else null
        }

    /**
     * Give a permission to the player.
     */
    fun givePermission(permission: String) {
        Bukkit.getScheduler().runTask(IngeniaMC.plugin, Runnable {
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "lp user " + player.name + " permission set " + permission + " true"
            )
        })
    }

    /**
     * Start a parkour.
     */
    fun startParkour(parkour: Parkour){
        player.velocity = Vector(0.0, 0.0, 0.0)
        game = PhysicalGameType.PARKOUR
        isInGameLeavingState = false
        sendMessage(MessageType.PLAYER_UPDATE + "Use '/leave' to cancel the parkour.")

        object: BukkitRunnable(){

            val startTime = System.currentTimeMillis()
            var currentTime = System.currentTimeMillis()

            override fun run() {
                val timeDisplay = Times.formatTime(currentTime - startTime)

                //Display normal time in actionbar
                (player as Audience).sendActionBar(Component.text("${parkour.displayName} current time: $timeDisplay").color(TextColor
                    .fromHexString(MessageType.PLAYER_UPDATE)))

                //If player is at end
                if(player.location.distanceSquared(parkour.endLocation!!) <= parkour.endRadius){

                    // Reload the config before saving it, so that changes made beforehand will be fixed
                    parkour.reload()

                    // Update leaderboard
                    if(parkour.getLeaderboard().getRecord(player) == 0L || parkour.getLeaderboard().getRecord(player) > (currentTime - startTime)) {
                        sendMessage(MessageType.PLAYER_UPDATE + "&lNEW RECORD!")
                        parkour.getLeaderboard().setRecord(player, currentTime - startTime)
                        parkour.getLeaderboard().spawnSign()
                    }

                    (player as Audience).sendActionBar(Component.text("${parkour.displayName} finished in: $timeDisplay").color(TextColor
                        .fromHexString(MessageType.PLAYER_UPDATE)))

                    sendMessage(MessageType.PLAYER_UPDATE + "${parkour.displayName} has been finished in $timeDisplay.")

                    player.playSound(player.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1f)

                    player.sendTitle(Colors.format(MessageType.PLAYER_UPDATE + parkour.displayName),
                        Colors.format(MessageType.PLAYER_UPDATE + timeDisplay),
                        10,
                        50,
                        10)

                    game = null

                    this.cancel()
                    return
                }

                if(isInGameLeavingState){
                    player.sendTitle(Colors.format(MessageType.PLAYER_UPDATE + parkour.displayName),
                        Colors.format(MessageType.ERROR + "Cancelled"),
                        10,
                        50,
                        10)
                    sendMessage(MessageType.PLAYER_UPDATE + "You left the parkour.")

                    game = null
                    isInGameLeavingState = false

                    this.cancel()
                    return
                }

                currentTime = System.currentTimeMillis()
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)
    }

    /**
     * Update the main boss bar.
     */
    fun updateMainBossBar(){
        val area = if(currentAreaName != null) Area(currentAreaName!!.split("/")[0], currentAreaName!!.split("/")[1]).displayName else "Not in area"
        val areaText = ArrayList<Pair<Char, Int>>()
        area.forEach { if(CharWidth.getAsMap().containsKey(it)) areaText.add(Pair(it, CharWidth.getAsMap()[it]!!)) }

        val rankRaw = rawPrefix
        val rank = componentPrefix
        val rankText = ArrayList<Pair<Char, Int>>()
        rankRaw.forEach { if(CharWidth.getAsMap().containsKey(it)) rankText.add(Pair(it, CharWidth.getAsMap()[it]!!)) }

        val stars = " ${NumberFormat.getInstance(Locale.getDefault()).format(bal)}"
        val starsText = ArrayList<Pair<Char, Int>>()
        stars.forEach { if(CharWidth.getAsMap().containsKey(it)) starsText.add(Pair(it, CharWidth.getAsMap()[it]!!)) }

        val bossBar = BossBarPlayerRegistry.getBossBar(this.player, BossBarIndex.FIRST) ?: return
        bossBar.name(
            Component.text("\uE022\uF801") // Left Part
                .append(Component.text().content(areaText.joinToString("") { '\uEF00'.plus(it.second).toString() + "\uF801" }).font(Key.key("minecraft:default")))
                .append(Component.text().content("\uE023\uF806\uF806")) // Right part
                .append(Component.text().content(areaText.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
                .append(Component.text().content(areaText.map { it.first }.joinToString("")).font(Key.key("ingeniamc:ui_top")))
                .append(Component.text("\uF826\uF826"))

                .append(Component.text("\uF828\uE022\uF801"))
                .append(Component.text().content(rankText.joinToString("") { '\uEF00'.plus(it.second + 1).toString() + "\uF801" }).font(Key.key("minecraft:default")))
                .append(Component.text().content("\uE023\uF806\uF806")) // Right part
                .append(Component.text().content(rankText.map { '\uF800'.plus(it.second + 1) }.joinToString("")).font(Key.key("minecraft:default")))
                .append(rank.font(Key.key("ingeniamc:ui_top")))
                .append(Component.text("\uF826\uF826"))

                .append(Component.text("\uF828\uE022\uF801\uEF07\uF801")) // extra space - left part - 7px part for star - 1 back
                .append(Component.text().content(starsText.joinToString("") { '\uEF00'.plus(it.second).toString() + "\uF801" }).font(Key.key("minecraft:default")))
                .append(Component.text().content("\uE023\uF806\uF806")) // Right part
                .append(Component.text().content(starsText.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
                .append(Component.text("\uF807\uE024")) // Star icon negative space and star icon
                .append(Component.text().content(starsText.map { it.first }.joinToString("")).font(Key.key("ingeniamc:ui_top")))
                .append(Component.text("\uF826\uF826"))

        )
    }

    /**
     * Sets the boss bar of the player.
     *
     * @param bossBarIndex The index of the boss bar.
     * @param component The component of which the title needs to be set to.
     * @param enable If the boss bar should be enabled.
     */
    fun setBossBar(bossBarIndex: BossBarIndex, component: Component?, enable: Boolean){
        val bossBar = BossBarPlayerRegistry.getBossBar(this.player, bossBarIndex) ?: return
        if(!enable){
            BossBarPlayerRegistry.hideBossBar(player, bossBarIndex)
        }else
            BossBarPlayerRegistry.showBossBar(player, bossBarIndex)
        bossBar.name(component ?: Component.empty())
    }

}