package me.m64diamondstar.ingeniamccore.utils

object EnumUtilities {

    inline fun <reified T : Enum<T>> enumContains(value: String): Boolean {
        return enumValues<T>().any { it.name.equals(value, true) }
    }

}