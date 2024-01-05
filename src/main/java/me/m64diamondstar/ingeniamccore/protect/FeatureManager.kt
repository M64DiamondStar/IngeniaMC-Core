package me.m64diamondstar.ingeniamccore.protect

import me.m64diamondstar.ingeniamccore.data.DataConfiguration

class FeatureManager: DataConfiguration("", "features") {

    fun enableFeature(type: FeatureType){
        this.getConfig().set("Features.$type.Enabled", true)
        this.save()
    }

    fun disableFeature(type: FeatureType){
        this.getConfig().set("Features.$type.Enabled", false)
        this.save()
    }

    fun isFeatureEnabled(type: FeatureType): Boolean{
        if(!this.getConfig().contains("Features.$type.Enabled")) return true
        return this.getConfig().getBoolean("Features.$type.Enabled")
    }

}