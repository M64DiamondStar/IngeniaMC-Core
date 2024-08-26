package me.m64diamondstar.ingeniamccore.npc

import com.ticxo.modelengine.api.ModelEngineAPI
import com.ticxo.modelengine.api.entity.Dummy
import com.ticxo.modelengine.api.model.ActiveModel
import com.ticxo.modelengine.api.model.bone.BoneBehaviorTypes
import io.papermc.paper.entity.LookAnchor
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.general.bossbar.BossBarIndex
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.npc.utils.DialoguePlayerRegistry
import me.m64diamondstar.ingeniamccore.npc.utils.DialogueUtils
import me.m64diamondstar.ingeniamccore.npc.utils.NpcRegistry
import me.m64diamondstar.ingeniamccore.npc.utils.NpcUtils
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.entities.CameraPacketEntity
import me.m64diamondstar.ingeniamccore.utils.entities.NpcPlayerEntity
import me.m64diamondstar.ingeniamccore.utils.messages.Font
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.joml.Vector3f
import java.time.Duration

class Npc(private val id: String) {

    private var baseEntity: Dummy<*>? = null
    private var activeModel: ActiveModel? = null
    private val dialoguePlayers = HashMap<Player, Dialogue>()
    private var lookTask: BukkitTask? = null

    /**
     * Does everything needed to set up the NPC.
     * Creates a file (if it doesn't exist already), registers and spawns the NPC.
     */
    fun init(): Npc {
        getData()
        spawn()
        if(baseEntity == null) return this
        NpcRegistry.addNpc(id, this)
        startLooking()
        return this
    }

