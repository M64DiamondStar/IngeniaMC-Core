package me.m64diamondstar.ingeniamccore.general.areas

object AreaAudioManager {
    private var startTime: Long = System.currentTimeMillis()

    /**
     * Loads all track lengths and saves the startup time of the server.
     * This is necessary because the server has to load all tracks before
     * so that the players can listen to synchronized music.
     */
    fun initialize(){
        startTime = System.currentTimeMillis()
    }

    fun getStartTime(): Long{
        return startTime
    }

}