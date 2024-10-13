package me.m64diamondstar.ingeniamccore.entity.body

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers
import com.google.common.collect.Maps
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import com.google.common.collect.SetMultimap
import com.google.common.collect.Sets
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object PassengerPacketManager {

    private val passengers: SetMultimap<UUID, Int> = Multimaps.newSetMultimap(
        Maps.newConcurrentMap()
    ) { Sets.newConcurrentHashSet() }
    private val executorService: ExecutorService = Executors.newFixedThreadPool(2) { r ->
        Thread(r, "IngeniaMC-PassengerEntities-PacketManager")
    }

    fun sendPassengersPacket(player: Player, passengerEntity: PassengerEntity) {
        val entityId = passengerEntity.getId()
        val ownerId = passengerEntity.getOwner().entityId
        executorService.submit {
            val passengerSet = HashSet(this.passengers[passengerEntity.getOwner().uniqueId])
            if(!passengerSet.contains(entityId)) {
                passengerSet.add(entityId)
                this.passengers.putAll(passengerEntity.getOwner().uniqueId, passengerSet)
            }

            val passengersArray = passengerSet.toIntArray()
            val packet = WrapperPlayServerSetPassengers(ownerId, passengersArray)
            PacketEvents.getAPI().playerManager.sendPacket(player, packet)
        }
    }

    fun removePassenger(player: Player, passenger: Int) {
        executorService.submit { passengers.remove(player.uniqueId, passenger) }
    }

}