package me.m64diamondstar.ingeniamccore.entity.body

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.entity.util.EntityUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class NametagEntity(private val owner: Player): PassengerEntity(owner = owner) {

    /**
     * Class which manages all the name tag actions, when nametags should be
     * applied, disabled, shown, hidden, ...
     */
    object NametagManager{

        private val nametags = ConcurrentHashMap<UUID, NametagEntity>()
        private val entityIdToDisplay = ConcurrentHashMap<Int, NametagEntity>()
        private val blocked: MutableSet<UUID> = Sets.newConcurrentHashSet()
        private val mounted: MutableSet<UUID> = Sets.newConcurrentHashSet()
        private val tasks: CopyOnWriteArrayList<BukkitTask> = Lists.newCopyOnWriteArrayList()
        private val viewers: ConcurrentHashMap<UUID, HashSet<UUID>> = ConcurrentHashMap()

        init {
            Bukkit.getOnlinePlayers().forEach { addPlayer(it) }
            startTask()

            IngeniaMC.addOnDisableRunnable(Runnable { removeAll() })
        }

        private fun startTask(){
            tasks.forEach { it.cancel() }

            // Refresh passengers
            val passengers = object: BukkitRunnable(){
                override fun run() {
                    Bukkit.getOnlinePlayers().forEach { player ->
                        getPacketEntity(player)?.sendPassengerPacketToViewers()
                    }
                }
            }.runTaskTimerAsynchronously(IngeniaMC.plugin, 10L, 20L)

            tasks.add(passengers)
        }

        fun addPlayer(player: Player){
            if(nametags.contains(player.uniqueId)) return
            if(blocked.contains(player.uniqueId)) return
            if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) return

            viewers[player.uniqueId] = hashSetOf(player.uniqueId)

            val entity = NametagEntity(player)
            entity.spawn(player.location.apply { this.yaw = 0f; this.pitch = 0f })

            handleVanish(player, entity)

            nametags[player.uniqueId] = entity
            entityIdToDisplay[entity.entity.entityId] = entity
        }

        fun removePlayer(player: Player){
            removePlayer(player.uniqueId)
        }

        fun removePlayer(uniqueId: UUID){
            if(!nametags.contains(uniqueId)) return

            val entity = nametags[uniqueId]!!
            nametags.remove(uniqueId)
            entityIdToDisplay.remove(entity.entity.entityId)
            entity.remove()
        }

        private fun handleVanish(player: Player, display: NametagEntity){
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
        fun removeAll(){
            nametags.forEach { it.value.remove() }
            nametags.clear()
            entityIdToDisplay.clear()
        }

        fun getPacketEntity(player: Player): NametagEntity? {
            if(!nametags.containsKey(player.uniqueId)) return null
            return nametags[player.uniqueId]
        }

        fun updateSneaking(player: Player, sneaking: Boolean){
            getPacketEntity(player)?.let {
                if(it.entityMeta.isSeeThrough){
                    it.entityMeta.isSeeThrough = !sneaking
                }

                it.entityMeta.textOpacity = if(sneaking) 110 else 0
                it.refresh()
            }
        }

        fun updateMounted(player: Player, mounted: Boolean){
            if(!viewers.containsKey(player.uniqueId)) return
            getPacketEntity(player)?.let {

                if(mounted) { // Make display invisible for every viewer
                    viewers[player.uniqueId]!!.forEach { viewer ->
                        if(Bukkit.getPlayer(viewer) == null) return@forEach
                        it.setVisible(Bukkit.getPlayer(viewer)!!, false)
                    }
                    NametagManager.mounted.add(player.uniqueId)
                }
                else { // Make display visible for every viewer
                    NametagManager.mounted.remove(player.uniqueId)

                    viewers[player.uniqueId]!!.forEach { viewer ->
                        if(Bukkit.getPlayer(viewer) == null) return@forEach
                        updateEntity(Bukkit.getPlayer(viewer)!!, player)
                    }
                }
            }
        }

        fun updateEntity(player: Player, target: Player){
            viewers[player.uniqueId]?.add(target.uniqueId)

            if(mounted.contains(target.uniqueId)) return

            getPacketEntity(target)?.let {
                it.hideFromPlayerSilently(player)
                it.setVisible(player, true)
            }
        }

        fun removeEntity(player: Player, target: Player){
            viewers[player.uniqueId]?.remove(target.uniqueId)

            getPacketEntity(target)?.setVisible(player, false)
        }

        fun blockPlayer(player: Player){
            blocked.add(player.uniqueId)
            Bukkit.getOnlinePlayers().forEach {
                getPacketEntity(it)?.setVisible(player, false)
            }
        }

        fun unblockPlayer(player: Player){
            blocked.remove(player.uniqueId)
            Bukkit.getOnlinePlayers().forEach {
                getPacketEntity(it)?.setVisible(player, true)
            }
        }

    }

    /*
     * NametagEntity class
     * starts here!
     */

    private val entity: WrapperEntity = WrapperEntity(EntityUtils.nextEntityId(), UUID.randomUUID(), EntityTypes.TEXT_DISPLAY)
    private val entityMeta: TextDisplayMeta = entity.entityMeta as TextDisplayMeta
    private var entitySpawned: Boolean = false

    init {
        entityMeta.setNotifyAboutChanges(false)
        entityMeta.lineWidth = 1000
        entityMeta.backgroundColor = Color.fromARGB(0, 0, 0, 0).asARGB()
        entityMeta.viewRange = 60f / 160f
        entityMeta.billboardConstraints = AbstractDisplayMeta.BillboardConstraints.CENTER
        entityMeta.isUseDefaultBackground = false
        entityMeta.scale = entityMeta.scale.multiply(0.85f)
        entityMeta.translation = entityMeta.translation.add(0f, 0.2f, 0f)
        entityMeta.isShadow = true
        refreshText() // Sets the necessary text in the display and updates it too
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
                }
            }.runTaskLaterAsynchronously(IngeniaMC.plugin, 2L)
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

    /**
     * Sets the position of the display to the owner's location.
     */
    private fun setPosition(){
        val location = owner.location.clone()
        location.y += 1.8
        entity.setLocation(EntityUtils.convertBukkitToEntityLocation(location))
    }

    /**
     * Sets the text of the display.
     */
    fun refreshText(){
        val ingeniaPlayer = IngeniaPlayer(owner)
        entityMeta.text = if(ingeniaPlayer.playerConfig.getTitle() != null)
            Component.text()
                .append(MiniMessage.miniMessage().deserialize(ingeniaPlayer.playerConfig.getTitle()!! + "<br>"))
                .append(ingeniaPlayer.componentIconPrefix)
                .append(Component.text(" "))
                .append(ingeniaPlayer.nameLightColored)
                .build()
        else
            Component.text()
                .append(ingeniaPlayer.componentIconPrefix)
                .append(Component.text(" "))
                .append(ingeniaPlayer.nameLightColored)
                .build()
        refresh()
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