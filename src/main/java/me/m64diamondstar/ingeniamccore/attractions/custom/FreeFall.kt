package me.m64diamondstar.ingeniamccore.attractions.custom

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.utils.Attraction
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionManager
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.CustomAttraction
import me.m64diamondstar.ingeniamccore.utils.messages.Messages
import net.minecraft.world.entity.EnumMoveType
import net.minecraft.world.phys.Vec3D
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArmorStand
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom attraction with a big drop tower that goes up, waits, goes down with high
 * speed, goes up again but with high speed and then goes down back to the station.
 */
class FreeFall(private val category: String, private val name: String): Attraction(category, name), CustomAttraction {

    companion object {
        fun clickEvent(event: PlayerInteractAtEntityEvent){
            val entity = event.rightClicked
            val player = event.player

            if(entity.type != EntityType.ARMOR_STAND) return
            if(entity.customName == null) return

            try{
                AttractionType.valueOf(entity.customName!!.split("-")[0])
            }catch (e: NullPointerException){
                return
            }catch (e: IllegalArgumentException){
                return
            }

            event.isCancelled = true

            if(AttractionType.valueOf(Objects.requireNonNull<String?>(entity.customName).split("-").toTypedArray()[0]) == AttractionType.FREEFALL){
                val freefall = FreeFall(entity.customName!!.split("-")[3], entity.customName!!.split("-")[2])

                if(entity.passengers.size != 0)
                    return

                AttractionManager.countdown(freefall)
                entity.addPassenger(player)

            }
        }
    }

