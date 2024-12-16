package me.m64diamondstar.ingeniamccore.rides.actions.utils

fun interface RideParameterTypeConverter {
    fun getAsType(value: String): Any
}