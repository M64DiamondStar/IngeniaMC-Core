package me.m64diamondstar.ingeniamccore.rides

class RideVariable(val name: String, var value: Any)

enum class RideVariableType {
    STRING,
    INT,
    FLOAT,
    BOOLEAN
}