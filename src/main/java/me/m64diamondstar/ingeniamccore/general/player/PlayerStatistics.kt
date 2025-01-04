package me.m64diamondstar.ingeniamccore.general.player

data class StatisticKey<T>(
    val name: String,
    val type: StatisticType<T>
)

sealed class StatisticType<T> {
    object IntType : StatisticType<Int>()
    object StringType : StatisticType<String>()
    // Add other types as needed
}

