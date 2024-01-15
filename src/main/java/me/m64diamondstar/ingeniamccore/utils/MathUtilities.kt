package me.m64diamondstar.ingeniamccore.utils

import org.joml.Quaternionf
import kotlin.math.cos
import kotlin.math.sin

object MathUtilities {

    fun eulerToQuaternion(yaw: Float, pitch: Float, roll: Float): Quaternionf {
        val yawRad = Math.toRadians(yaw.toDouble()).toFloat()
        val pitchRad = Math.toRadians(pitch.toDouble()).toFloat()
        val rollRad = Math.toRadians(roll.toDouble()).toFloat()

        val qx = sin(rollRad / 2.0) * cos(pitchRad / 2.0) * cos(yawRad / 2.0) - cos(rollRad / 2.0) * sin(pitchRad / 2.0) * sin(yawRad / 2.0)
        val qy = cos(rollRad / 2.0) * sin(pitchRad / 2.0) * cos(yawRad / 2.0) + sin(rollRad / 2.0) * cos(pitchRad / 2.0) * sin(yawRad / 2.0)
        val qz = cos(rollRad / 2.0) * cos(pitchRad / 2.0) * sin(yawRad / 2.0) - sin(rollRad / 2.0) * sin(pitchRad / 2.0) * cos(yawRad / 2.0)
        val qw = cos(rollRad / 2.0) * cos(pitchRad / 2.0) * cos(yawRad / 2.0) + sin(rollRad / 2.0) * sin(pitchRad / 2.0) * sin(yawRad / 2.0)

        return Quaternionf(qx.toFloat(), qy.toFloat(), qz.toFloat(), qw.toFloat())
    }

}