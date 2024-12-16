package me.m64diamondstar.ingeniamccore.rides

import me.m64diamondstar.ingeniamccore.data.DataConfiguration

class RideConfiguration(ride: Ride): DataConfiguration("ride/${ride.id}", "config")