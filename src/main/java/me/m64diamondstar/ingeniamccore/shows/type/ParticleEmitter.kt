package me.m64diamondstar.ingeniamccore.shows.type

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.utils.EffectType
import me.m64diamondstar.ingeniamccore.shows.utils.Show
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.*
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class ParticleEmitter(show: Show, id: Int) : EffectType(show, id) {

    init{
        getShow().getConfig().set("$id.Type", "Particle-Emitter")
    }

    override fun execute() {
        val location = LocationUtils.getLocationFromString(getSection().getString("Location")!!) ?: return
        val particle = getSection().getString("Particle")?.let { Particle.valueOf(it) } ?: return
        val amount = if (getSection().get("Amount") != null) getSection().getInt("Amount") else 0
        val dX = if (getSection().get("dX") != null) getSection().getDouble("dX") else 0.0
        val dY = if (getSection().get("dY") != null) getSection().getDouble("dY") else 0.0
        val dZ = if (getSection().get("dZ") != null) getSection().getDouble("dZ") else 0.0
        val length = if (getSection().get("Length") != null) getSection().getInt("Length") else 1
        val force = if (getSection().get("Force") != null) getSection().getBoolean("Force") else false
        val extra = if(amount == 0) 1.0 else 0.0

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

                object: BukkitRunnable(){
                    var c = 0
                    override fun run() {
                        if(c == length){
                            this.cancel()
                            return
                        }
                        location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, dustOptions, force)
                        c++
                    }
                }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)
            }

            Particle.BLOCK_CRACK, Particle.BLOCK_DUST, Particle.FALLING_DUST -> {
                val material =
                    if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!) else Material.STONE
                object: BukkitRunnable(){
                    var c = 0
                    override fun run() {

                        if(c == length){
                            this.cancel()
                            return
                        }
                        location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, material.createBlockData(), force)
                        c++
                    }
                }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)
            }

            Particle.ITEM_CRACK -> {
                val material =
                    if (getSection().get("Block") != null) Material.valueOf(getSection().getString("Block")!!) else Material.STONE
                object: BukkitRunnable(){
                    var c = 0
                    override fun run() {

                        if(c == length){
                            this.cancel()
                            return
                        }
                        location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, ItemStack(material), force)
                        c++
                    }
                }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)
            }

            else -> {
                object: BukkitRunnable(){
                    var c = 0
                    override fun run() {
                        if(c == length){
                            this.cancel()
                            return
                        }
                        location.world!!.spawnParticle(particle, location, amount, dX, dY, dZ, extra, null, force)
                        c++
                    }
                }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)
            }
        }
    }

    override fun getType(): Types {
        return Types.PARTICLE_EMITTER
    }

    override fun isSync(): Boolean {
        return false
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, length: Int, delay: Int){
        getSection().set("Type", "Particle")
        getSection().set("Location", "${location.world!!.name}, ${location.x}, ${location.y}, ${location.z}")
        getSection().set("Particle", particle.toString())
        getSection().set("Amount", amount)
        getSection().set("dX", dX)
        getSection().set("dY", dY)
        getSection().set("dZ", dZ)
        getSection().set("Force", force)
        getSection().set("Length", length)
        getSection().set("Delay", delay)
        getShow().reloadConfig()
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, length: Int, material: Material, delay: Int ){
        setup(location, particle, amount, dX, dY, dZ, force, length, delay)
        getSection().set("Block", material.toString())
        getShow().reloadConfig()
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, length: Int, color: Color, delay: Int ){
        setup(location, particle, amount, dX, dY, dZ, force, length, delay)
        getSection().set("Color", "${color.red}, ${color.green}, ${color.blue}")
        getShow().reloadConfig()
    }

    fun setup(location: Location, particle: Particle, amount: Int, dX: Double, dY: Double, dZ: Double, force: Boolean, length: Int, color: Color, size: Float, delay: Int){
        setup(location, particle, amount, dX, dY, dZ, force, length, color, delay)
        getSection().set("Size", "$size")
        getShow().reloadConfig()
    }


}