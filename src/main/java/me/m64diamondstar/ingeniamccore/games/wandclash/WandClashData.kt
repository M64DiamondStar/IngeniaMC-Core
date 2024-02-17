package me.m64diamondstar.ingeniamccore.games.wandclash

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Location

class WandClashData(name: String): DataConfiguration("games/wandclash", name) {

    var displayName: String
        get() = this.getConfig().getString("Display-Name")!!
        set(value) {
            this.getConfig().set("Display-Name", value)
            this.save()
        }

    inner class GameModeSettings {

        /**
         * Add a new spawn point to the FFA game mode.
         * @param location the location to be added as a spawn point
         */
        fun addFFASpawn(location: Location){
            val list = getConfig().getStringList("GameModeSettings.FFA.Spawn-Points")
            list.add(LocationUtils.getStringFromLocation(location))
            getConfig().set("GameModeSettings.FFA.Spawn-Points", list)
            save()
        }

        /**
         * Removes the specified location from the list of spawn points for the FFA game mode.
         *
         * @param location the location to be removed from the spawn points list
         */
        fun removeFFASpawn(location: Location){
            val list = getConfig().getStringList("GameModeSettings.FFA.Spawn-Points")
            list.remove(LocationUtils.getStringFromLocation(location))
            getConfig().set("GameModeSettings.FFA.Spawn-Points", list)
            save()
        }


        /**
         * Add a new spawn point to the Team Deathmatch (TDM) game mode.
         * @param location the location to be added as a spawn point
         */
        fun addTDMSpawn(location: Location){
            val list = getConfig().getStringList("GameModeSettings.TDM.Spawn-Points")
            list.add(LocationUtils.getStringFromLocation(location))
            getConfig().set("GameModeSettings.TDM.Spawn-Points", list)
            save()
        }

        /**
         * Remove a TDM spawn point from the configuration
         * @param location the location to be removed from the TDM spawn points
         */
        fun removeTDMSpawn(location: Location){
            val list = getConfig().getStringList("GameModeSettings.TDM.Spawn-Points")
            list.remove(LocationUtils.getStringFromLocation(location))
            getConfig().set("GameModeSettings.TDM.Spawn-Points", list)
            save()
        }

    }



}