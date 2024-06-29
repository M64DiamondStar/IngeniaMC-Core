package me.m64diamondstar.ingeniamccore.general.player.settings

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.bossbar.BossBarIndex
import me.m64diamondstar.ingeniamccore.general.inventory.MainInventory
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.PlayerSelectors
import me.m64diamondstar.ingeniamccore.utils.SettingButtons
import me.m64diamondstar.ingeniamccore.utils.entities.NametagEntity
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.collections.contains

class SettingsInventory(player: IngeniaPlayer): InventoryHandler(player) {

    private val row1 = intArrayOf(6, 7, 8)
    private val row2 = intArrayOf(15, 16, 17)
    private val row3 = intArrayOf(24, 25, 26)
    private val row4 = intArrayOf(33, 34, 35)
    // private val ROW_5 = intArrayOf(42, 43, 44)

    override fun setDisplayName(): Component {
        return Component.text("${Font.getGuiNegativeSpace(0)}\uEB04${Font.getGuiNegativeSpace(2)}" +
                (if(ingeniaPlayer.playerConfig.getShowSkinDuringDialogue()) SettingButtons.TOGGLE_1_ON.getUnicode() else SettingButtons.TOGGLE_1_OFF.getUnicode()) +
                Font.getGuiNegativeSpace(2) +
                (if(ingeniaPlayer.playerConfig.getShowHud()) SettingButtons.TOGGLE_2_ON.getUnicode() else SettingButtons.TOGGLE_2_OFF.getUnicode()) +
                Font.getGuiNegativeSpace(2) +
                (SettingButtons.getFromPlayerSelector(ingeniaPlayer.playerConfig.getShowNametags(), 3).getUnicode()) +
                Font.getGuiNegativeSpace(2) +
                (SettingButtons.getFromPlayerSelector(ingeniaPlayer.playerConfig.getShowPlayers(), 4).getUnicode()))
            .color(TextColor.color(255,255,255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return true
    }

    override fun onClick(event: InventoryClickEvent) {
        if(event.slot in row1 || event.slot in row2 || event.slot in row3 || event.slot in row4) {
            if (event.slot in row1) { // Show skin during dialogue
                ingeniaPlayer.playerConfig.setShowSkinDuringDialogue(ingeniaPlayer.playerConfig.getShowSkinDuringDialogue().not())
            }

            if (event.slot in row2) { // Show HUD
                ingeniaPlayer.playerConfig.setShowHud(ingeniaPlayer.playerConfig.getShowHud().not())
                if(!ingeniaPlayer.playerConfig.getShowHud()) {
                    ingeniaPlayer.setBossBar(BossBarIndex.FIRST, null, false)
                    ingeniaPlayer.setBossBar(BossBarIndex.SECOND, null, false)
                    ingeniaPlayer.setBossBar(BossBarIndex.THIRD, null, false)
                }else{
                    ingeniaPlayer.setBossBar(BossBarIndex.FIRST, null, true)
                    ingeniaPlayer.setBossBar(BossBarIndex.SECOND, null, true)
                    ingeniaPlayer.setBossBar(BossBarIndex.THIRD, null, true)
                    ingeniaPlayer.updateMainBossBar()
                }
            }

            if (event.slot in row3) { // Show nametags
                ingeniaPlayer.playerConfig.setShowNametags(ingeniaPlayer.playerConfig.getShowNametags().next())
                updateNametags()
            }

            if (event.slot in row4) { // Show players
                ingeniaPlayer.playerConfig.setShowPlayers(ingeniaPlayer.playerConfig.getShowPlayers().next())
                Bukkit.getOnlinePlayers().forEach {
                    if(it.uniqueId == ingeniaPlayer.player.uniqueId) return@forEach
                    when(ingeniaPlayer.playerConfig.getShowPlayers()){
                        PlayerSelectors.ALL -> {
                            ingeniaPlayer.player.showPlayer(IngeniaMC.plugin, it)
                        }
                        PlayerSelectors.STAFF -> {
                            if(it.hasPermission("ingenia.team") || it.hasPermission("ingenia.team-trial") || it.isOp) ingeniaPlayer.player.showPlayer(IngeniaMC.plugin, it)
                            else ingeniaPlayer.player.hidePlayer(IngeniaMC.plugin, it)
                        }
                        PlayerSelectors.NONE -> {
                            ingeniaPlayer.player.hidePlayer(IngeniaMC.plugin, it)
                        }
                    }
                }

                updateNametags()
            }

            val settingsInventory = SettingsInventory(ingeniaPlayer)
            settingsInventory.open()
        }

        if(event.slot == 49){
            val mainInventory = MainInventory(getPlayer(), 1)
            mainInventory.open()
        }
    }

    private fun updateNametags(){
        Bukkit.getOnlinePlayers().forEach {

            val nameTag = NametagEntity.Registry.get(it.uniqueId) ?: return@forEach

            when(ingeniaPlayer.playerConfig.getShowNametags()){
                PlayerSelectors.ALL -> {
                    if(ingeniaPlayer.playerConfig.getShowPlayers() == PlayerSelectors.STAFF && !it.hasPermission("ingenia.team") && !it.hasPermission("ingenia.team-trial") && !it.isOp) {
                        nameTag.remove(ingeniaPlayer.player)
                        return@forEach
                    }
                    if(ingeniaPlayer.playerConfig.getShowPlayers() == PlayerSelectors.NONE) {
                        nameTag.remove(ingeniaPlayer.player)
                        return@forEach
                    }
                    nameTag.spawn(ingeniaPlayer.player)
                }
                PlayerSelectors.STAFF -> {
                    if(ingeniaPlayer.playerConfig.getShowPlayers() == PlayerSelectors.NONE) {
                        nameTag.remove(ingeniaPlayer.player)
                        return@forEach
                    }
                    if(it.hasPermission("ingenia.team") || it.hasPermission("ingenia.team-trial") || it.isOp) nameTag.spawn(ingeniaPlayer.player)
                    else nameTag.remove(ingeniaPlayer.player)
                }
                PlayerSelectors.NONE -> {
                    nameTag.remove(ingeniaPlayer.player)
                }
            }
        }
    }

    override fun onOpen(event: InventoryOpenEvent) {
        val inventory = event.inventory

        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        transparentMeta.displayName(MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}><b>Go Back").decoration(TextDecoration.ITALIC, false))
        transparentMeta.lore(listOf(MiniMessage.miniMessage().deserialize("<${MessageType.LORE}>Click here to go back to the main menu.").decoration(TextDecoration.ITALIC, false)))
        transparentMeta.setCustomModelData(1)

        transparentItem.itemMeta = transparentMeta

        inventory.setItem(49, transparentItem)
    }

    override fun onClose(event: InventoryCloseEvent) {}

    override fun onDrag(event: InventoryDragEvent) {}


}