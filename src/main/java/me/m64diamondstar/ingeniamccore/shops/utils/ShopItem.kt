package me.m64diamondstar.ingeniamccore.shops.utils

import me.m64diamondstar.ingeniamccore.cosmetics.data.CosmeticItems
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveColor
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.wands.utils.WandRegistry
import me.m64diamondstar.ingeniamccore.wands.utils.Wands
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class ShopItem {
    HAT {
        override fun getDisplayName(): String {
            return "Hat"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addCosmetic(CosmeticType.HAT, id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return CosmeticItems(CosmeticType.HAT).getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return CosmeticItems(CosmeticType.HAT).getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasCosmetic(CosmeticType.HAT, id)
        }

        override fun getItemDisplayName(id: String): String {
            return CosmeticItems(CosmeticType.HAT).getItem(id)?.itemMeta?.displayName ?: "Hat"
        }
    },
    SHIRT {
        override fun getDisplayName(): String {
            return "Shirt"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addCosmetic(CosmeticType.SHIRT, id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return CosmeticItems(CosmeticType.SHIRT).getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return CosmeticItems(CosmeticType.SHIRT).getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasCosmetic(CosmeticType.SHIRT, id)
        }

        override fun getItemDisplayName(id: String): String {
            return CosmeticItems(CosmeticType.SHIRT).getItem(id)?.itemMeta?.displayName ?: "Shirt"
        }
    },
    PANTS {
        override fun getDisplayName(): String {
            return "Pants"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addCosmetic(CosmeticType.PANTS, id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return CosmeticItems(CosmeticType.PANTS).getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return CosmeticItems(CosmeticType.PANTS).getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasCosmetic(CosmeticType.PANTS, id)
        }

        override fun getItemDisplayName(id: String): String {
            return CosmeticItems(CosmeticType.PANTS).getItem(id)?.itemMeta?.displayName ?: "Pants"
        }
    },
    SHOES {
        override fun getDisplayName(): String {
            return "Shoes"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addCosmetic(CosmeticType.SHOES, id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return CosmeticItems(CosmeticType.SHOES).getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return CosmeticItems(CosmeticType.SHOES).getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasCosmetic(CosmeticType.SHOES, id)
        }

        override fun getItemDisplayName(id: String): String {
            return CosmeticItems(CosmeticType.SHOES).getItem(id)?.itemMeta?.displayName ?: "Shoes"
        }
    },
    BALLOON {
        override fun getDisplayName(): String {
            return "Balloon"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addCosmetic(CosmeticType.BALLOON, id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return CosmeticItems(CosmeticType.BALLOON).getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return CosmeticItems(CosmeticType.BALLOON).getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasCosmetic(CosmeticType.BALLOON, id)
        }

        override fun getItemDisplayName(id: String): String {
            return CosmeticItems(CosmeticType.BALLOON).getItem(id)?.itemMeta?.displayName ?: "Balloon"
        }
    },
    WAND {
        override fun getDisplayName(): String {
            return "Wand"
        }

        override fun givePlayer(player: Player, id: String) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} permission set ingeniawands.${id.lowercase()} true")
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return Wands.getAsItemStack(id)
        }

        override fun getAllItemIDs(): List<String> {
            val list = mutableListOf<String>()
            WandRegistry.getWands().forEach{ list.add(it.key) }
            return list
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            return player.hasPermission("ingeniawands.${id.lowercase()}")
        }

        override fun getItemDisplayName(id: String): String {
            return WandRegistry.getWand(id)?.getDisplayName() ?: "Wand"
        }
    },
    MESSAGE_COLOR {
        override fun getDisplayName(): String {
            return "Join/Leave Message Color"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addMessageColor(id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return JoinLeaveColor().getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return JoinLeaveColor().getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasMessageColor(id)
        }

        override fun getItemDisplayName(id: String): String {
            return JoinLeaveColor().getName(id)
        }
    },
    JOIN_MESSAGE {
        override fun getDisplayName(): String {
            return "Join Message"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addJoinMessage(id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return JoinLeaveMessage(MessageType.JOIN).getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return JoinLeaveMessage(MessageType.JOIN).getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasJoinMessage(id)
        }

        override fun getItemDisplayName(id: String): String {
            return JoinLeaveMessage(MessageType.JOIN).getName(id)
        }
    },
    LEAVE_MESSAGE {
        override fun getDisplayName(): String {
            return "Leave Message"
        }

        override fun givePlayer(player: Player, id: String) {
            val cosmeticPlayer = CosmeticPlayer(player)
            cosmeticPlayer.addLeaveMessage(id)
        }

        override fun getAsItemStack(id: String): ItemStack? {
            return JoinLeaveMessage(MessageType.LEAVE).getItem(id)
        }

        override fun getAllItemIDs(): List<String> {
            return JoinLeaveMessage(MessageType.LEAVE).getAllIDs()
        }

        override fun allowMultiple(): Boolean {
            return false
        }

        override fun alreadyBought(player: Player, id: String): Boolean {
            val cosmeticPlayer = CosmeticPlayer(player)
            return cosmeticPlayer.hasLeaveMessage(id)
        }

        override fun getItemDisplayName(id: String): String {
            return JoinLeaveMessage(MessageType.LEAVE).getName(id)
        }
    };

    abstract fun getDisplayName(): String
    abstract fun givePlayer(player: Player, id: String)
    abstract fun getAsItemStack(id: String): ItemStack?
    abstract fun getAllItemIDs(): List<String>
    abstract fun allowMultiple(): Boolean
    abstract fun alreadyBought(player: Player, id: String): Boolean
    abstract fun getItemDisplayName(id: String): String
}