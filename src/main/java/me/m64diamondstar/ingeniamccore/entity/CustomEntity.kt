package me.m64diamondstar.ingeniamccore.entity

import org.bukkit.Location
import org.bukkit.entity.Player

interface CustomEntity {

    /**
     * Spawns the entity.
     * @param location the location where the entity should be spawned
     */
    fun spawn(location: Location)

    /**
     * Fully removes the entity.
     */
    fun remove()

    /**
     * Changes the visibility of the entity for the player.
     * @param player the player
     * @param visible the visibility
     */
    fun setVisible(player: Player, visible: Boolean)

    /**
     * Refreshes all entity data.
     */
    fun refresh()

    /**
     * Gets the id of the entity.
     * @return the id
     */
    fun getId(): Int
}