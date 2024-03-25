package me.m64diamondstar.ingeniamccore.npc.utils

import me.m64diamondstar.ingeniamccore.npc.Npc
import me.m64diamondstar.ingeniamccore.shops.inventories.ShopInventory
import org.bukkit.entity.Player

enum class DialogueOptionType {

    OPEN_SHOP {
        override fun execute(npc: Npc, player: Player, data: String?) {
            if(data == null) return
            val args = data.split(", ")
            val shopInventory = ShopInventory(player, args[0], args[1])
            shopInventory.open()
        }
    },
    GO_TO_BRANCH {
        override fun execute(npc: Npc, player: Player, data: String?) {
            if(data == null) return
            npc.getDialogue(player).setBranch(data)
        }
    },
    CANCEL_DIALOGUE{
        override fun execute(npc: Npc, player: Player, data: String?) {
            npc.getDialogue(player).end()
        }
    };

    abstract fun execute(npc: Npc, player: Player, data: String?)

}