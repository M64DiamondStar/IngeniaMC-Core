package me.m64diamondstar.ingeniamccore.attractions.tccoasters.particles

import com.bergerkiller.bukkit.coasters.particles.TrackParticle
import com.bergerkiller.bukkit.coasters.tracks.TrackConnection
import com.bergerkiller.bukkit.common.collections.octree.DoubleOctree
import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.random.Random

class TrackParticleParticle(position: Vector, private var point: TrackConnection.PointOnPath?): TrackParticle() {

    private var position: DoubleOctree. Entry<TrackParticle> = DoubleOctree.Entry.create(position, this)
    private val players: ArrayList<Player> = ArrayList()
    private var isRunning = false

    private val runnable = object: BukkitRunnable(){
        override fun run(){
            val vec = point?.orientation?.forwardVector() ?: Vector(0.0, 0.0, 0.0)
            players.forEach {
                it.spawnParticle(
                    Particle.SNOWFLAKE,
                    position.toLocation(it.world).clone().add(Random.nextDouble(-0.3, 0.3), Random.nextDouble(0.1, 0.3), Random.nextDouble(-0.3, 0.3)),
                    0,
                    vec.x,
                    vec.y,
                    vec.z,
                    0.25)
            }
        }
    }

    fun updatePosition(position: Vector?, point: TrackConnection.PointOnPath?){
        this.position = updatePosition(this.position, position)
        this.point = point
    }

    override fun onAdded() {
        addPosition(this.position)
    }

    override fun onRemoved() {
        removePosition(this.position)
    }

    override fun distanceSquared(viewerPosition: Vector?): Double {
        return this.position.distanceSquared(viewerPosition)
    }

    override fun makeVisibleFor(viewer: Player?) {
        if(!isRunning) {
            runnable.runTaskTimerAsynchronously(IngeniaMC.plugin, 0, 1)
            isRunning = true
        }
        players.add(viewer ?: return)
    }

    override fun makeHiddenFor(viewer: Player?) {
        players.remove(viewer)
    }

    override fun updateAppearance() {

    }

    override fun usesEntityId(entityId: Int): Boolean {
        return false
    }
}