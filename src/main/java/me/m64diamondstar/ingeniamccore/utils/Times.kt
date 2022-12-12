package me.m64diamondstar.ingeniamccore.utils

import java.text.SimpleDateFormat
import java.util.*

object Times {

    fun formatTime(time: Long): String {
        var sdf = SimpleDateFormat("mm:ss:SSS")
        if (time > 1000000) {
            sdf = SimpleDateFormat("HH:mm:ss:SSS")
        }
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(time)!!
    }

}