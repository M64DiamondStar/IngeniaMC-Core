package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.utils.EffectType
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class ParticleLine(show: Show, id: Int) : EffectType(show, id) {

    override fun execute() {

        val fromLocation = LocationUtils.getLocationFromString(getSection().getString("FromLocation")!!) ?: return
        val toLocation = LocationUtils.getLocationFromString(getSection().getString("ToLocation")!!) ?: return
        val particle = getSection().getString("Particle")?.let { Particle.valueOf(it) } ?: return
        val amount = if (getSection().get("Amount") != null) getSection().getInt("Amount") else 0
        val speed = if (getSection().get("Speed") != null) getSection().getInt("Speed") * 0.05 else 0.05
        val dX = if (getSection().get("dX") != null) getSection().getDouble("dX") else 0.0
        val dY = if (getSection().get("dY") != null) getSection().getDouble("dY") else 0.0
        val dZ = if (getSection().get("dZ") != null) getSection().getDouble("dZ") else 0.0
        val force = if (getSection().get("Force") != null) getSection().getBoolean("Force") else false
        val extra = if(amount == 0) 1.0 else 0.0

        val moveX: Double = (toLocation.x - fromLocation.x) / speed
        val moveY: Double = (toLocation.y - fromLocation.y) / speed
        val moveZ: Double = (toLocation.z - fromLocation.z) / speed

        var nx = moveX
        var ny = moveY
        var nz = moveZ
        if (nx < 0) nx = -nx
        if (ny < 0) ny = -ny
        if (nz < 0) nz = -nz

        var move = nx
        if (ny > nx && ny > nz) move = ny
        if (nz > ny && nz > nx) move = nz

        val x: Double = moveX / move / 20.0 * (speed * 20.0)
        val y: Double = moveY / move / 20.0 * (speed * 20.0)
        val z: Double = moveZ / move / 20.0 * (speed * 20.0)

        val finalMove = move

        object : BukkitRunnable() {
            var c = 0
            var location: Location = fromLocation
            override fun run() {
                if (c > finalMove) {
                    cancel()
                    return
                }
                when (particle) {
                    Particle.REDSTONE, Particle.SPELL_MOB, Particle.SPELL_MOB_AMBIENT -> {
                        val color = Colors.getJavaColorFromString(getSection().getString("Color")!!)
                        val dustOptions = Particle.DustOptions(
                            Color.fromRGB(color.red, color.green, color.blue),
                            if (getSection().get("Size") != null)
                                getSection().getInt("Size").toFloat()
                            else
                                1F
                        )
                        location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, dustOptions, force)
                    }
                    Particle.BLOCK_CRACK, Particle.BLOCK_DUST, Particle.FALLING_DUST -> {
                        val material =
                            if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!) else Material.STONE
                        location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, material.createBlockData(), force
                        )
                    }
                    Particle.ITEM_CRACK -> {
                        val material =
                            if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!) else Material.STONE
                        location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, ItemStack(material), force
                        )
                    }
                    else -> {location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, null, force)}
                }
                location.add(x, y, z)
                c++
            }
        }.runTaskTimer(IngeniaMC.plugin, 0L, 1L)

    }

    override fun getType(): Types {
        return Types.PARTICLE_LINE
    }

    override fun isSync(): Boolean {
        return true
    }
}