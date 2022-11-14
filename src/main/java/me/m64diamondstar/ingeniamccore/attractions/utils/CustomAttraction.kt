package me.m64diamondstar.ingeniamccore.attractions.utils

import org.bukkit.Location
import org.bukkit.entity.ArmorStand

interface CustomAttraction {

    fun spawn()

    fun despawn()

    fun getSeats(): List<ArmorStand>

    fun hasPassengers(): Boolean

    fun registerSeats()

    fun getSpawnLocation(): Location?

    fun setSpawnLocation(location: Location)

    fun execute()

}