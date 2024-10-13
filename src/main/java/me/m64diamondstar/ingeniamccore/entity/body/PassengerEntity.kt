package me.m64diamondstar.ingeniamccore.entity.body

import me.m64diamondstar.ingeniamccore.entity.CustomEntity
import org.bukkit.entity.Player

abstract class PassengerEntity(owner: Player): CustomEntity {

    /**
     * Adds the entity to the owner through a packet sent to the player.
     * @param player the player to send the packet to
     */
    abstract fun sendPassengerPacket(player: Player)

    /**
     * Adds the entity to the owner through a packet sent to the player.
     * Only viewers of the player/entity will receive the packet.
     */
    abstract fun sendPassengerPacketToViewers()

    /**
     * @return the owner of the passenger entity
     */
    abstract fun getOwner(): Player

}