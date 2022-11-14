package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.shows.utils.EffectType
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.*
import org.bukkit.Particle

class Particle(show: Show, id: Int) : EffectType(show, id) {

    init{
        getShow().getConfig().set("$id.Type", "Particle")
    }

    override fun execute() {
        val location = LocationUtils.getLocationFromString(getSection().getString("Location")!!) ?: return
        val particle = getSection().getString("Particle")?.let { Particle.valueOf(it) } ?: return
        val amount = if (getSection().get("Amount") != null) getSection().getInt("Amount") else 0
        val dX = if (getSection().get("dX") != null) getSection().getDouble("dX") else 0.0
        val dY = if (getSection().get("dY") != null) getSection().getDouble("dY") else 0.0
        val dZ = if (getSection().get("dZ") != null) getSection().getDouble("dZ") else 0.0
        val force = if (getSection().get("Force") != null) getSection().getBoolean("Force") else false


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
                location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, 0.0, dustOptions, force)
            }
            Particle.BLOCK_CRACK, Particle.BLOCK_DUST -> {
                val material =
                    if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!) else Material.STONE
                location.world!!.spawnParticle(
                    particle,
                    location,
                    amount,
                    dX,
                    dY,
                    dZ,
                    0.0,
                    material.createBlockData(),
                    force
                )
            }
            else -> {location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, 0.0, null, force)}
        }
    }

    override fun getType(): Types {
        return Types.PARTICLE
    }

    override fun isSync(): Boolean {
        return false
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, delay: Int){
        getSection().set("Type", "Particle")
        getSection().set("Location", "${location.world!!.name}, ${location.x}, ${location.y}, ${location.z}")
        getSection().set("Particle", particle.toString())
        getSection().set("Amount", amount)
        getSection().set("dX", dX)
        getSection().set("dY", dY)
        getSection().set("dZ", dZ)
        getSection().set("Force", force)
        getSection().set("Delay", delay)
        getShow().reloadConfig()
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, material: Material, delay: Int ){
        setup(location, particle, amount, dX, dY, dZ, force, delay)
        getSection().set("Block", material.toString())
        getShow().reloadConfig()
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, color: Color, delay: Int ){
        setup(location, particle, amount, dX, dY, dZ, force, delay)
        getSection().set("Color", "${color.red}, ${color.green}, ${color.blue}")
        getShow().reloadConfig()
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, color: Color, size: Float, delay: Int){
        setup(location, particle, amount, dX, dY, dZ, force, color, delay)
        getSection().set("Size", "$size")
        getShow().reloadConfig()
    }


}