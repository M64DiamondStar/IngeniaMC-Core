package me.m64diamondstar.ingeniamccore.utils.entities

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType

object EntityUtils {

    /**
     * Determines whether the player may enter or leave this
     * entity as a passenger.
     */
    fun setLocked(entity: Entity, locked: Boolean){
        val container = entity.persistentDataContainer
        container.set(NamespacedKey(IngeniaMC.plugin, "locked"), PersistentDataType.STRING, locked.toString())
    }

    /**
     * @return whether the entity is locked or not
     */
    fun isLocked(entity: Entity): Boolean{
        val container = entity.persistentDataContainer
        return try {
            container.get(NamespacedKey(IngeniaMC.plugin, "locked"), PersistentDataType.STRING).toBoolean()
        }catch (e: Exception){
            false
        }
    }

}