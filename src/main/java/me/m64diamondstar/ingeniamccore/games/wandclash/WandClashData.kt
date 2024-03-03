package me.m64diamondstar.ingeniamccore.games.wandclash

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.games.wandclash.util.WandClashTeam
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import org.bukkit.Location

class WandClashData(name: String): DataConfiguration("games/wandclash/arenas", name) {

    var displayName: String
        get() = this.getConfig().getString("Display-Name")!!
        set(value) {
            this.getConfig().set("Display-Name", value)
            this.save()
        }

    var maxPlayers: Int
        get() = this.getConfig().getInt("Max-Players")
        set(value) {
            this.getConfig().set("Max-Players", value)
            this.save()
        }

    var minPlayers: Int
        get() = this.getConfig().getInt("Min-Players")
        set(value) {
            this.getConfig().set("Min-Players", value)
            this.save()
        }

    var lobbySpawnLocation: Location?
        get() = this.getConfig().getLocation("Lobby.Spawn")
        set(value) {
            this.getConfig().set("Lobby.Spawn", value)
            this.save()
        }


    var leaveLocation: Location?
        get() = this.getConfig().getLocation("Lobby.Leave")
        set(value) {
            this.getConfig().set("Lobby.Leave", value)
            this.save()
        }

    var playersCountdown: Int
        get() = this.getConfig().getInt("Lobby.Countdown.Players")
        set(value) {
            this.getConfig().set("Lobby.Countdown.Players", value)
            this.save()
        }

    var gameModeCountdown: Int
        get() = this.getConfig().getInt("Lobby.Countdown.Game-Mode")
        set(value) {
            this.getConfig().set("Lobby.Countdown.Game-Mode", value)
            this.save()
        }

    var teamCountdown: Int
        get() = this.getConfig().getInt("Lobby.Countdown.Team")
        set(value) {
            this.getConfig().set("Lobby.Countdown.Team", value)
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
         * Removes all locations from the list of spawn points for the FFA game mode.
         */
        fun removeAllFFASpawns(){
            getConfig().set("GameModeSettings.FFA.Spawn-Points", null)
            save()
        }

        /**
         * The amount of kills needed to win in the FFA game mode.
         */
        var necessaryFFAKills: Int
            get() = if(getConfig().getInt("GameModeSettings.FFA.Neccessary-Kills") == 0) 20 else getConfig().getInt("GameModeSettings.FFA.Neccessary-Kills")
            set(value) {
                getConfig().set("GameModeSettings.FFA.Neccessary-Kills", value)
                save()
            }

        /**
         * Set the spawn point for the Team Deathmatch (TDM) game mode.
         * @param location the location to be added as a spawn point
         * @param team the team to set the spawn point for
         */
        fun setTDMSpawn(location: Location, team: WandClashTeam){
            getConfig().set("GameModeSettings.TDM.Spawn-Point.${team.name}", location)
            save()
        }

        /**
         * Retrieves the spawn location of a team.
         * @param team the team to get the spawn location from
         */
        fun getTDMSpawn(team: WandClashTeam): Location?{
            return getConfig().getLocation("GameModeSettings.TDM.Spawn-Point.${team.name}")
        }

        /**
         * The amount of kills needed to win in the FFA game mode.
         */
        var necessaryTDMKills: Int
            get() = if(getConfig().getInt("GameModeSettings.TDM.Neccessary-Kills") == 0) 20 else getConfig().getInt("GameModeSettings.TDM.Neccessary-Kills")
            set(value) {
                getConfig().set("GameModeSettings.TDM.Neccessary-Kills", value)
                save()
            }

        /**
         * Set the spawn point for the Control Points (CP) game mode.
         * @param location the location to be added as a spawn point
         * @param team the team to set the spawn point for
         */
        fun setCPSpawn(location: Location, team: WandClashTeam){
            getConfig().set("GameModeSettings.CP.Spawn-Point.${team.name}", location)
            save()
        }

        /**
         * Retrieves the spawn location of a team.
         * @param team the team to get the spawn location from
         */
        fun getCPSpawn(team: WandClashTeam): Location?{
            return getConfig().getLocation("GameModeSettings.CP.Spawn-Point.${team.name}")
        }

        /**
         * The amount of CP points needed to win in the Control Points (CP) game mode.
         */
        var necessaryCPPoints: Int
            get() = if(getConfig().getInt("GameModeSettings.CP.Neccessary-Points") == 0) 20 else getConfig().getInt("GameModeSettings.CP.Neccessary-Points")
            set(value) {
                getConfig().set("GameModeSettings.CP.Neccessary-Points", value)
                save()
            }

        /**
         * Set the spawn point for the Mana Reactor (MR) game mode.
         * @param location the location to be added as a spawn point
         * @param team the team to set the spawn point for
         */
        fun setMRSpawn(location: Location, team: WandClashTeam){
            getConfig().set("GameModeSettings.MR.Spawn-Point.${team.name}", location)
            save()
        }

        /**
         * Retrieves the spawn location of a team.
         * @param team the team to get the spawn location from
         */
        fun getMRSpawn(team: WandClashTeam): Location?{
            return getConfig().getLocation("GameModeSettings.MR.Spawn-Point.${team.name}")
        }

        /**
         * The amount of damage needed to be done to a mana reactor in order to destroy it and win the battle.
         */
        var manaReactorDurability: Int
            get() = if(getConfig().getInt("GameModeSettings.MR.Mana-Reactor-Durability") == 0) 20 else getConfig().getInt("GameModeSettings.MR.Mana-Reactor-Durability")
            set(value) {
                getConfig().set("GameModeSettings.MR.Mana-Reactor-Durability", value)
                save()
            }

    }



}