    private fun startLooking(){
        lookTask = object: BukkitRunnable(){

            override fun run() {
                if(baseEntity == null) return
                val data = getData()
                var closestPlayer: Player? = null
                for(player in NpcUtils.PlayersMoved.getPlayers()){
                    if(player.location.distanceSquared(data.getLocation()!!) > 64) continue
                    if(!getDialogue(player).isInNormalView()) continue
                    if(closestPlayer == null) closestPlayer = player
                    if(player.location.distanceSquared(data.getLocation()!!) < closestPlayer.location.distanceSquared(data.getLocation()!!))
                        closestPlayer = player

                    val fromVector = Vector3f(player.eyeLocation.toVector().toVector3f())
                    val directionVector = Vector3f(player.eyeLocation.direction.toVector3f()).normalize().mul(0.1f)

                    if(!DialoguePlayerRegistry.contains(player)) {
                        activeModel!!.getBone("b_origin_hitbox").ifPresent { bone ->
                            bone.getBoneBehavior(BoneBehaviorTypes.SUB_HITBOX).ifPresent {
                                val halfX = it.dimension.x / 2
                                val halfZ = it.dimension.z / 2
                                val bottomCorner = (it.location.clone() as Vector3f).sub(halfX, 0f, halfZ)
                                val topCorner = (it.location.clone() as Vector3f).add(halfX, it.dimension.y, halfZ)

                                var result = false

                                for (i in 0..40) {
                                    fromVector.add(directionVector)
                                    if (LocationUtils.isPointInsideCuboid(fromVector, LocationUtils.Cuboid(bottomCorner, topCorner))) {
                                        IngeniaPlayer(player).setBossBar(BossBarIndex.THIRD, Font.getWidget("Click to talk with ${data.getName()}"), true)
                                        result = true
                                        break
                                    }
                                }

                                if (!result) {
                                    IngeniaPlayer(player).setBossBar(BossBarIndex.THIRD, null, true)
                                }
                            }
                        }
                    }else{
                        IngeniaPlayer(player).setBossBar(BossBarIndex.THIRD, null, true)
                    }
                }

                if(closestPlayer != null){
                    Bukkit.getScheduler().runTaskAsynchronously(IngeniaMC.plugin, Runnable {
                        baseEntity!!.xHeadRot = (LocationUtils.getPitchLookAt(baseEntity!!.location.clone().add(0.0, 1.68, 0.0), closestPlayer.eyeLocation))
                        baseEntity!!.yHeadRot = (LocationUtils.getYawLookAt(baseEntity!!.location.clone().add(0.0, 1.68, 0.0), closestPlayer.eyeLocation))
                    })
                }

                NpcUtils.PlayersMoved.clear()
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 3L)
    }

    fun getData(): NpcData{
        return NpcData(id)
    }

    fun spawn(){
        val data = getData()
        if (data.getLocation() == null || data.getModel() == null)
            return

        if (baseEntity != null)
            baseEntity!!.isRemoved = true

        val dummy = Dummy<Any>()
        dummy.syncLocation(data.getLocation()!!)

        val modeledEntity = ModelEngineAPI.createModeledEntity(dummy)
        val activeModel = ModelEngineAPI.createActiveModel(data.getModel())

        modeledEntity.addModel(activeModel, true)

        baseEntity = dummy
        this.activeModel = activeModel
        NpcRegistry.setNpcDummy(baseEntity!!, id)
        dummy.lookController.setBodyYaw(data.getLocation()!!.yaw)
        if(lookTask == null)
            startLooking()
    }

    fun despawn(){
        if(baseEntity != null) {
            baseEntity?.isRemoved = true
            baseEntity?.data?.destroy()
            lookTask?.cancel()
            lookTask = null
        }

    }

    fun respawn(){
        despawn()
        spawn()
    }

    fun getBaseEntity(): Dummy<*>? {
        return baseEntity
    }

    fun delete() {
        NpcRegistry.removeNpc(id)
        despawn()
        getData().deleteFile()
    }

    fun getDialogue(player: Player): Dialogue {
        return dialoguePlayers[player] ?: Dialogue(player)
    }

    /**
     * ! Do not make an instance of this class !
     * Use the getDialogue(player) method instead.
     */
    inner class Dialogue(val player: Player){

        private var previousNext = 0L
        private var normalView: Boolean = true
        private var branch = "main"
        private var index = 0
        private var currentCompleted = false
        private var task: BukkitTask? = null
        private var ticks = 0
        private var progressiveTask: BukkitTask? = null
        private var actionTask: BukkitTask? = null
        private var camera: CameraPacketEntity? = null
        private var playerInventory = player.inventory.contents
        private var playerLocation = player.location
        private var checkLocation = true
        private var fakePlayer: NpcPlayerEntity? = null

        init{
            dialoguePlayers[player] = this
        }

        fun start(){
            val data = getData()
            if(data.getDialogue(branch, index).isEmpty()){ // Dialogue does not exist or has been wrongly configured
                dialoguePlayers.remove(player)
                return
            }

            CosmeticPlayer(player).getEquipment().hideCosmetic(CosmeticType.BODY_WEAR)

            player.inventory.heldItemSlot = 8
            playerInventory = player.inventory.contents
            player.inventory.contents = emptyArray()
            DialoguePlayerRegistry.addDialoguePlayer(player, this@Npc)

            player.lookAt(baseEntity!!.location.clone().add(0.0, 1.65, 0.0), LookAnchor.EYES)
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, Int.MAX_VALUE, 2, false, false, false))

            baseEntity!!.xHeadRot = (LocationUtils.getPitchLookAt(baseEntity!!.location.clone().add(0.0, 1.68, 0.0), player.eyeLocation))
            baseEntity!!.yHeadRot = (LocationUtils.getYawLookAt(baseEntity!!.location.clone().add(0.0, 1.68, 0.0), player.eyeLocation))

            progressiveTask = DialogueUtils.sendProgressiveDialogue(
                player,
                data.getDialogue(branch, index),
                data.getDialogueBackdrop(),
                data.getDialogueBackdropColor(),
                data.getSkin()
            )

            task = object: BukkitRunnable(){
                override fun run() {

                    if(player.location.distanceSquared(baseEntity!!.location) > 36 && checkLocation){
                        end()
                        return
                    }
                    if(currentCompleted){
                        (player as Audience).sendActionBar(
                            DialogueUtils.getDialogueFormat(
                                data.getDialogue(branch, index),
                                data.getDialogueBackdrop(),
                                data.getDialogueBackdropColor(),
                                data.getSkin()
                            )
                        )
                    }else{
                        if(ticks > data.getDialogue(branch, index).sumOf { it.length }){
                            currentCompleted = true
                            ticks = 0
                        }
                        ticks += 5
                    }
                }
            }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0, 5)
        }

        fun next(withIndex: Int?, withBranch: String?) {
            if(System.currentTimeMillis() - previousNext < 1000) return
            val data = getData()
            if(!currentCompleted){
                progressiveTask?.cancel()
                currentCompleted = true
                (player as Audience).sendActionBar(DialogueUtils.getDialogueFormat(
                    data.getDialogue(branch, index), data.getDialogueBackdrop(), data.getDialogueBackdropColor(), data.getSkin()
                )
                )
                return
            }
            if(data.hasOptions(branch, index) && withIndex == null && withBranch == null) return

            actionTask?.cancel()
            if(camera != null && camera!!.isAlive && !normalView) {
                Bukkit.getScheduler().runTaskLater(IngeniaMC.plugin, Runnable {
                    camera!!.despawn()
                    IngeniaPlayer(player).setBossBar(BossBarIndex.FIRST, null, true)
                    IngeniaPlayer(player).updateMainBossBar()
                    IngeniaPlayer(player).setBossBar(BossBarIndex.SECOND, null, true)
                    IngeniaPlayer(player).setBossBar(BossBarIndex.THIRD, null, true)
                }, 10L)
                val times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(500), Duration.ofMillis(500))
                (player as Audience).showTitle(
                    Title.title(
                        Component.text(Font.Characters.COLOR_SCREEN).color(TextColor.color(0, 0, 0)),
                        Component.empty(),
                        times
                    )
                )
            }

            currentCompleted = false
            if(withIndex != null) index = withIndex
            else index += 1
            if(withBranch != null) branch = withBranch
            ticks = 0
            previousNext = System.currentTimeMillis()

            // Dialogue has ended
            if(data.getDialogue(branch, index).isEmpty()){
                end()
                return
            }

            // If the player has an option, give it numbers. If not, remove the numbers
            if(data.hasOptions(branch, index))
                this.setNumbers(true)
            else
                this.setNumbers(false)

            // Dialogue has an action
            if(data.getActionType(branch, index) != null && data.getActionData(branch, index) != null){
                // If the player is in normal view, it'll save their current location to teleport them back after
                if(normalView)
                    playerLocation = player.location

                checkLocation = false

                // Execute the action
                actionTask = data.getActionType(branch, index)!!.execute(this@Npc, player, data.getActionData(branch, index)!!)

                Bukkit.getScheduler().runTaskLater(IngeniaMC.plugin, Runnable { // Start the dialogue after 10 ticks
                    progressiveTask = DialogueUtils.sendProgressiveDialogue(
                        player,
                        data.getDialogue(branch, index),
                        data.getDialogueBackdrop(),
                        data.getDialogueBackdropColor(),
                        data.getSkin()
                    )
                }, 10L)
            }

            // Dialogue has no action
            else{
                Bukkit.getScheduler().runTaskLater(IngeniaMC.plugin, Runnable {
                    camera = null
                    normalView = true
                    checkLocation = true // Start checking the location again after 10 ticks if out of normal view, else start instantly
                    despawnFakePlayer() // Despawn the fake player if there is one
                    progressiveTask = DialogueUtils.sendProgressiveDialogue(
                        player,
                        data.getDialogue(branch, index),
                        data.getDialogueBackdrop(),
                        data.getDialogueBackdropColor(),
                        data.getSkin()
                    )
                }, if(!normalView) 10L else 0L)
            }
        }

        fun end(){
            val data = getData()
            if(data.getActionType(branch, index - 1) != null && data.getActionData(branch, index - 1) != null){
                val times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(500), Duration.ofMillis(500))
                (player as Audience).showTitle(
                    Title.title(
                        Component.text(Font.Characters.COLOR_SCREEN).color(TextColor.color(0, 0, 0)),
                        Component.empty(),
                        times
                    )
                )
                Bukkit.getScheduler().runTaskLaterAsynchronously(IngeniaMC.plugin, Runnable {
                    (player as Audience).sendActionBar(Component.empty())
                }, 10L)
            }else{
                (player as Audience).sendActionBar(Component.empty())
            }
            task?.cancel()
            progressiveTask?.cancel()
            dialoguePlayers.remove(player)
            DialoguePlayerRegistry.removeDialoguePlayer(player)
            DialoguePlayerRegistry.putOnCooldown(player)
            player.inventory.contents = playerInventory
            CosmeticPlayer(player).getEquipment().showCosmetic(CosmeticType.BODY_WEAR)

            player.walkSpeed = 0.2f
            Bukkit.getScheduler().runTask(IngeniaMC.plugin, Runnable {
                player.removePotionEffect(PotionEffectType.SLOWNESS)
            })

        }

        fun setBranch(branch: String){
            next(0, branch)
        }

        fun setCamera(camera: CameraPacketEntity){
            this.camera = camera
        }
        
        fun setNormalView(boolean: Boolean){
            this.normalView = boolean
        }

        fun isInNormalView() = normalView

        fun executeOption(optionIndex: Int){
            val data = getData()
            if(data.getDialogue(branch, index).isEmpty()) return
            if(!data.hasOptions(branch, index)) return
            if(data.getOption(branch, index, optionIndex) == null) return

            val option = data.getOption(branch, index, optionIndex)!!
            option.execute(player, this@Npc)

        }

        fun getPlayerLocation() = playerLocation.clone()

        private fun setNumbers(boolean: Boolean){
            if(boolean){
                val item = ItemStack(Material.FEATHER)
                val meta = item.itemMeta!!
                meta.displayName(Component.empty())

                for(i in 0.. 3) {
                    meta.setCustomModelData(i + 4)
                    item.itemMeta = meta
                    player.inventory.setItem(i, item)
                }
            }else{
                for(i in 0.. 3) player.inventory.setItem(i, null)
            }
        }

        fun spawnFakePlayer(){
            despawnFakePlayer()
            val npcEntity = NpcPlayerEntity(player.location.world, player.location, player)
            npcEntity.spawn(IngeniaPlayer(player).playerConfig.getShowSkinDuringDialogue())
            this.fakePlayer = npcEntity
        }

        fun despawnFakePlayer(){
            if(this.fakePlayer != null)
                this.fakePlayer?.despawn()
        }

    }

}