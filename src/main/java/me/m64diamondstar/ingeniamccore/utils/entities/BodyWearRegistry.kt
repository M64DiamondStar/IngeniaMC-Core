package me.m64diamondstar.ingeniamccore.utils.entities

import java.util.UUID

object BodyWearRegistry {

    private val map = HashMap<UUID, BodyWearEntity>()

    fun add(uuid: UUID, bodyWearEntity: BodyWearEntity){
        map[uuid] = bodyWearEntity
    }

    fun get(uuid: UUID): BodyWearEntity? {
        return map[uuid]
    }

    fun remove(uuid: UUID){
        map.remove(uuid)
    }

    fun contains(uuid: UUID): Boolean {
        return map.containsKey(uuid)
    }

}