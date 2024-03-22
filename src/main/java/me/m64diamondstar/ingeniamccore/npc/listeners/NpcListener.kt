package me.m64diamondstar.ingeniamccore.npc.listeners

import com.ticxo.modelengine.api.entity.Dummy
import com.ticxo.modelengine.api.events.BaseEntityInteractEvent
import com.ticxo.modelengine.api.events.ModelRegistrationEvent
import com.ticxo.modelengine.api.generator.ModelGenerator
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.npc.utils.DialoguePlayerRegistry
import me.m64diamondstar.ingeniamccore.npc.utils.NpcRegistry
import me.m64diamondstar.ingeniamccore.npc.utils.NpcUtils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerAnimationEvent
import org.bukkit.event.player.PlayerAnimationType
import org.bukkit.event.player.PlayerMoveEvent

class NpcListener: Listener {

    @EventHandler
    fun onModelRegistration(event: ModelRegistrationEvent){
        if(event.phase == ModelGenerator.Phase.FINISHED) {
            NpcUtils.loadNpcFiles()
            Bukkit.getLogger().info("NPCs (re)loaded ✓")
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

        val dialogue = NpcRegistry.getNpc(NpcRegistry.getNpcID(entity)!!)!!.getDialogue(player)
        dialogue.start()
    }

    @EventHandler
    fun onPlayerSwing(event: PlayerAnimationEvent){
        val player = event.player
        if(event.animationType != PlayerAnimationType.ARM_SWING) return
        if(!DialoguePlayerRegistry.contains(player)) return
        DialoguePlayerRegistry.getDialoguePlayer(player)!!.getDialogue(player).next()
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent){
        val player = event.player
        if(!DialoguePlayerRegistry.contains(player)) return
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent){
        if(!event.hasChangedPosition()) return

        val player = event.player
        NpcUtils.PlayersMoved.addPlayer(player)
    }
}