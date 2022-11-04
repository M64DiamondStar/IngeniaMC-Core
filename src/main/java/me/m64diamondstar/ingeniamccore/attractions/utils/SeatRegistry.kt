package me.m64diamondstar.ingeniamccore.attractions.utils

import java.util.UUID
import me.m64diamondstar.ingeniamccore.attractions.utils.SeatRegistry
import org.bukkit.entity.Entity
import java.util.ArrayList

object SeatRegistry {

    private val seatEntities: MutableList<UUID> = ArrayList()

    fun addEntity(entity: Entity) {
        seatEntities.add(entity.uniqueId)
    }

    operator fun contains(entity: Entity): Boolean {
        return seatEntities.contains(entity.uniqueId)
    }

    fun getList(): MutableList<UUID> {
        return seatEntities
    }
}