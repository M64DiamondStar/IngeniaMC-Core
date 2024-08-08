package me.m64diamondstar.ingeniamccore.npc.listeners

import com.ticxo.modelengine.api.entity.Dummy
import com.ticxo.modelengine.api.events.BaseEntityInteractEvent
import com.ticxo.modelengine.api.events.ModelRegistrationEvent
import com.ticxo.modelengine.api.generator.ModelGenerator
import gg.flyte.twilight.scheduler.sync
import io.papermc.paper.event.player.AsyncChatEvent
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.npc.utils.DialoguePlayerRegistry
import me.m64diamondstar.ingeniamccore.npc.utils.NpcRegistry
import me.m64diamondstar.ingeniamccore.npc.utils.NpcUtils
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerAnimationEvent
import org.bukkit.event.player.PlayerAnimationType
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerMoveEvent

class NpcListener: Listener {

    @EventHandler
    fun onModelRegistration(event: ModelRegistrationEvent){
        if(event.phase == ModelGenerator.Phase.FINISHED) {
            NpcUtils.loadNpcFiles()
            IngeniaMC.plugin.logger.info("NPCs (re)loaded ✓")
        }
    }

    @EventHandler
    fun onNPCInteract(event: BaseEntityInteractEvent) {
        val entity = event.baseEntity
        val player = event.player
        IngeniaPlayer(player)

        if(entity !is Dummy<*>) return
        if (!NpcRegistry.isNpc(entity)) return
        if (DialoguePlayerRegistry.contains(player)) return
        if(DialoguePlayerRegistry.isOnCooldown(player)){
            (player as Audience).sendActionBar(MiniMessage.miniMessage().deserialize("<${MessageType.ERROR}>Please slow down a little bit!"))
            return
        }

        val dialogue = NpcRegistry.getNpc(NpcRegistry.getNpcID(entity)!!)!!.getDialogue(player)
        dialogue.start()
    }

    @EventHandler
    fun onPlayerSwing(event: PlayerAnimationEvent){
        val player = event.player
        if(event.animationType != PlayerAnimationType.ARM_SWING) return
        if(!DialoguePlayerRegistry.contains(player)) return
        DialoguePlayerRegistry.getDialoguePlayer(player)!!.getDialogue(player).next(null, null)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent){
        val player = event.player
        if(!DialoguePlayerRegistry.contains(player)) return
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent){
        val player = event.player
        NpcUtils.PlayersMoved.addPlayer(player)
    }

    @EventHandler
    fun onSwitchSlot(event: PlayerItemHeldEvent){
        val player = event.player
        if(!DialoguePlayerRegistry.contains(player)) return
        DialoguePlayerRegistry.getDialoguePlayer(player)!!.getDialogue(player).executeOption(event.newSlot + 1)
        event.isCancelled = true
    }

    @EventHandler
    fun onChatEvent(event: AsyncChatEvent) {
        val player = event.player
        if (!event.signedMessage().message().first().isDigit()) return
        if (!DialoguePlayerRegistry.contains(player)) return
        sync {
            DialoguePlayerRegistry.getDialoguePlayer(player)!!.getDialogue(player)
                .executeOption(event.signedMessage().message().first().digitToInt())
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onInventoryOpenEvent(event: InventoryOpenEvent){
        val player = event.player as Player
        if(!DialoguePlayerRegistry.contains(player)) return
        event.isCancelled = true
    }
}