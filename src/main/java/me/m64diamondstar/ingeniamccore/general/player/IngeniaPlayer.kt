package me.m64diamondstar.ingeniamccore.general.player

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionUtils
import me.m64diamondstar.ingeniamccore.data.files.PlayerConfig
import me.m64diamondstar.ingeniamccore.games.PhysicalGameType
import me.m64diamondstar.ingeniamccore.games.parkour.Parkour
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.getLevel
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.getLevelUpLevels
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.getRewards
import me.m64diamondstar.ingeniamccore.general.levels.LevelUtils.isLevelUp
import me.m64diamondstar.ingeniamccore.general.scoreboard.Scoreboard
import me.m64diamondstar.ingeniamccore.general.tablist.TabList
import me.m64diamondstar.ingeniamccore.general.warps.WarpUtils
import me.m64diamondstar.ingeniamccore.utils.LocationUtils.getLocationFromString
import me.m64diamondstar.ingeniamccore.utils.TeamHandler
import me.m64diamondstar.ingeniamccore.utils.Times
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.wands.Wands.getAccessibleWands
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class IngeniaPlayer(val player: Player) {
    private var scoreboard: Scoreboard? = null
    private var previousInventory: Inventory? = null

    private fun getConfig(): PlayerConfig {
        return PlayerConfig(player.uniqueId)
    }

    fun startUp() {
        game = null
        allowDamage = false
        player.teleport(WarpUtils.getNearestLocation(player))
        setScoreboard(true)
        setTablist(true)
        giveMenuItem()
        TeamHandler.addPlayer(player)
        player.isCollidable = false
        if (player.isOp) player.setPlayerListName(Colors.format("#c43535&lLead #ffdede$name")) else if (player.hasPermission(
                "ingenia.team"
            )
        ) player.setPlayerListName(
            Colors.format(
                "#4180bf&lTeam #deefff$name"
            )
        ) else if (player.hasPermission("ingenia.vip+")) player.setPlayerListName(
            Colors.format(
                "#9054b0VIP+ #f9deff$name"
            )
        ) else if (player.hasPermission("ingenia.vip")) player.setPlayerListName(
            Colors.format(
                "#54b0b0VIP #defdff$name"
            )
        ) else player.setPlayerListName(Colors.format("#a1a1a1Visitor #cccccc$name"))

        AttractionUtils.getAllAttractions().forEach { it.spawnRidecountSign(player) }
    }

    fun shutDown(){

    }

    val name: String
        get() = player.name
    val prefix: String
        get() = if (player.isOp) Colors.format("#c43535&lLead") else if (player.hasPermission(
                "ingenia.team"
            )
        ) Colors.format("#4180bf&lTeam") else if (player.hasPermission("ingenia.vip+")) Colors.format(
            "#9054b0VIP+"
        ) else if (player.hasPermission("ingenia.vip")) Colors.format("#54b0b0VIP") else Colors.format(
            "#a1a1a1Visitor"
        )

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

    fun sendMessage(string: String?) {
        player.sendMessage(Colors.format(string))
    }

    fun sendMessage(string: String?, messageType: MessageType?) {
        player.sendMessage(Colors.format(string, messageType))
    }

    fun sendMessage(string: String?, messageLocation: MessageLocation) {
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
        get() = getConfig().getExp()
        set(l) {
            if (isLevelUp(exp, l)) levelUp(exp, l)
            getConfig().setExp(l)
        }

    fun addExp(l: Long) {
        if (isLevelUp(exp, exp + l)) levelUp(exp, exp + l)
        val newExp = l + exp
        getConfig().setExp(newExp)
    }

    fun getLevel(): Int{
        return getLevel(exp)
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

        player.sendMessage(Colors.format("\uFE01"))
        player.sendMessage(Colors.format("               ${MessageType.SUCCESS}&lLevel Up"))
        player.sendMessage(Colors.format("               ${MessageType.BACKGROUND}Level ${getLevel(previousExp)} ➡ ${getLevel(newExp)}"))
        player.sendMessage(Colors.format("               ${MessageType.BACKGROUND}Rewards: "))
        player.sendMessage(Colors.format("               $rewardDisplay"))
        player.sendMessage(" ")

    }

    var bal: Long
        get() = getConfig().getBal()
        set(l) {
            getConfig().setBal(l)
        }

    fun addBal(l: Long) {
        getConfig().setBal(l + bal)
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

    fun setScoreboard(on: Boolean) {
        if (scoreboard == null) scoreboard = Scoreboard(this)
        if (on) {
            scoreboard!!.createBoard()
            scoreboard!!.startUpdating()
            scoreboard!!.showBoard()
        } else {
            scoreboard!!.hideBoard()
        }
    }

    private fun setTablist(on: Boolean) {
        val tabList = TabList(IngeniaMC.plugin)
        if (on) {
            for (header in Objects.requireNonNull(IngeniaMC.plugin.config.getConfigurationSection("Tablist"))
                !!.getStringList("Header")) {
                tabList.addHeader(header, player)
            }
            for (footer in Objects.requireNonNull(IngeniaMC.plugin.config.getConfigurationSection("Tablist"))
                !!.getStringList("Footer")) {
                tabList.addFooter(
                    footer.replace(
                        "%online%", Bukkit.getOnlinePlayers().size.toString() + ""
                    )
                )
            }
        } else {
            tabList.clearHeader()
            tabList.clearFooter()
        }
        tabList.showTab(player)
    }

    val wands: List<ItemStack>
        get() = getAccessibleWands(player)

    fun setWand(item: ItemStack?) {
        player.inventory.setItem(5, item)
        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1.5f)
    }

    var joinMessage: String?
        get() = getConfig().getJoinMessage()!!.replace("%player%", Colors.format(getConfig().getJoinColor() + player.name + "#ababab"))
        set(msg) {
            getConfig().setJoinMessage(msg!!)
        }
    var leaveMessage: String?
        get() = getConfig().getLeaveMessage()!!.replace("%player%", Colors.format(getConfig().getJoinColor() + player.name + "#ababab"))
        set(msg) {
            getConfig().setLeaveMessage(msg!!)
        }

    fun openInventory(inventory: Inventory?) {
        if (player.openInventory.type != InventoryType.CRAFTING) previousInventory = player.openInventory.topInventory
        player.openInventory(inventory!!)
    }

    fun giveMenuItem() {
        val itemStack = ItemStack(Material.NETHER_STAR)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.setDisplayName(Colors.format("#f4b734&lIngeniaMC"))
        itemMeta.lore =
            listOf(Colors.format(MessageType.LORE + "Click to open the IngeniaMC menu."))
        itemStack.itemMeta = itemMeta
        player.inventory.setItem(4, itemStack)
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
        Bukkit.dispatchCommand(
            Bukkit.getConsoleSender(),
            "lp user " + player.name + " permission set " + permission + " true"
        )
    }

    fun startParkour(parkour: Parkour){

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
}