package me.m64diamondstar.ingeniamccore.rides.actions.utils

fun interface RideParameterValidator {
    fun isValid(value: String): Boolean
}