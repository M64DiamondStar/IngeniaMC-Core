package me.m64diamondstar.ingeniamccore.utils

enum class PlayerSelectors {

    ALL{
        override fun next(): PlayerSelectors = STAFF
    },
    STAFF{
        override fun next(): PlayerSelectors = NONE
    },
    NONE{
        override fun next(): PlayerSelectors = ALL
    };

    abstract fun next(): PlayerSelectors

}