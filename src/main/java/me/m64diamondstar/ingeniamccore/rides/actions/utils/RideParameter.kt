package me.m64diamondstar.ingeniamccore.rides.actions.utils

data class RideParameter(

    /**
     * The name of the parameter
     */
    val name: String,

    /**
     * The default value if the parameter hasn't been set yet
     */
    val defaultValue: Any,

    /**
     * Sets the parameter to the correct type. All inputs are always a String, but not every parameter should be a string.
     * This method will set the parameter value to the correct type/class.
     */
    val converter: RideParameterTypeConverter,

    /**
     * A check to see if the parameter is valid.
     */
    val validator: RideParameterValidator
)
