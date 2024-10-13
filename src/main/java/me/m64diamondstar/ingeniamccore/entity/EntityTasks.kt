package me.m64diamondstar.ingeniamccore.entity

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.entity.body.BodyWearEntity
import me.m64diamondstar.ingeniamccore.entity.body.NametagEntity
import me.m64diamondstar.ingeniamccore.entity.body.listeners.BodyWearListener
import me.m64diamondstar.ingeniamccore.entity.body.listeners.NametagListener

object EntityTasks {

    /**
     * Creates new instance of all object Manager classes to run their init methods.
     */
    fun startUp(){
        NametagEntity.NametagManager
        BodyWearEntity.BodyWearManager
    }

    fun loadListeners(){
        IngeniaMC.plugin.server.pluginManager.registerEvents(NametagListener(), IngeniaMC.plugin)
        IngeniaMC.plugin.server.pluginManager.registerEvents(BodyWearListener(), IngeniaMC.plugin)
    }

}