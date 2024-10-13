package me.m64diamondstar.ingeniamccore.entity.body

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.Equipment
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment
import com.google.common.collect.*
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.cosmetics.utils.CosmeticType
import me.m64diamondstar.ingeniamccore.entity.util.EntityUtils
import me.m64diamondstar.ingeniamccore.general.player.CosmeticPlayer
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.tofaa.entitylib.meta.other.ArmorStandMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class BodyWearEntity(private val owner: Player): PassengerEntity(owner) {

    /**
     * Class which manages all the body wear actions, when nametags should be
     * applied, disabled, shown, hidden, ...
     */
    object BodyWearManager {
        private val bodyWears = ConcurrentHashMap<UUID, BodyWearEntity>()
        private val entityIdToDisplay = ConcurrentHashMap<Int, BodyWearEntity>()
        private val blocked: MutableSet<UUID> = Sets.newConcurrentHashSet()
        private val mounted: MutableSet<UUID> = Sets.newConcurrentHashSet()
        private val tasks: CopyOnWriteArrayList<BukkitTask> = Lists.newCopyOnWriteArrayList()
        private val viewers: ConcurrentHashMap<UUID, HashSet<UUID>> = ConcurrentHashMap()

        init {
            Bukkit.getOnlinePlayers().forEach { addPlayer(it) }
            startTask()

            IngeniaMC.addOnDisableRunnable(Runnable { removeAll() })
        }

        private fun startTask() {
            tasks.forEach { it.cancel() }

            // Refresh passengers
            val passengers = object : BukkitRunnable() {
                override fun run() {
                    Bukkit.getOnlinePlayers().forEach { player ->
                        getPacketEntity(player)?.sendPassengerPacketToViewers()
                    }
                }
            }.runTaskTimerAsynchronously(IngeniaMC.plugin, 10L, 20L)

            tasks.add(passengers)
        }

        fun addPlayer(player: Player) {
            if(bodyWears.contains(player.uniqueId)) return
            if(blocked.contains(player.uniqueId)) return
            if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) return

            viewers[player.uniqueId] = hashSetOf(player.uniqueId)

            val entity = BodyWearEntity(player)
            entity.spawn(player.location.apply { this.yaw = 0f; this.pitch = 0f })

            handleVanish(player, entity)

            bodyWears[player.uniqueId] = entity
            entityIdToDisplay[entity.entity.entityId] = entity
        }

        fun removePlayer(player: Player) {
            removePlayer(player.uniqueId)
        }

        fun removePlayer(uniqueId: UUID){
            if(!bodyWears.contains(uniqueId)) return

            val entity = bodyWears[uniqueId]!!
            bodyWears.remove(uniqueId)
            entityIdToDisplay.remove(entity.entity.entityId)
            entity.remove()
        }

        private fun handleVanish(player: Player, display: BodyWearEntity) {
            // If the player is vanished, hide display for all players except for those who can see the player
            Bukkit.getOnlinePlayers()
                .filter { it != player }  // filter out the current player
                .filter { it.location.world == player.location.world }  // check if they are in the same world
                .filter { it.location.distance(player.location) <= 250 }  // check distance
                .filter { !display.canPlayerSee(it) }  // check if the player can already see the display
                .forEach {
                    display.setVisible(it, true)
                    viewers[player.uniqueId]!!.add(it.uniqueId)
                }  // show the display to the player
        }

        /**
         * Removes all nametags and clears all lists.
         */
        fun removeAll() {
            bodyWears.values.forEach { it.remove() }
            bodyWears.clear()
            entityIdToDisplay.clear()
        }

        fun getPacketEntity(player: Player): BodyWearEntity? {
            if(!bodyWears.containsKey(player.uniqueId)) return null
            return bodyWears[player.uniqueId]
        }

        fun setItem(player: Player, item: ItemStack) {
            if(!viewers.containsKey(player.uniqueId)) return
            getPacketEntity(player)?.setHeadItem(item)
        }

        fun updateMounted(player: Player, mounted: Boolean) {
            if(!viewers.containsKey(player.uniqueId)) return
            getPacketEntity(player)?.let {

                if(mounted) { // Make armor stand invisible for every viewer
                    viewers[player.uniqueId]!!.forEach { viewer ->
                        if(Bukkit.getPlayer(viewer) == null) return@forEach
                        it.setVisible(Bukkit.getPlayer(viewer)!!, false)
                    }
                    this.mounted.add(player.uniqueId)
                }
                else { // Make armor stand visible for every viewer
                    this.mounted.remove(player.uniqueId)

                    viewers[player.uniqueId]!!.forEach { viewer ->
                        if(Bukkit.getPlayer(viewer) == null) return@forEach
                        updateEntity(Bukkit.getPlayer(viewer)!!, player)
                    }
                }
            }
        }

        fun updateEntity(player: Player, target: Player) {
            viewers[player.uniqueId]?.add(target.uniqueId)

            if(mounted.contains(target.uniqueId)) return

            getPacketEntity(target)?.let {
                it.hideFromPlayerSilently(player)
                it.setVisible(player, true)
            }
        }

        fun removeEntity(player: Player, target: Player) {
            viewers[player.uniqueId]?.remove(target.uniqueId)

            getPacketEntity(target)?.setVisible(player, false)
        }


        fun blockPlayer(player: Player){
            blocked.add(player.uniqueId)
            Bukkit.getOnlinePlayers().forEach {
                NametagEntity.NametagManager.getPacketEntity(it)?.setVisible(player, false)
            }
        }

        fun unblockPlayer(player: Player){
            blocked.remove(player.uniqueId)
            Bukkit.getOnlinePlayers().forEach {
                NametagEntity.NametagManager.getPacketEntity(it)?.setVisible(player, true)
            }
        }
    }

    private val entity: WrapperEntity = WrapperEntity(EntityUtils.nextEntityId(), UUID.randomUUID(), EntityTypes.ARMOR_STAND)
    private val entityMeta: ArmorStandMeta = entity.entityMeta as ArmorStandMeta
    private var entitySpawned: Boolean = false
    private var item = ItemStack(Material.AIR)

    init {
        entityMeta.isInvisible = true
        entityMeta.isMarker = true
        entityMeta.setHasNoGravity(true)
    }

    override fun sendPassengerPacket(player: Player) {
        PassengerPacketManager.sendPassengersPacket(player, this)
    }

    override fun sendPassengerPacketToViewers() {
        entity.viewers.forEach {
            val viewer = Bukkit.getPlayer(it) ?: return@forEach
            PassengerPacketManager.sendPassengersPacket(viewer, this)
        }
    }

    override fun spawn(location: Location) {
        entity.addViewer(owner.uniqueId)
        entitySpawned = true
        entity.spawn(EntityUtils.convertBukkitToEntityLocation(location))

        object: BukkitRunnable() {
            override fun run() {
                if (!entitySpawned) cancel()
                else {
                    entity.rotateHead(owner.location.yaw, 0f)
                    entity.refresh()
                }
            }
        }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)

        object: BukkitRunnable() {
            override fun run() {
                val ingeniaPlayer = IngeniaPlayer(owner)
                if (ingeniaPlayer.bodyWearId == null) return
                CosmeticPlayer(owner).getEquipment().equipCosmetic(CosmeticType.BODY_WEAR, ingeniaPlayer.bodyWearId!!)
            }
        }.runTask(IngeniaMC.plugin)
    }

    override fun remove() {
        entity.remove()
        PassengerPacketManager.removePassenger(owner, entity.entityId)
        entity.refresh()
        entitySpawned = false
    }

    override fun setVisible(player: Player, visible: Boolean) {
        if(visible){
            if(!entitySpawned) return
            if(!player.canSee(owner)) return

            setPosition()
            entity.addViewer(player.uniqueId)

            object: BukkitRunnable(){
                override fun run() {
                    sendPassengerPacket(player)
                    setHeadItem(item) // Send equipment packet to show the item to newly joined players
                }
            }.runTaskLaterAsynchronously(IngeniaMC.plugin, 3L)
        }else{
            entity.removeViewer(player.uniqueId)
            //PacketManager.removePassenger(player, entity.entityId)
        }
    }

    override fun refresh() {
        fixViewers()
        entity.refresh()
    }

    /**
     * Fixes the viewers of the entity. Removes all viewers that are not online or in the server.
     */
    private fun fixViewers() {
        entity.viewers.filter { uuid ->
            val player = Bukkit.getPlayer(uuid)
            if(player == null){
                return@filter true
            }
            val user = PacketEvents.getAPI().playerManager.getUser(player)
            return@filter user == null || user.channel == null
        }.forEach { entity.removeViewerSilently(it) }
    }

    /**
     * Checks if a player can see the display.
     * @param player the player
     * @return true if the player can see the display
     */
    fun canPlayerSee(player: Player): Boolean {
        return entity.viewers.contains(player.uniqueId)
    }

    fun setHeadItem(item: ItemStack) {
        this.item = item
        val equipmentPacket = WrapperPlayServerEntityEquipment(entity.entityId, listOf(Equipment(EquipmentSlot.HELMET, SpigotConversionUtil.fromBukkitItemStack(item))))
        entity.sendPacketsToViewersIfSpawned(equipmentPacket)
    }

    /**
     * Sets the position of the display to the owner's location.
     */
    private fun setPosition(){
        val location = owner.location.clone()
        location.y += 1.8
        entity.setLocation(EntityUtils.convertBukkitToEntityLocation(location))
    }

    /**
     * Silently hides the display from a player.
     */
    fun hideFromPlayerSilently(player: Player){
        entity.removeViewerSilently(player.uniqueId)
    }

    override fun getId(): Int {
        return entity.entityId
    }

    override fun getOwner(): Player {
        return owner
    }

}
