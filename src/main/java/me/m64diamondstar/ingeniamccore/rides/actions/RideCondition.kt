package me.m64diamondstar.ingeniamccore.rides.actions

import me.m64diamondstar.ingeniamccore.rides.Ride

interface RideCondition {

    fun getId(): String
    fun check(ride: Ride, vararg arguments: String): Boolean

}