package me.m64diamondstar.ingeniamccore.attractions.utils

abstract class CustomAttraction(name: String, category: String): Attraction(name, category) {

    abstract fun run()

}