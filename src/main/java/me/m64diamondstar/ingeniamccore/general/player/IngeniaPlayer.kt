package me.m64diamondstar.ingeniamccore.general.player

import me.m64diamondstar.ingeniamccore.general.player.levels.LevelUtils.isLevelUp
import me.m64diamondstar.ingeniamccore.general.player.levels.LevelUtils.getLevelUpLevels
import me.m64diamondstar.ingeniamccore.general.player.levels.LevelUtils.getRewards
import me.m64diamondstar.ingeniamccore.wands.Wands.getAccessibleWands
import me.m64diamondstar.ingeniamccore.utils.LocationUtils.getLocationFromString
import me.m64diamondstar.ingeniamccore.data.files.PlayerConfig
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation
import me.m64diamondstar.ingeniamccore.general.tablist.TabList
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.levels.LevelUtils.getLevel
import me.m64diamondstar.ingeniamccore.general.scoreboard.Scoreboard
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class IngeniaPlayer(val player: Player) {
    private var scoreboard: Scoreboard? = null
    private var config: PlayerConfig
    private var previousInventory: Inventory? = null

    private fun getConfig(): PlayerConfig {
        config = PlayerConfig(player.uniqueId)
        return config
    }

    fun startUp() {
        setScoreboard(true)
        setTablist(true)
        giveMenuItem()
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

    var exp: Long
        get() = getConfig().getExp()
        set(l) {
            if (isLevelUp(exp, l)) levelUp(exp, l)
            getConfig().setExp(l)
        }

    fun addExp(l: Long) {
        if (isLevelUp(exp, exp + l)) levelUp(exp, exp + l)
        getConfig().setExp(l + exp)
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

    init {
        config = PlayerConfig(player.uniqueId)
    }
}