    override fun spawn() {
        for (i in 0..15) {
            val x = 3.75f * sin(i.toDouble() * 45 / 360 * Math.PI)
            val z = 3.75f * cos(i.toDouble() * 45 / 360 * Math.PI)
            val loc = Location(getWorld(), getSpawnLocation()!!.x + x, getSpawnLocation()!!.y, getSpawnLocation()!!.z + z)
            loc.yaw = -(22.5f * i.toFloat())
            val stand = getWorld()?.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand
            val item = ItemStack(Material.DIAMOND_SWORD)
            val meta = item.itemMeta
            meta!!.setCustomModelData(9)
            item.itemMeta = meta
            stand.equipment!!.helmet = item
            stand.isInvisible = true
            stand.isInvulnerable = true
            stand.setGravity(false)
            stand.isPersistent = false
            stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING)
            stand.customName = "FREEFALL-" + i * 45 + "-" + getName() + "-" + getCategory()

            getConfig().set("Data.Stands.${stand.uniqueId}.Index", i)

            stand.location.chunk.addPluginChunkTicket(IngeniaMC.plugin)
        }
        reloadConfig()
    }

    override fun despawn() {
        for(uuid in getConfig().getConfigurationSection("Data.Stands")!!.getValues(false)){
            Bukkit.getEntity(UUID.fromString(uuid.key))?.remove()
        }

        getConfig().set("Data.Stands", null)
        reloadConfig()
    }

    override fun hasPassengers(): Boolean {
        var hasPassengers = false
        getSeats().forEach { if(it.passengers.size > 0) hasPassengers = true }

        return hasPassengers
    }

    override fun getSeats(): List<ArmorStand> {
        val list = ArrayList<ArmorStand>()
        for(uuid in getConfig().getConfigurationSection("Data.Stands")!!.getValues(false)){
            if(Bukkit.getEntity(UUID.fromString(uuid.key)) != null)
            list.add(Bukkit.getEntity(UUID.fromString(uuid.key)) as ArmorStand)
        }
        return list
    }

    override fun getSpawnLocation(): Location?{
        if(this.getConfig().get("Settings.SpawnLocation") == null)
            return null

        val args = this.getConfig().getString("Settings.SpawnLocation")?.split(", ")
        val x = args?.get(0)?.toDouble()!!
        val y = args[1].toDouble()
        val z = args[2].toDouble()

        return Location(getWorld(), x, y, z)
    }

    override fun setSpawnLocation(location: Location){
        this.getConfig().set("Settings.SpawnLocation", "${location.x}, ${location.y}, ${location.z}")
        this.reloadConfig()
    }

    override fun dispatch() {
        AttractionManager.setLocked(this@FreeFall, locked = true)
        if(getShow() != null) getShow()!!.play()
        object : BukkitRunnable() {
            var c = 0.0
            val radius = 3.75f
            var angle = 0f
            var y = 0.0
            var stop = 0f
            override fun run() {

                if(getConfig().getConfigurationSection("Data.Stands") == null){
                    this.cancel()
                    return
                }

                if (c > 40 && c <= 620) // Y = 0 -> 58
                    y = 0.1

                else if (c > 620 && c <= 840) // Y = 58
                    y = 0.0

                else if (c > 902 && c <= 950) // Y = 58 -> 10
                    y = -1.0

                else if (c > 950 && c <= 982) { // Y = 10 -> 2 -> 10 (smooth)

                    if (stop == 0f)
                        y = -1.0

                    if (stop > 0)
                        y = 8 * sin((stop / 32 + 1) * Math.PI) - 8 * sin(((stop - 1) / 32 + 1) * Math.PI)

                    if (stop > 8)
                        y = 8 * (sin((stop / 32 + 1) * Math.PI) + 1) - 8 * (sin(((stop - 1) / 32 + 1) * Math.PI) + 1)

                    stop += 1f
                }

                else if (c > 982 && c <= 1014) { // Y = 10 -> 42 (+32)
                    y = 1.0
                    stop = 0f
                }

                else if (c > 1014 && c <= 1046) { // Y = 42 -> 52 -> 42
                    if (stop == 0f)
                        y = 1.0

                    if (stop > 0)

                        y = 8 * sin(stop / 32 * Math.PI) - 8 * sin((stop - 1) / 32 * Math.PI)

                    if (stop > 8)
                        y = 8 * (sin(stop / 32 * Math.PI) + 1) - 8 * (sin((stop - 1) / 32 * Math.PI) + 1)

                    stop += 1f
                }

                else if (c > 1046 && c <= 1079) { // Y = 42 -> 8 (-34)
                    y = -1.0
                    stop = 0f
                }

                else if (c > 1079 && c <= 1111) { // Y = 8 -> 0 (Smooth)
                    if (stop == 0f)
                        y = -1.0

                    if (stop > 0)
                        y = 8 * sin((stop / 64 + 1) * Math.PI) - 8 * sin(((stop - 1) / 64 + 1) * Math.PI)
                    stop += 1f
                }

                else if (c > 1111) {
                    y = 0.0
                    stop = 0f
                }

                if(c == 1160.0){
                    AttractionManager.setLocked(this@FreeFall, locked = false)
                }

                // RUN FOR EVERY ARMORSTAND
                for (stand in getSeats()) {
                    val standYaw = stand.location.yaw
                    val x = radius * sin(
                        (angle + Objects.requireNonNull<String?>(stand.customName).split("-")
                            .toTypedArray()[1].toDouble()) / 360 * Math.PI
                    )
                    val z = radius * cos(
                        (angle + stand.customName!!.split("-").toTypedArray()[1].toDouble()) / 360 * Math.PI
                    )
                    val prevX = radius * sin(
                        (angle - 2.5 + stand.customName!!.split("-").toTypedArray()[1].toDouble()) / 360 * Math.PI
                    )
                    val prevZ = radius * cos(
                        (angle - 2.5 + stand.customName!!.split("-").toTypedArray()[1].toDouble()) / 360 * Math.PI
                    )
                    var deltaX = x - prevX
                    var deltaZ = z - prevZ
                    if (c >= 864) {
                        deltaX = 0.0
                        deltaZ = 0.0
                    } else stand.setRotation(standYaw - 1.25f, 0f)
                    (stand as CraftArmorStand).handle.a(EnumMoveType.e, Vec3D(deltaX, y, deltaZ))
                    if (c == 1160.0) {

                        if (stand.getPassengers().isNotEmpty()) {
                            val passenger = stand.getPassengers()[0] as Player
                            if (passenger.isInsideVehicle) {
                                addRidecount(passenger, 1)
                                passenger.sendMessage(Messages.currentRidecount(getRidecount(passenger)))
                            }
                        }

                        stand.eject()
                        val i = stand.getCustomName()!!.split("-").toTypedArray()[1].toDouble() / 45
                        val xFix = 3.75f * sin(i * 45 / 360 * Math.PI)
                        val zFix = 3.75f * cos(i * 45 / 360 * Math.PI)
                        val loc = Location(getSpawnLocation()?.world, getSpawnLocation()!!.x + xFix, getSpawnLocation()!!.y, getSpawnLocation()!!.z + zFix
                        )
                        loc.yaw = -(22.5f * i.toFloat())
                        stand.teleport(loc)
                        openGates()
                    }
                }

                if (c == 1160.0) {

                    spawnRidecountSign()

                    cancel()
                    return
                }
                c += 1.0
                angle += 2.5.toFloat()
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)
    }

    override fun getAttraction(): Attraction {
        return Attraction(category, name)
    }

}