package me.m64diamondstar.ingeniamccore.utils.entities

import net.minecraft.world.entity.Entity

object EntityRegistry {
    private val map: MutableMap<Int, Class<out Entity?>> = HashMap()

    fun addEntity(id: Int, entityClass: Class<out Entity?>) {
        map[id] = entityClass
    }

    private fun getMap(): Map<Int, Class<out Entity?>> {
        return map
    }

    fun containsId(id: Int): Boolean {
        return getMap().containsKey(id)
    }
}
