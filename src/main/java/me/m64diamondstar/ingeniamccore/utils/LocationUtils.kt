package me.m64diamondstar.ingeniamccore.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import org.joml.Vector3f
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*

object LocationUtils {

    private val EPSILON = Math.ulp(1.0) * 2.0

    /**
     * Get location from format (world, x, y, z[, yaw, pitch])
     *
     */
    fun getLocationFromString(string: String?): Location?{
        if(string == null)
            return null
        var args = string.split(", ")
        if(args.size == 1)
            args = string.split(",")
        if(args.size == 1)
            args = string.split(" - ")
        if(args.size == 1)
            return null

        val world = Bukkit.getWorld(args[0]) ?: return null
        val x: Double
        val y: Double
        val z: Double
        var yaw = 0F
        var pitch = 0F

        try{
            x = args[1].toDouble()
            y = args[2].toDouble()
            z = args[3].toDouble()
            if(args.size == 6){
                yaw = args[4].toFloat()
                pitch = args[5].toFloat()
            }
        }catch (e: NumberFormatException){
            return null
        }

        return Location(world, x, y, z, yaw, pitch)
    }

    fun getVectorFromString(string: String): Vector?{
        var args = string.split(", ")
        if(args.size == 1)
            args = string.split(",")
        if(args.size == 1)
            return null

        val x: Double
        val y: Double
        val z: Double

        try{
            x = args[0].toDouble()
            y = args[1].toDouble()
            z = args[2].toDouble()
        }catch (e: NumberFormatException){
            return null
        }

        return Vector(x, y, z)
    }

    fun getStringFromLocation(location: Location): String{
        return "${location.world?.name}," +
                " ${BigDecimal(location.x).setScale(3, RoundingMode.HALF_EVEN)}," +
                " ${BigDecimal(location.y).setScale(3, RoundingMode.HALF_EVEN)}," +
                " ${BigDecimal(location.z).setScale(3, RoundingMode.HALF_EVEN)}," +
                " ${location.yaw}," +
                " ${location.pitch}"
    }

    /**
     * Credits to Wasabi_Thumbs on SpigotMC
     * https://www.spigotmc.org/threads/getting-location-to-the-relative-left-or-right-of-player.580325/#post-4509362
     */
    private fun isSignificant(value: Double): Boolean {
        return abs(value) >= EPSILON
    }

    /**
     * Gets the location relative to the entity's yaw and pitch
     *
     * Credits to Wasabi_Thumbs on SpigotMC
     * https://www.spigotmc.org/threads/getting-location-to-the-relative-left-or-right-of-player.580325/#post-4509362
     */
    fun getRelativeLocation(entity: Entity, forward: Double, right: Double, up: Double): Location {
        return if (entity is LivingEntity) {
            getRelativeLocation(entity.eyeLocation, forward, right, up)
        } else {
            getRelativeLocation(entity.location, forward, right, up)
        }
    }

    /**
     * Gets the location relative to the entity's yaw and pitch
     *
     * Credits to Wasabi_Thumbs on SpigotMC
     * https://www.spigotmc.org/threads/getting-location-to-the-relative-left-or-right-of-player.580325/#post-4509362
     */
    fun getRelativeLocation(location: Location, forward: Double, right: Double, up: Double): Location {
        val loc = location.clone()
        var direction: Vector? = null
        if (isSignificant(forward)) {
            direction = loc.direction
            loc.add(direction.clone().multiply(forward))
        }
        val hasUp: Boolean = isSignificant(up)
        if (hasUp && direction == null) direction = loc.direction
        if (isSignificant(right) || hasUp) {
            val rightDirection: Vector
            if (direction != null && isSignificant(abs(direction.y) - 1)) {
                rightDirection = direction.clone()
                val factor = sqrt(1 - rightDirection.y.pow(2.0)) // a shortcut that lets us not normalize which is slow
                val nx = -rightDirection.z / factor
                val nz = rightDirection.x / factor
                rightDirection.setX(nx)
                rightDirection.setY(0.0)
                rightDirection.setZ(nz)
            } else {
                val yaw = loc.yaw + 90f
                val yawRad = yaw * (Math.PI / 180.0)
                val z = cos(yawRad)
                val x = -sin(yawRad)
                rightDirection = Vector(x, 0.0, z)
            }
            loc.add(rightDirection.clone().multiply(right))
            if (hasUp) {
                val upDirection = rightDirection.crossProduct(direction!!)
                loc.add(upDirection.clone().multiply(up))
            }
        }
        return loc
    }

    fun getYawLookAt(fromLocation: Location, toLocation: Location): Float{
        val direction = toLocation.toVector().subtract(fromLocation.toVector())
        val yaw = (-Math.toDegrees(atan2(direction.x, direction.z))).toFloat()
        return if (yaw < 0) yaw + 360 else yaw
    }

    fun getPitchLookAt(fromLocation: Location, toLocation: Location): Float{
        val deltaX = toLocation.x - fromLocation.x
        val deltaY = toLocation.y - fromLocation.y
        val deltaZ = toLocation.z - fromLocation.z

        val distFlat = sqrt(deltaX * deltaX + deltaZ * deltaZ)
        return Math.toDegrees(-atan2(deltaY, distFlat)).toFloat()
    }

    data class Cuboid(val bottomCorner: Vector3f, val topCorner: Vector3f)

    fun isPointInsideCuboid(point: Vector3f, cuboid: Cuboid): Boolean {
        val minX = minOf(cuboid.bottomCorner.x, cuboid.topCorner.x)
        val maxX = maxOf(cuboid.bottomCorner.x, cuboid.topCorner.x)
        val minY = minOf(cuboid.bottomCorner.y, cuboid.topCorner.y)
        val maxY = maxOf(cuboid.bottomCorner.y, cuboid.topCorner.y)
        val minZ = minOf(cuboid.bottomCorner.z, cuboid.topCorner.z)
        val maxZ = maxOf(cuboid.bottomCorner.z, cuboid.topCorner.z)

        return point.x in minX..maxX && point.y in minY..maxY && point.z in minZ..maxZ
    }


}