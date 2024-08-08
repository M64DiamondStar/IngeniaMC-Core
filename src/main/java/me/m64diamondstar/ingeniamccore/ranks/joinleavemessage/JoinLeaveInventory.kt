package me.m64diamondstar.ingeniamccore.ranks.joinleavemessage

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveColor
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageBuilder
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.ranks.RankPerksInventory
import me.m64diamondstar.ingeniamccore.utils.gui.InventoryHandler
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.persistence.PersistentDataType

class JoinLeaveInventory(val player: Player, private var selected: Selected) : InventoryHandler(IngeniaPlayer(player)) {

    enum class Selected {
        JOIN, LEAVE
    }

    private val joinSlots = Array(6) {i -> if(i in  0..2) i + 3 else i + 9}
    private val leaveSlots = Array(6) {i -> if(i in  0..2) i + 21 else i + 27}
    private val previewSlots = Array(3) {i -> i + 48}
    private val resetSlots = Array(2) {i -> i + 52}

    private val colorScrollUp = 11
    private val colorScrollDown = 20

    private val messageScrollUp = 17
    private val messageScrollDown = 26

    private val colorSlots = intArrayOf(0, 1, 9, 10, 18, 19, 27, 28)
    private val messageSlots = intArrayOf(6, 7, 15, 16, 24, 25, 33, 34)

    private var colorScroll = 0
    private var messageScroll = 0

    override fun setDisplayName(): Component {
        return if(selected == Selected.JOIN) Component.text("\uF808\uEA01").color(TextColor.color(255, 255, 255))
        else Component.text("\uF808\uEA02").color(TextColor.color(255, 255, 255))
    }

    override fun setSize(): Int {
        return 54
    }

    override fun shouldCancel(): Boolean {
        return true
    }

