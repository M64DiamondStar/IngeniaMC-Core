package me.m64diamondstar.ingeniamccore.npc.utils

import com.ticxo.modelengine.api.entity.Dummy
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.npc.Npc
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType

object NpcRegistry {

    private val map = HashMap<String, Npc>()
    private val entityMap = HashMap<Dummy<*>, String>()
    private val allEntities = ArrayList<Dummy<*>>()

    fun addNpc(id: String, npc: Npc){
        map[id] = npc
        entityMap[npc.getBaseEntity()!!] = id
        allEntities.add(npc.getBaseEntity()!!)
    }

    fun removeNpc(id: String){
        map.remove(id)
        entityMap.remove(getNpc(id)?.getBaseEntity())
    }

    fun setNpcDummy(dummy: Dummy<*>, id: String){
        entityMap[dummy] = id
        allEntities.add(dummy)
    }

    fun getNpc(id: String): Npc?{
        return map[id]
    }

    fun getNpcID(dummy: Dummy<*>): String?{
        return entityMap[dummy]
    }

    fun getAllNpcs(): List<Npc>{
        return map.values.toList()
    }

    fun isNpc(dummy: Dummy<*>): Boolean {
        return entityMap.contains(dummy)
    }

    fun contains(id: String): Boolean{
        return map.containsKey(id)
    }

    fun contains(entity: Entity): Boolean{
        if(entity.persistentDataContainer.has(NamespacedKey(IngeniaMC.plugin, "npc"), PersistentDataType.STRING)){
            return map.containsKey(entity.persistentDataContainer.get(NamespacedKey(IngeniaMC.plugin, "npc"), PersistentDataType.STRING))
        }
        return false
    }

    fun clear() {
        map.clear()
        entityMap.clear()
    }

    fun deleteAll() {
        allEntities.forEach {
            it.isRemoved = true
            it.data.destroy()
        }
        allEntities.clear()
    }


}