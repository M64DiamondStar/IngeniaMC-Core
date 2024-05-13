package me.m64diamondstar.ingeniamccore.utils.entities

import net.minecraft.world.entity.Entity

object EntityRegistry {
    private val map: MutableMap<Int, Class<out Entity?>> = HashMap()
    private val bukkitMap: ArrayList<org.bukkit.entity.Entity> = ArrayList()

    fun addEntity(id: Int, entityClass: Class<out Entity?>) {
        map[id] = entityClass
    }

    private fun getMap(): Map<Int, Class<out Entity?>> {
        return map
    }

    /**
     * These are temporary entities which need to be deleted on server shutdown
     */
    fun addBukkitEntity(entity: org.bukkit.entity.Entity) {
        bukkitMap.add(entity)
    }

    fun getBukkitMap(): ArrayList<org.bukkit.entity.Entity> {
        return bukkitMap
    }

    fun containsId(id: Int): Boolean {
        return getMap().containsKey(id)
    }
}