    override fun onClick(event: InventoryClickEvent) {
        if(event.clickedInventory?.type == InventoryType.PLAYER) return
        val player = event.whoClicked as Player
        val inventory = event.inventory

        if(joinSlots.contains(event.slot) && selected == Selected.LEAVE){
            val joinLeaveInventory = JoinLeaveInventory(player, Selected.JOIN)
            joinLeaveInventory.open()
        }

        if(leaveSlots.contains(event.slot) && selected == Selected.JOIN){
            val joinLeaveInventory = JoinLeaveInventory(player, Selected.LEAVE)
            joinLeaveInventory.open()
        }

        if(previewSlots.contains(event.slot)){
            val ingeniaPlayer = IngeniaPlayer(player)

            player.sendMessage(" ")
            if(selected == Selected.JOIN){
                player.sendMessage(MessageBuilder.JoinMessageBuilder(player.name, ingeniaPlayer.joinColor, ingeniaPlayer.joinMessage)
                    .build())
            }else{
                player.sendMessage(MessageBuilder.LeaveMessageBuilder(player.name, ingeniaPlayer.leaveColor,ingeniaPlayer.leaveMessage)
                    .build())
            }
            player.sendMessage(" ")
            (player as Audience).sendMessage(
                MiniMessage.miniMessage().deserialize("<#f4b734>Click <reset><hover:show_text:\"Click me!\">" +
                        "<click:run_command:\"/edit${selected.toString().lowercase()}message\">here<reset> " +
                        "<#f4b734>to re-open the ${selected.toString().lowercase()} editor!")
            )
            player.closeInventory()
        }

        if(colorSlots.contains(event.slot)){
            val ingeniaPlayer = IngeniaPlayer(player)
            val meta = (event.currentItem ?: return).itemMeta ?: return
            val colorId = meta.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "color-id"), PersistentDataType.STRING) ?: return
            if(selected == Selected.JOIN){
                ingeniaPlayer.joinColor = colorId
                addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
            }else{
                ingeniaPlayer.leaveColor = colorId
                addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
            }
        }

        if(messageSlots.contains(event.slot)){
            val ingeniaPlayer = IngeniaPlayer(player)
            val meta = (event.currentItem ?: return).itemMeta ?: return
            val messageId = meta.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "message-id"), PersistentDataType.STRING) ?: return
            if(selected == Selected.JOIN){
                ingeniaPlayer.joinMessage = messageId
                addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
            }else{
                ingeniaPlayer.leaveMessage = messageId
                addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
            }
        }

        if(event.slot == colorScrollUp && event.currentItem != null && event.currentItem!!.type != Material.BARRIER){
            colorScroll--
            if(selected == Selected.JOIN)
                addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
            else
                addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
        }

        if(event.slot == colorScrollDown && event.currentItem != null && event.currentItem!!.type != Material.BARRIER){
            colorScroll++
            if(selected == Selected.JOIN)
                addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
            else
                addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
        }

        if(event.slot == messageScrollUp && event.currentItem != null && event.currentItem!!.type != Material.BARRIER){
            messageScroll--
            if(selected == Selected.JOIN)
                addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
            else
                addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
        }

        if(event.slot == messageScrollDown && event.currentItem != null && event.currentItem!!.type != Material.BARRIER){
            messageScroll++
            if(selected == Selected.JOIN)
                addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
            else
                addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
        }

        if(event.slot == 45){
            val rankPerksInventory = RankPerksInventory(player)
            rankPerksInventory.open()
        }
    }

    override fun onDrag(event: InventoryDragEvent) {

    }

    override fun onOpen(event: InventoryOpenEvent) {
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        val inventory = event.inventory

        transparentMeta.setCustomModelData(1)

        transparentMeta.setDisplayName(Colors.format("#AA8143Click to preview"))
        transparentItem.itemMeta = transparentMeta

        previewSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format("#AA8143Scroll up"))
        transparentItem.itemMeta = transparentMeta

        inventory.setItem(11, transparentItem)
        inventory.setItem(17, transparentItem)

        transparentMeta.setDisplayName(Colors.format("#AA8143Scroll down"))
        transparentItem.itemMeta = transparentMeta

        inventory.setItem(20, transparentItem)
        inventory.setItem(26, transparentItem)

        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lGo Back"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to go back to the main menu."))
        transparentItem.itemMeta = transparentMeta

        inventory.setItem(45, transparentItem)

        if(selected == Selected.JOIN)
            editJoin(inventory)
        else
            editLeave(inventory)
    }

    override fun onClose(event: InventoryCloseEvent) {}

    private fun addColors(messageType: me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType, inventory: Inventory){
        colorSlots.forEach { inventory.setItem(it, null) }

        val barrier = ItemStack(Material.BARRIER)
        val barrierMeta = barrier.itemMeta!!

        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        transparentMeta.setCustomModelData(1)

        val cosmeticPlayer = CosmeticPlayer(player)
        val joinLeaveColor = JoinLeaveColor()

        val colorItem = ItemStack(Material.LEATHER_HORSE_ARMOR)
        val colorMeta = colorItem.itemMeta as LeatherArmorMeta

        if(colorScroll == 0) {
            barrierMeta.setDisplayName(Colors.format("#AA8143Scroll up"))
            barrier.itemMeta = barrierMeta
            inventory.setItem(11, barrier)
        }else{
            transparentMeta.setDisplayName(Colors.format("#AA8143Scroll up"))
            transparentItem.itemMeta = transparentMeta
            inventory.setItem(11, transparentItem)
        }

        if(8 + colorScroll * 2 >= cosmeticPlayer.getAllMessageColors().size){
            barrierMeta.setDisplayName(Colors.format("#AA8143Scroll down"))
            barrier.itemMeta = barrierMeta
            inventory.setItem(20, barrier)
        }else{
            transparentMeta.setDisplayName(Colors.format("#AA8143Scroll down"))
            transparentItem.itemMeta = transparentMeta
            inventory.setItem(20, transparentItem)
        }

        for(i in 0 + colorScroll * 2..7 + colorScroll * 2){

            if(cosmeticPlayer.getAllMessageColors().size >= i + 1){
                val color = joinLeaveColor.getColor(cosmeticPlayer.getAllMessageColors().toList()[i])
                val hexColor = joinLeaveColor.getHexColor(cosmeticPlayer.getAllMessageColors().toList()[i])
                val name = joinLeaveColor.getName(cosmeticPlayer.getAllMessageColors().toList()[i])
                val equipped = cosmeticPlayer.getMessageEquipment()
                    .getEquippedColor(messageType) == cosmeticPlayer.getAllMessageColors().toList()[i]

                if(color != null) {

                    colorMeta.setCustomModelData(7000)
                    colorMeta.addItemFlags(ItemFlag.HIDE_DYE)
                    colorMeta.setColor(Color.fromRGB(color.red, color.green, color.blue))
                    colorMeta.setDisplayName(Colors.format("$hexColor&l$name"))
                    colorMeta.lore = listOf(
                        "",
                        if(equipped) Colors.format("${MessageType.ERROR}Equipped") else Colors.format("${MessageType.SUCCESS}Click to equip"),
                    )

                    if(equipped){
                        colorMeta.addEnchant(Enchantment.UNBREAKING, 1, true)
                        colorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    }else
                        colorMeta.removeEnchant(Enchantment.UNBREAKING)

                    colorMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "color-id"), PersistentDataType.STRING, cosmeticPlayer.getAllMessageColors().toList()[i])

                    colorItem.itemMeta = colorMeta
                    inventory.setItem(colorSlots[i - colorScroll * 2], colorItem)
                }
            }else break
        }
    }

    private fun addMessages(messageType: me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType, inventory: Inventory){
        messageSlots.forEach { inventory.setItem(it, null) }

        val barrier = ItemStack(Material.BARRIER)
        val barrierMeta = barrier.itemMeta!!

        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta
        transparentMeta.setCustomModelData(1)

        val cosmeticPlayer = CosmeticPlayer(player)
        val joinLeaveMessage = JoinLeaveMessage(messageType)

        val messageItem = ItemStack(Material.FEATHER)
        val messageMeta = messageItem.itemMeta!!

        if(messageScroll == 0) {
            barrierMeta.setDisplayName(Colors.format("#AA8143Scroll up"))
            barrier.itemMeta = barrierMeta
            inventory.setItem(17, barrier)
        }else{
            transparentMeta.setDisplayName(Colors.format("#AA8143Scroll up"))
            transparentItem.itemMeta = transparentMeta
            inventory.setItem(17, transparentItem)
        }

        if(8 + messageScroll * 2 >=
            if(messageType == me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN)
                cosmeticPlayer.getAllJoinMessages().size
            else
                cosmeticPlayer.getAllLeaveMessages().size
            ){
            barrierMeta.setDisplayName(Colors.format("#AA8143Scroll down"))
            barrier.itemMeta = barrierMeta
            inventory.setItem(26, barrier)
        }else{
            transparentMeta.setDisplayName(Colors.format("#AA8143Scroll down"))
            transparentItem.itemMeta = transparentMeta
            inventory.setItem(26, transparentItem)
        }

        for(i in 0 + messageScroll * 2..7 + messageScroll * 2){
            if((if(messageType == me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN)
                cosmeticPlayer.getAllJoinMessages().size
                else
                    cosmeticPlayer.getAllLeaveMessages().size) >= i + 1){
                val id = if(messageType == me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN)
                    cosmeticPlayer.getAllJoinMessages().toList()[i]
                else
                    cosmeticPlayer.getAllLeaveMessages().toList()[i]
                val message = joinLeaveMessage.getMessage(id)
                val name = joinLeaveMessage.getName(id)
                val equipped = cosmeticPlayer.getMessageEquipment()
                    .getEquippedId(messageType) == id

                if(message != null) {
                    messageMeta.setDisplayName(Colors.format("${MessageType.INFO}&l$name"))
                    messageMeta.lore = listOf(
                        "",
                        Colors.format("${MessageType.DEFAULT}${message.replace("%player%", player.name)}"),
                        if(equipped) Colors.format("${MessageType.ERROR}Equipped") else Colors.format("${MessageType.SUCCESS}Click to equip"),
                    )

                    if(messageType == me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN)
                        messageMeta.setCustomModelData(2)
                    else
                        messageMeta.setCustomModelData(3)

                    if(equipped){
                        messageMeta.addEnchant(Enchantment.UNBREAKING, 1, true)
                        messageMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    }else
                        messageMeta.removeEnchant(Enchantment.UNBREAKING)

                    messageMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "message-id"), PersistentDataType.STRING, id)

                    messageItem.itemMeta = messageMeta
                    inventory.setItem(messageSlots[i - messageScroll * 2], messageItem)
                }
            }else break
        }
    }

    /**
     * Put the join items in the inventory
     */
    private fun editJoin(inventory: Inventory){
        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta

        transparentMeta.setCustomModelData(1)


        transparentMeta.setDisplayName(Colors.format("#5eab59Selected"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.BACKGROUND + ""),
            Colors.format(MessageType.BACKGROUND + "You're currently editing your"),
            Colors.format(MessageType.BACKGROUND + "join message!")
        )

        transparentItem.itemMeta = transparentMeta

        joinSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format("#5eab59Click to select"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.BACKGROUND + ""),
            Colors.format(MessageType.BACKGROUND + "Click to start editing your"),
            Colors.format(MessageType.BACKGROUND + "leave message!")
        )

        transparentItem.itemMeta = transparentMeta

        leaveSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lReset"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to reset your join message."))

        transparentItem.itemMeta = transparentMeta

        resetSlots.forEach { inventory.setItem(it, transparentItem) }

        addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
        addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN, inventory)
    }

    /**
     * Put the leave items in the inventory
     */
    private fun editLeave(inventory: Inventory){

        val transparentItem = ItemStack(Material.FEATHER)
        val transparentMeta = transparentItem.itemMeta as ItemMeta

        transparentMeta.setCustomModelData(1)


        transparentMeta.setDisplayName(Colors.format("#5eab59Click to select"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.BACKGROUND + ""),
            Colors.format(MessageType.BACKGROUND + "Click to start editing your"),
            Colors.format(MessageType.BACKGROUND + "join message!")
        )

        transparentItem.itemMeta = transparentMeta

        joinSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format("#5eab59Selected"))
        transparentMeta.lore = listOf(
            Colors.format(MessageType.BACKGROUND + ""),
            Colors.format(MessageType.BACKGROUND + "You're currently editing your"),
            Colors.format(MessageType.BACKGROUND + "leave message!")
        )

        transparentItem.itemMeta = transparentMeta

        leaveSlots.forEach { inventory.setItem(it, transparentItem) }

        transparentMeta.setDisplayName(Colors.format(MessageType.ERROR + "&lReset"))
        transparentMeta.lore = listOf(Colors.format("${MessageType.LORE}Click here to reset your leave message."))

        transparentItem.itemMeta = transparentMeta

        resetSlots.forEach { inventory.setItem(it, transparentItem) }

        addColors(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
        addMessages(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE, inventory)
    }
}