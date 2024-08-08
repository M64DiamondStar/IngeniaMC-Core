package me.m64diamondstar.ingeniamccore.general.player

import io.papermc.paper.entity.TeleportFlag
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
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
import me.m64diamondstar.ingeniamccore.utils.PlayerSelectors
import me.m64diamondstar.ingeniamccore.utils.Times
import me.m64diamondstar.ingeniamccore.utils.entities.BodyWearEntity
import me.m64diamondstar.ingeniamccore.utils.entities.BodyWearRegistry
import me.m64diamondstar.ingeniamccore.utils.entities.NametagEntity
import me.m64diamondstar.ingeniamccore.utils.event.player.ReceiveExpEvent
import me.m64diamondstar.ingeniamccore.utils.event.player.ReceiveGoldenStarsEvent
import me.m64diamondstar.ingeniamccore.utils.messages.ChatIcons
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
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
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class IngeniaPlayer(val player: Player) {
    private var previousInventory: Inventory? = null
    val playerConfig: PlayerConfig = PlayerConfig(player.uniqueId)

    fun startUp() {
        game = null
        allowDamage = false
        player.setGravity(true)
        player.walkSpeed = 0.2f
        player.fallDistance = 0f
        Bukkit.getScheduler().runTask(IngeniaMC.plugin, Runnable {
            player.removePotionEffect(PotionEffectType.SLOWNESS)
        })

        player.teleport(WarpUtils.getNearestLocation(player), TeleportFlag.EntityState.RETAIN_PASSENGERS)

        IngeniaMC.scoreboardTeamManager.addPlayerToTeam(player)

        val tabCompletions = ArrayList<String>()
        tabCompletions.addAll(EmojiUtils.getAllEmojiKeys())
        tabCompletions.addAll(Bukkit.getOnlinePlayers().map { it.name })
        player.setCustomChatCompletions(tabCompletions)

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

        val currentLevel = getLevel()
        val necessaryExp = LevelUtils.getExpRequirement(currentLevel + 1) - LevelUtils.getExpRequirement(currentLevel)
        val currentExp = exp - LevelUtils.getExpRequirement(currentLevel)
        player.level = getLevel()
        val visualExp = currentExp.toFloat() / necessaryExp
        player.exp = if(0 < visualExp && visualExp < 1) currentExp.toFloat() / necessaryExp else 0.01f

        if(joinColor == null)
            joinColor = "default"
        if(leaveColor == null)
            leaveColor = "default"
        if(joinMessage == null)
            joinMessage = "default"
        if(leaveMessage == null)
            leaveMessage = "default"

        AttractionUtils.getAllAttractions().forEach { it.spawnRidecountSign(player) }
        SplashBattleUtils.getAllSplashBattles().forEach { it.getLeaderboard().spawnSoaksSign(player) }
        ParkourUtils.getAllParkours().forEach { it.getLeaderboard().spawnSign(player) }

        if (isLevelUp(playerConfig.getLevel(), playerConfig.getExp()))
            levelUp(playerConfig.getLevel(), playerConfig.getExp())

        if(!(player.hasPermission("ingenia.team") || player.hasPermission("ingenia.team-trial")) && !player.isOp)
            player.gameMode = GameMode.ADVENTURE

        // Check title
        if(NametagEntity.Registry.contains(player.uniqueId)){
            NametagEntity.Registry.get(player.uniqueId)!!.remove()
            NametagEntity.Registry.remove(player.uniqueId)
        }

        val nametagEntity = NametagEntity(player.world, player.location, player)
        nametagEntity.setTitle(
            Component.text()
                .append(componentIconPrefix)
                .append(Component.text(" "))
                .append(nameLightColored)
                .build()
        )
        NametagEntity.Registry.add(player.uniqueId, nametagEntity)

        // Check body wear
        if(BodyWearRegistry.contains(player.uniqueId)){
            BodyWearRegistry.get(player.uniqueId)!!.remove()
            BodyWearRegistry.remove(player.uniqueId)
        }

        val bodyWearEntity = BodyWearEntity(player.world, player.location, player)
        val cosmeticPlayer = CosmeticPlayer(player)
        BodyWearRegistry.add(player.uniqueId, bodyWearEntity)
        if(this.bodyWearId != null){
            cosmeticPlayer.getEquipment().equipCosmetic(CosmeticType.BODY_WEAR, this.bodyWearId!!)
        }

        // Check other online player's settings
        Bukkit.getOnlinePlayers().forEach {
            val otherIngeniaPlayer = IngeniaPlayer(it)
            if((otherIngeniaPlayer.playerConfig.getShowPlayers() == PlayerSelectors.STAFF && !it.hasPermission("ingenia.team") && !it.hasPermission("ingenia.team-trial") && !it.isOp)
                || otherIngeniaPlayer.playerConfig.getShowPlayers() == PlayerSelectors.NONE) {
                it.hidePlayer(IngeniaMC.plugin, player)
                return@forEach
            }
            else if((otherIngeniaPlayer.playerConfig.getShowNametags() == PlayerSelectors.STAFF && !it.hasPermission("ingenia.team") && !it.hasPermission("ingenia.team-trial") && !it.isOp)
                || otherIngeniaPlayer.playerConfig.getShowNametags() == PlayerSelectors.NONE) {
                return@forEach
            }
            nametagEntity.spawn(it)
        }

    }

    val name: String
        get() = player.name
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
                container.set(
                    NamespacedKey(IngeniaMC.plugin, "current-area"), PersistentDataType.STRING, "$value")
        }

    var rideSeat: UUID?
        get() {
            val container = player.persistentDataContainer
            val uuid = container.get(NamespacedKey(IngeniaMC.plugin, "ride-seat"), PersistentDataType.STRING)
            if(uuid.equals("null", ignoreCase = true))
                return null
            return UUID.fromString(uuid)
        }
        set(uuid) {
            val container = player.persistentDataContainer
            if(uuid == null)
                container.remove(
                    NamespacedKey(IngeniaMC.plugin, "current-area"))
            else
                container.set(
                    NamespacedKey(IngeniaMC.plugin, "current-area"), PersistentDataType.STRING, "$uuid")
        }

    fun sendMessage(string: String) {
        player.sendMessage(Colors.format(string))
    }

    fun setNewLinkAttempt(){
        val container = player.persistentDataContainer
        container.set(
            NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"), PersistentDataType.LONG, System.currentTimeMillis())
    }

    fun isNewLinkingAttemptAvailable(): Boolean{
        val container = player.persistentDataContainer
        val cooldown = container.get(NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"), PersistentDataType.LONG)
            ?: return true
        if(System.currentTimeMillis() - cooldown < 82800000)
            return false // Still on cooldown
        return true // Difference in time is bigger than 1 day -> May attempt new linking session
    }

    fun getLinkingCooldown(): String{
        val container = player.persistentDataContainer
        val cooldown = container.get(NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"), PersistentDataType.LONG)!! + 82800000 - System.currentTimeMillis()
        val format = SimpleDateFormat("kk:mm:ss")
        val args = format.format(Date(cooldown)).split(":")
        return "${args[0]}h ${args[1]}m ${args[2]}s"
    }

    fun resetLinkingCooldown(){
        val container = player.persistentDataContainer
        container.remove(NamespacedKey(IngeniaMC.plugin, "discord-link-cooldown"),)
    }

    fun sendMessage(string: String, messageType: MessageType) {
        player.sendMessage(Colors.format(string, messageType))
    }

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

    fun setGameMode(gameMode: GameMode) {
        player.gameMode = gameMode
        this.sendMessage(
            "Your gamemode has changed to: " + gameMode.toString().lowercase(Locale.getDefault()),
            MessageType.BACKGROUND
        )
    }

    fun sendMessage(s: String, color: String) {
        this.sendMessage(Colors.format(color + s))
    }

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

    var game: PhysicalGameType?
        get(){
            val container = player.persistentDataContainer
            val name =
                container.get(NamespacedKey(IngeniaMC.plugin, "current-game"), PersistentDataType.STRING) ?: return null
            return try {
                PhysicalGameType.valueOf(name)
            }catch (ex: IllegalArgumentException){
                null
            }
        }
        set(value) {
            val container = player.persistentDataContainer
            container.set(
                NamespacedKey(IngeniaMC.plugin, "current-game"), PersistentDataType.STRING, value.toString()
            )
        }

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

    var exp: Long
        get() = playerConfig.getExp()
        set(l) {
            if (isLevelUp(exp, l)) levelUp(exp, l)
            playerConfig.setExp(l)
            val receiveExpEvent = ReceiveExpEvent(player, l - exp)
            Bukkit.getPluginManager().callEvent(receiveExpEvent)
        }

    fun addExp(l: Long) {
        if (isLevelUp(exp, exp + l)) levelUp(exp, exp + l)
        val newExp = l + exp
        playerConfig.setExp(newExp)
        val receiveExpEvent = ReceiveExpEvent(player, l)
        Bukkit.getPluginManager().callEvent(receiveExpEvent)
    }

    var bal: Long
        get() = playerConfig.getBal()
        set(l) {
            playerConfig.setBal(l)
            val receiveGoldenStarsEvent = ReceiveGoldenStarsEvent(player, l - bal)
            Bukkit.getPluginManager().callEvent(receiveGoldenStarsEvent)
        }

    fun addBal(l: Long) {
        playerConfig.setBal(l + bal)
        val receiveGoldenStarsEvent = ReceiveGoldenStarsEvent(player, l)
        Bukkit.getPluginManager().callEvent(receiveGoldenStarsEvent)
    }

    fun getLevel(): Int{
        return getLevel(exp)
    }

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

    fun updatePlaytime(){
        playerConfig.setPlaytime(player.getStatistic(Statistic.PLAY_ONE_MINUTE))
    }

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

    private fun levelUp(previousLevel: Int, newExp: Long) {
        levelUp(LevelUtils.getExpRequirement(previousLevel), newExp)
    }

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

    val wands: List<ItemStack>
        get() = getAccessibleWands(player)

    fun setWand(item: ItemStack?) {
        player.inventory.setItem(3, item)
    }

    var joinMessage: String?
        get() = playerConfig.getJoinMessage()
        set(id) {
            playerConfig.setJoinMessage(id)
        }
    var leaveMessage: String?
        get() = playerConfig.getLeaveMessage()
        set(id) {
            playerConfig.setLeaveMessage(id)
        }

    var joinColor: String?
        get() = playerConfig.getJoinColor()
        set(id) {
            playerConfig.setJoinColor(id)
        }

    var leaveColor: String?
        get() = playerConfig.getLeaveColor()
        set(id) {
            playerConfig.setLeaveColor(id)
        }

    var bodyWearId: String?
        get() = playerConfig.getBodyWearId()
        set(id) {
            playerConfig.setBodyWearId(id)
        }

    fun openInventory(inventory: Inventory?) {
        Bukkit.getScheduler().runTask(IngeniaMC.plugin, Runnable {
            if (player.openInventory.type != InventoryType.CRAFTING) previousInventory =
                player.openInventory.topInventory
            player.openInventory(inventory!!)
        })
    }

    fun giveMenuItem() {
        val itemStack = ItemStack(Material.NETHER_STAR)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.setDisplayName(Colors.format("#f4b734&lIngeniaMC Menu"))
        itemMeta.lore =
            listOf(Colors.format(MessageType.LORE + "Click to open the IngeniaMC menu."))
        itemMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING, "main")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(0, itemStack)
    }

    fun giveRidesItem() {
        val itemStack = ItemStack(Material.MINECART)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.setDisplayName(Colors.format("#f4b734&lRides"))
        itemMeta.lore =
            listOf(Colors.format(MessageType.LORE + "Click to open the rides menu."))
        itemMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING, "rides")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(1, itemStack)
    }

    fun giveShopsItem() {
        val itemStack = ItemStack(Material.ENDER_CHEST)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.setDisplayName(Colors.format("#f4b734&lShops"))
        itemMeta.lore =
            listOf(Colors.format(MessageType.LORE + "Click to open the shops menu."))
        itemMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "menu-item"), PersistentDataType.STRING, "shops")
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(2, itemStack)
    }

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

    val previousLocation: Location?
        get() {
            val container = player.persistentDataContainer
            val string = container.get(NamespacedKey(IngeniaMC.plugin, "previous-location"), PersistentDataType.STRING)
            return if (string != null) {
                getLocationFromString(string)
            } else null
        }

    fun givePermission(permission: String) {
        Bukkit.getScheduler().runTask(IngeniaMC.plugin, Runnable {
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "lp user " + player.name + " permission set " + permission + " true"
            )
        })
    }

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

    fun setBossBar(bossBarIndex: BossBarIndex, component: Component?, enable: Boolean){
        val bossBar = BossBarPlayerRegistry.getBossBar(this.player, bossBarIndex) ?: return
        if(!enable){
            BossBarPlayerRegistry.hideBossBar(player, bossBarIndex)
        }else
            BossBarPlayerRegistry.showBossBar(player, bossBarIndex)
        bossBar.name(component ?: Component.empty())
    }

}