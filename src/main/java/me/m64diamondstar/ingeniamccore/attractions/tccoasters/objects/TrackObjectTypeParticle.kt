package me.m64diamondstar.ingeniamccore.attractions.tccoasters.objects

import com.bergerkiller.bukkit.coasters.TCCoasters
import com.bergerkiller.bukkit.coasters.csv.TrackCSV.TrackObjectTypeEntry
import com.bergerkiller.bukkit.coasters.editor.PlayerEditState
import com.bergerkiller.bukkit.coasters.objects.TrackObjectType
import com.bergerkiller.bukkit.coasters.tracks.TrackConnection
import com.bergerkiller.bukkit.coasters.util.StringArrayBuffer
import com.bergerkiller.bukkit.common.map.MapCanvas
import com.bergerkiller.bukkit.common.map.widgets.MapWidget
import com.bergerkiller.bukkit.common.math.Matrix4x4
import me.m64diamondstar.ingeniamccore.attractions.tccoasters.particles.TrackParticleParticle
import org.bukkit.inventory.ItemStack
import java.util.function.Supplier

class TrackObjectTypeParticle private constructor(val width: Double?, val transformation: Matrix4x4?): TrackObjectType<TrackParticleParticle> {

    companion object {

        fun create(width: Double): TrackObjectTypeParticle {
            return TrackObjectTypeParticle(width, null)
        }

        fun createDefault(): TrackObjectTypeParticle {
            return create(1.0)
        }

    }

    override fun getTitle(): String? {
        return "Particle"
    }

    override fun getWidth(): Double {
        return this.width ?: 0.0
    }

    override fun setWidth(width: Double): TrackObjectType<TrackParticleParticle>? {
        return TrackObjectTypeParticle(width, this.transformation)
    }

    override fun getTransform(): Matrix4x4? {
        return this.transformation
    }

    override fun setTransform(transform: Matrix4x4?): TrackObjectType<TrackParticleParticle>? {
        return TrackObjectTypeParticle(this.width, transform)
    }

    override fun generateName(): String? {
        return "particle"
    }

    override fun createParticle(point: TrackConnection.PointOnPath?): TrackParticleParticle? {
        val particle = point?.world?.particles?.addParticle<TrackParticleParticle>(TrackParticleParticle(point.position, point))
        particle?.isAlwaysVisible = true
        return particle
    }

    override fun updateParticle(particle: TrackParticleParticle?, point: TrackConnection.PointOnPath?) {
        particle?.updatePosition(point?.position, point)
    }

    override fun isSameImage(type: TrackObjectType<*>?): Boolean {
        return true
    }

    override fun drawImage(plugin: TCCoasters?, canvas: MapCanvas?) {
        canvas?.draw(plugin?.loadTexture("com/bergerkiller/bukkit/coasters/resources/leash.png"), 0, 0)
    }

    override fun openMenu(parent: MapWidget?, stateSupplier: Supplier<PlayerEditState?>?) {}

    override fun acceptItem(item: ItemStack?): TrackObjectType<TrackParticleParticle>? {
        return this
    }

    override fun toString(): String {
        return "{TrackObjectType[Particle]}"
    }

    class CSVEntry : TrackObjectTypeEntry<TrackObjectTypeParticle>() {
        override fun getType(): String {
            return "PARTICLE"
        }

        override fun getDefaultType(): TrackObjectTypeParticle {
            return createDefault()
        }

        override fun readDetails(buffer: StringArrayBuffer): TrackObjectTypeParticle {
            return create(this.width)
        }

        override fun writeDetails(buffer: StringArrayBuffer, objectType: TrackObjectTypeParticle) {
        }
    }

}