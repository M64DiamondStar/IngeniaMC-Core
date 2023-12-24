package me.m64diamondstar.ingeniamccore.general.player

import me.m64diamondstar.ingeniamccore.cosmetics.data.CosmeticItems
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType
import me.m64diamondstar.ingeniamccore.general.player.data.CosmeticPlayerConfig
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class CosmeticPlayer(private val player: Player) {

    private val cosmeticPlayerConfig = CosmeticPlayerConfig(player.uniqueId)

    fun getPlayer(): Player {
        return player
    }

    /**
     * Gets the equipment status of the player
     * You can equip and un-equip pieces with this class
     */
    fun getEquipment(): Equipment{
        return Equipment(cosmeticPlayer = this)
    }

    /**
     * Gets the equipment status of the player
     * You can equip and un-equip pieces with this class
     */
    fun getMessageEquipment(): MessageEquipment{
        return MessageEquipment(cosmeticPlayer = this)
    }

    /**
     * Gets all cosmetics a player has obtained from a specific cosmetic type
     * @param cosmeticType the type of cosmetic to check
     */
    fun getAllCosmetics(cosmeticType: CosmeticType): SortedSet<String> {
        return cosmeticPlayerConfig.getAllCosmetics(cosmeticType)
    }

    /**
     * Gets all cosmetics a player has obtained from a specific cosmetic type
     * @param cosmeticType the type of cosmetic to check
     */
    fun getAllCosmeticsAsItemStack(cosmeticType: CosmeticType): List<ItemStack> {
        val list = ArrayList<ItemStack>()
        getAllCosmetics(cosmeticType).forEach {
            if(CosmeticItems(cosmeticType).getItem(it) == null) return@forEach
            list.add(CosmeticItems(cosmeticType).getItem(it)!!)
        }
        return list
    }

    /**
     * Adds a cosmetic to the player's file
     * @param cosmeticType the type of the cosmetic to add
     * @param id the ID of the cosmetic
     */
    fun addCosmetic(cosmeticType: CosmeticType, id: String){
        cosmeticPlayerConfig.addCosmetic(cosmeticType, id)
    }

    /**
     * Removes a cosmetic to the player's file
     * @param cosmeticType the type of the cosmetic to remove
     * @param id the ID of the cosmetic
     */
    fun removeCosmetic(cosmeticType: CosmeticType, id: String){
        cosmeticPlayerConfig.removeCosmetic(cosmeticType, id)
    }

    /**
     * Checks if the player has a specific cosmetic
     * @param cosmeticType the type of the cosmetic to remove
     * @param id the ID of the cosmetic
     */
    fun hasCosmetic(cosmeticType: CosmeticType, id: String): Boolean{
        return cosmeticPlayerConfig.hasCosmetic(cosmeticType, id)
    }

    /**
     * Gets all message colors
     * @return all message colors
     */
    fun getAllMessageColors(): SortedSet<String> {
        val sortedSet = TreeSet<String>()
        sortedSet.add("default")
        sortedSet.addAll(cosmeticPlayerConfig.getAllMessageColors())
        return sortedSet
    }

    /**
     * Adds a message color
     * @param id the ID of the message
     */
    fun addMessageColor(id: String){
        cosmeticPlayerConfig.addMessageColor(id)
    }

    /**
     * Removes a message color
     * @param id the ID of the message
     */
    fun removeMessageColor(id: String){
        cosmeticPlayerConfig.removeMessageColor(id)
    }

    /**
     * Checks if the player has a specific message color
     * @param id the ID of the message
     * @return true if the player has the message color
     */
    fun hasMessageColor(id: String): Boolean{
        if(id.equals("default", ignoreCase = true)) return true
        return cosmeticPlayerConfig.hasMessageColor(id)
    }

    /**
     * Gets all join messages
     * @return all join messages
     */
    fun getAllJoinMessages(): SortedSet<String>{
        val sortedSet = TreeSet<String>()
        sortedSet.add("default")
        sortedSet.addAll(cosmeticPlayerConfig.getAllJoinMessages())
        return sortedSet
    }

    /**
     * Adds a join message
     * @param id the ID of the message
     */
    fun addJoinMessage(id: String){
        cosmeticPlayerConfig.addJoinMessage(id)
    }

    /**
     * Removes a join message
     * @param id the ID of the message
     */
    fun removeJoinMessage(id: String){
        cosmeticPlayerConfig.removeJoinMessage(id)
    }

    /**
     * Checks if the player has a specific join message
     * @param id the ID of the message
     * @return true if the player has the join message
     */
    fun hasJoinMessage(id: String): Boolean{
        if(id.equals("default", ignoreCase = true)) return true
        return cosmeticPlayerConfig.hasJoinMessage(id)
    }

    /**
     * Gets all leave messages
     * @return all leave messages
     */
    fun getAllLeaveMessages(): SortedSet<String>{
        val sortedSet = TreeSet<String>()
        sortedSet.add("default")
        sortedSet.addAll(cosmeticPlayerConfig.getAllLeaveMessages())
        return sortedSet
    }

    /**
     * Adds a leave message
     * @param id the ID of the message
     */
    fun addLeaveMessage(id: String){
        cosmeticPlayerConfig.addLeaveMessage(id)
    }

    /**
     * Removes a leave message
     * @param id the ID of the message
     */
    fun removeLeaveMessage(id: String){
        cosmeticPlayerConfig.removeLeaveMessage(id)
    }

    /**
     * Checks if the player has a specific leave message
     * @param id the ID of the message
     * @return true if the player has the leave message
     */
    fun hasLeaveMessage(id: String): Boolean{
        if(id.equals("default", ignoreCase = true)) return true
        return cosmeticPlayerConfig.hasLeaveMessage(id)
    }

    class Equipment(private val cosmeticPlayer: CosmeticPlayer) {

        /**
         * Equips a cosmetic, ignoring the check if the player has the cosmetic
         * @param cosmeticType the type of the cosmetic
         * @param id the id of the cosmetic
         */
        private fun equipUnsafeCosmetic(cosmeticType: CosmeticType, id: String) {
            when (cosmeticType) {
                CosmeticType.HAT -> {
                    cosmeticPlayer.player.equipment!!.helmet = CosmeticItems(cosmeticType).getItem(id)
                }

                CosmeticType.SHIRT -> {
                    cosmeticPlayer.player.equipment!!.chestplate = CosmeticItems(cosmeticType).getItem(id)
                }

                CosmeticType.PANTS -> {
                    cosmeticPlayer.player.equipment!!.leggings = CosmeticItems(cosmeticType).getItem(id)
                }

                CosmeticType.SHOES -> {
                    cosmeticPlayer.player.equipment!!.boots = CosmeticItems(cosmeticType).getItem(id)
                }

                CosmeticType.BALLOON -> {

                }
            }
        }

        /**
         * Un-equips a cosmetic
         * @param cosmeticType the type of the cosmetic
         */
        fun unEquipCosmetic(cosmeticType: CosmeticType) {
            when (cosmeticType) {
                CosmeticType.HAT -> {
                    cosmeticPlayer.player.equipment!!.helmet = null
                }

                CosmeticType.SHIRT -> {
                    cosmeticPlayer.player.equipment!!.chestplate = null
                }

                CosmeticType.PANTS -> {
                    cosmeticPlayer.player.equipment!!.leggings = null
                }

                CosmeticType.SHOES -> {
                    cosmeticPlayer.player.equipment!!.boots = null
                }

                CosmeticType.BALLOON -> {
                    cosmeticPlayer.player.equipment!!.helmet = null
                }
            }
        }

        /**
         * Equips a cosmetic but only if the player has it
         * @param cosmeticType the type of the cosmetic
         * @param id the id of the cosmetic
         */
        fun equipCosmetic(cosmeticType: CosmeticType, id: String): Boolean {
            if (!cosmeticPlayer.hasCosmetic(cosmeticType, id)) return false
            equipUnsafeCosmetic(cosmeticType, id)
            return true
        }

        /**
         * Gets the ID of the equipped cosmetic
         */
        fun getEquippedId(cosmeticType: CosmeticType): String? {
            return when (cosmeticType) {
                CosmeticType.HAT -> {
                    val helmet = cosmeticPlayer.player.equipment!!.helmet ?: return null
                    CosmeticItems(cosmeticType).getID(helmet)
                }
                CosmeticType.SHIRT -> {
                    val chestplate = cosmeticPlayer.player.equipment!!.chestplate ?: return null
                    CosmeticItems(cosmeticType).getID(chestplate)
                }
                CosmeticType.PANTS -> {
                    val leggings = cosmeticPlayer.player.equipment!!.leggings ?: return null
                    CosmeticItems(cosmeticType).getID(leggings)
                }
                CosmeticType.SHOES -> {
                    val boots = cosmeticPlayer.player.equipment!!.boots ?: return null
                    CosmeticItems(cosmeticType).getID(boots)
                }
                CosmeticType.BALLOON -> {
                    null
                }
            }
        }

    }

    /**
     * Manages the message equipment of a player. This includes joining and leaving messages and their colors.
     * @param cosmeticPlayer the player
     */
    class MessageEquipment(private val cosmeticPlayer: CosmeticPlayer) {

        private fun equipUnsafeMessage(messageType: MessageType, id: String){

            val ingeniaPlayer = IngeniaPlayer(cosmeticPlayer.player)

            when (messageType) {
                MessageType.JOIN -> {
                    ingeniaPlayer.joinMessage = id
                }

                MessageType.LEAVE -> {
                    ingeniaPlayer.leaveMessage = id
                }
            }
        }

        private fun equipUnsafeColor(messageType: MessageType, id: String){

            val ingeniaPlayer = IngeniaPlayer(cosmeticPlayer.player)

            when (messageType) {
                MessageType.JOIN -> {
                    ingeniaPlayer.joinColor = id
                }

                MessageType.LEAVE -> {
                    ingeniaPlayer.leaveColor = id
                }
            }
        }

        /**
         * Equips a message but only if the player has it
         * @param messageType the type of the cosmetic
         * @param id the id of the cosmetic
         */
        fun equipMessage(messageType: MessageType, id: String): Boolean {
            when (messageType) {
                MessageType.JOIN -> {
                    if (!cosmeticPlayer.hasJoinMessage(id)) return false
                    equipUnsafeMessage(messageType, id)
                    return true
                }

                MessageType.LEAVE -> {
                    if (!cosmeticPlayer.hasLeaveMessage(id)) return false
                    equipUnsafeMessage(messageType, id)
                    return true
                }
            }
        }

        /**
         * Equips a color but only if the player has it
         * @param messageType the type of the cosmetic
         * @param id the id of the cosmetic
         */
        fun equipColor(messageType: MessageType, id: String): Boolean {
            if (!cosmeticPlayer.hasMessageColor(id)) return false
            equipUnsafeColor(messageType, id)
            return true
        }

        /**
         * Un-equips a message
         * @param messageType the type of the cosmetic
         */
        fun unEquipMessage(messageType: MessageType){

            val ingeniaPlayer = IngeniaPlayer(cosmeticPlayer.player)

            when(messageType){
                MessageType.JOIN -> {
                    ingeniaPlayer.joinMessage = null
                }

                MessageType.LEAVE -> {
                    ingeniaPlayer.leaveMessage = null
                }
            }
        }

        /**
         * Un-equips a color
         * @param messageType the type of the cosmetic
         */
        fun unEquipColor(messageType: MessageType){

            val ingeniaPlayer = IngeniaPlayer(cosmeticPlayer.player)

            when(messageType){
                MessageType.JOIN -> {
                    ingeniaPlayer.joinColor = null
                }

                MessageType.LEAVE -> {
                    ingeniaPlayer.leaveColor = null
                }
            }
        }

        /**
         * Gets the ID of the equipped message
         * @param messageType the type of the cosmetic
         */
        fun getEquippedId(messageType: MessageType): String? {
            val ingeniaPlayer = IngeniaPlayer(cosmeticPlayer.player)
            return when (messageType) {
                MessageType.JOIN -> {
                    ingeniaPlayer.joinMessage
                }
                MessageType.LEAVE -> {
                    ingeniaPlayer.leaveMessage
                }
            }
        }

        /**
         * Gets the equipped color for a message
         * @param messageType the type of the cosmetic
         */
        fun getEquippedColor(messageType: MessageType): String? {
            val ingeniaPlayer = IngeniaPlayer(cosmeticPlayer.player)
            return when (messageType) {
                MessageType.JOIN -> {
                    ingeniaPlayer.joinColor
                }
                MessageType.LEAVE -> {
                    ingeniaPlayer.leaveColor
                }
            }
        }

    }

}