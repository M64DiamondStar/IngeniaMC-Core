package me.m64diamondstar.ingeniamccore.attractions.custom

import com.bergerkiller.bukkit.common.math.Matrix4x4
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.attractions.Attraction
import me.m64diamondstar.ingeniamccore.attractions.custom.utils.CountdownRegistry
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionManager
import me.m64diamondstar.ingeniamccore.attractions.utils.AttractionType
import me.m64diamondstar.ingeniamccore.attractions.utils.CustomAttraction
import net.minecraft.world.entity.MoverType
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Frisbee(private val category: String, private val name: String): Attraction(category, name), CustomAttraction {

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

            if(AttractionType.valueOf(Objects.requireNonNull<String?>(entity.customName).split("-").toTypedArray()[0]) == AttractionType.FRISBEE){
                val frisbee = Frisbee(entity.customName!!.split("-")[3], entity.customName!!.split("-")[2])

                if(entity.passengers.size != 0)
                    return
                if(!CountdownRegistry.isCountingDown(frisbee.category, frisbee.name)) {
                    frisbee.countdown()
                    CountdownRegistry.setCountingDown(frisbee.category, frisbee.name, true)
                }

                entity.addPassenger(player)
            }
        }
    }


    /**
     * Calculates the coordinates of the pendulum
     * @param speed the speed of the pendulum
     * @param armLength the length of the arm of the pendulum in blocks
     * @param currentFrame the current frame to get the coordinates of
     */
    private fun calculatePendulumCoordinates(speed: Double, armLength: Double, currentFrame: Double): Pair<Double, Double> {
        val angle = Math.toRadians((speed * (currentFrame + 180)) % 360)

        val x = armLength * sin(angle)
        val y = armLength * cos(angle) + armLength

        return Pair(x, y)
    }




    /*
     * All Frisbee methods
     */
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
            stand.isPersistent = true
            stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING)
            stand.customName = "FRISBEE-" + i * 22.5f + "-" + getName() + "-" + getCategory()

            getConfig().set("Data.Stands.${stand.uniqueId}.Index", i)

            stand.location.chunk.addPluginChunkTicket(IngeniaMC.plugin)
        }
        reloadConfig()
    }

    override fun despawn() {
        if(getConfig().getConfigurationSection("Data.Stands") == null ||
            getConfig().getConfigurationSection("Data.Stands")!!.getValues(false).isEmpty())
            return

        for(uuid in getConfig().getConfigurationSection("Data.Stands")!!.getValues(false)){
            Bukkit.getEntity(UUID.fromString(uuid.key))?.remove()
        }

        getConfig().set("Data.Stands", null)
        reloadConfig()
    }

    fun setAxis(axis: Char){
        getConfig().set("Data.Axis", axis)
        reloadConfig()
    }

    fun getAxis(): Char{
        return getConfig().getString("Data.Axis")?.get(0) ?: 'x'
    }

    fun setArmLength(armLength: Double){
        getConfig().set("Data.ArmLength", armLength)
        reloadConfig()
    }

    fun getArmLength(): Double{
        return getConfig().getDouble("Data.ArmLength")
    }

    override fun countdown() {

        if(this.hasPassengers())
            return

        val customAttraction: CustomAttraction = this

        object: BukkitRunnable(){
            var c = customAttraction.getAttraction().getCountdownTime()
            override fun run() {

                if(c == 0){
                    customAttraction.dispatch()
                    this.cancel()
                    return
                }

                var hasPassenger = false
                customAttraction.getSeats().forEach {
                    if(it.passengers.size == 1){
                        customAttraction.getAttraction().getCountdownType().sendActionBarMessage(player = it.passengers[0] as Player, c)
                        hasPassenger = true
                    }
                }

                if(!hasPassenger){
                    CountdownRegistry.setCountingDown(customAttraction.getAttraction().getCategory(), customAttraction.getAttraction().getName(), false)
                    this.cancel()
                    return
                }

                c -= 1
            }
        }.runTaskTimer(IngeniaMC.plugin, 5L, 20L)

    }

    /**
     * Start the Frisbee ride.
     */
    override fun dispatch() {

        if(getSpawnLocation() == null) return
        CountdownRegistry.setCountingDown(category, name, false)
        AttractionManager.setLocked(this@Frisbee, locked = true)
        if(getShow() != null) getShow()!!.play()

        object : BukkitRunnable() {
            var c = 1
            val armLength = getArmLength()
            override fun run() {

                if(getConfig().getConfigurationSection("Data.Stands") == null){
                    AttractionManager.setLocked(this@Frisbee, locked = false)
                    this.cancel()
                    return
                }

                // Create new center coordinates and location
                val newCoordinates = calculatePendulumCoordinates(1.0, armLength, c.toDouble())
                val newLocation = getSpawnLocation()!!.clone().add(
                    if(getAxis() == 'x') newCoordinates.first else 0.0,
                    newCoordinates.second,
                    if(getAxis() == 'z') newCoordinates.first else 0.0
                )

                // Move each individual seat
                getSeats().forEach {
                    val x = 3.75 * sin(-it.location.yaw / 180 * Math.PI)
                    val z = 3.75 * cos(-it.location.yaw / 180 * Math.PI)
                    val matrix4x4 = Matrix4x4()
                    matrix4x4.rotateYawPitchRoll(
                        c.toDouble(),
                        0.0,
                        0.0
                    )
                    matrix4x4.translate(Vector(x, 0.0, z))

                    val vector3 = matrix4x4.toVector3()
                    val location = newLocation.clone().add(vector3.x, vector3.y, vector3.z)

                    (it as CraftArmorStand).handle.move(MoverType.SELF, Vec3(
                        location.x - it.location.x,
                        location.y - it.location.y,
                        location.z - it.location.z
                    ))
                    it.setRotation(it.location.yaw - 1.25f, 0f)

                    val directionMatrix = Matrix4x4()
                    directionMatrix.rotateYawPitchRoll(
                        -c.toDouble(),
                        it.location.yaw.toDouble(),
                        0.0
                    )
                    val quaternion = directionMatrix.rotation
                    if(it.passengers.isNotEmpty() && it.passengers[0] != null && it.passengers[0] is Player)
                        IngeniaMC.smoothCoastersAPI.setRotation(null,
                            it.passengers[0] as Player,
                            quaternion.x.toFloat(),
                            quaternion.y.toFloat(),
                            quaternion.z.toFloat(),
                            quaternion.w.toFloat(),
                            3)
                }

                if(c == 720){
                    AttractionManager.setLocked(this@Frisbee, locked = false)
                    this.cancel()
                    return
                }

                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)
    }

    override fun getSeats(): List<ArmorStand> {
        val list = ArrayList<ArmorStand>()
        if(getConfig().getConfigurationSection("Data.Stands") == null) return emptyList()
        for(uuid in getConfig().getConfigurationSection("Data.Stands")!!.getValues(false)){
            if(Bukkit.getEntity(UUID.fromString(uuid.key)) != null)
                list.add(Bukkit.getEntity(UUID.fromString(uuid.key)) as ArmorStand)
        }
        return list
    }

    override fun hasPassengers(): Boolean {
        var hasPassengers = false
        getSeats().forEach { if(it.passengers.size > 0) hasPassengers = true }

        return hasPassengers
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

    override fun getAttraction(): Attraction {
        return Attraction(category, name)
    }
}