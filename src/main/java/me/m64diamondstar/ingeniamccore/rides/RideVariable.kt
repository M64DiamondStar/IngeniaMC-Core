package me.m64diamondstar.ingeniamccore.rides

class RideVariable(var value: Any, var type: RideVariableType = RideVariableType.STRING)

enum class RideVariableType {
    STRING,
    INT,
    FLOAT,
    BOOLEAN
}