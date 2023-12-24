package me.m64diamondstar.ingeniamccore.shops.utils

import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.utils.EnumUtilities
import me.m64diamondstar.ingeniamccore.utils.Rank
import org.bukkit.entity.Player

enum class ItemRequirement {

    RIDECOUNT{
        /**
        * @param player the player
         * @param value a string in the format of "category,name,amount"
        */
        override fun isCompleted(player: Player, value: String): Boolean {
            val args = value.split(",")
            if(args[2].toLongOrNull() == null) return false
            val attraction = Attraction(args[0], args[1])
            return attraction.getRidecount(player) >= args[2].toLong()
        }

        /**
         * @param value a string in the format of "category,name,amount"
         */
        override fun getDisplay(value: String): String {
            val args = value.split(",")
            if(args[2].toLongOrNull() == null) return "Internal error"
            val attraction = Attraction(args[0], args[1])
            return "${args[2]} ridecount for ${attraction.getName()}"
        }

        /**
         * Gets the format of the value.
         */
        override fun getValueFormat(): String {
            return "category,name,amount"
        }
    },
    LEVEL{
        /**
         * @param player the player
         * @param value a string in the format of "level"
         */
        override fun isCompleted(player: Player, value: String): Boolean {
            if(value.toIntOrNull() == null) return false
            val ingeniaPlayer = IngeniaPlayer(player)
            return ingeniaPlayer.getLevel() >= value.toInt()
        }

        /**
         * @param value a string in the format of "level"
         */
        override fun getDisplay(value: String): String {
            if(value.toIntOrNull() == null) return "Internal error"
            return "Level $value"
        }

        /**
         * Gets the format of the value.
         */
        override fun getValueFormat(): String {
            return "minimumLevel"
        }
    },
    RANK{
        /**
         * @param player the player
         * @param value a string in the format of "rank"
         */
        override fun isCompleted(player: Player, value: String): Boolean {
            if(!EnumUtilities.enumContains<Rank>(value.uppercase())) return false
            return player.hasPermission("ingenia.${value.lowercase()}")
        }

        /**
         * @param value a string in the format of "rank"
         */
        override fun getDisplay(value: String): String {
            if(!EnumUtilities.enumContains<Rank>(value.uppercase())) return "Internal error"
            return "${Rank.valueOf(value).getDisplayName()} Rank"
        }

        /**
         * Gets the format of the value.
         */
        override fun getValueFormat(): String {
            return "rank"
        }
    },
    SHOP_ITEM{
        /**
         * @param player the player
         * @param value a string in the format of "ShopItem,id"
         */
        override fun isCompleted(player: Player, value: String): Boolean {
            val args = value.split(",")
            if(args.size != 2) return false
            if(!EnumUtilities.enumContains<ShopItem>(args[0])) return false
            val shopItem = ShopItem.valueOf(args[0])
            val id = args[1]
            return shopItem.alreadyBought(player, id)
        }

        /**
         * @param value a string in the format of "ShopItem,id"
         */
        override fun getDisplay(value: String): String {
            val args = value.split(",")
            if(args.size != 2) return "Internal error"
            if(!EnumUtilities.enumContains<ShopItem>(args[0])) return "Internal error"
            val shopItem = ShopItem.valueOf(args[0])
            val id = args[1]
            return "${shopItem.getDisplayName()}: ${shopItem.getItemDisplayName(id)}"
        }

        /**
         * Gets the format of the value.
         */
        override fun getValueFormat(): String {
            return "ShopItem,id"
        }
    };

    abstract fun isCompleted(player: Player, value: String): Boolean
    abstract fun getDisplay(value: String): String
    abstract fun getValueFormat(): String
}