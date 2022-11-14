package me.m64diamondstar.ingeniamccore.utils

interface IngeniaSubcommand {
    fun execute()
    fun getTabCompleters(): ArrayList<String>
}