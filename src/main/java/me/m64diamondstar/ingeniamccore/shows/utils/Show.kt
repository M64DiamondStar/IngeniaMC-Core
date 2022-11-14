package me.m64diamondstar.ingeniamccore.shows.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.data.Configuration
import me.m64diamondstar.ingeniamccore.shows.type.Animatronic
import me.m64diamondstar.ingeniamccore.shows.type.AnimatronicGroup
import me.m64diamondstar.ingeniamccore.shows.type.Particle
import me.m64diamondstar.ingeniamccore.shows.type.ParticleEmitter
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.ArrayList

/**
 * Play custom shows in the park, can be used in coasters and other rides for fun decoration.
 */
class Show(private val category: String, private val name: String): Configuration("shows/$category", name, false, true) {

    /**
     * Adds the standard comments to the configuration file of this show.
     */
    fun createShow(){
        val header = ArrayList<String>()
        header.add("-----------------------------------------")
        header.add("This is the file for the show: ${getName()}.")
        header.add("Please try to use in-game commands as much as possible, ")
        header.add("but if it's really necessary you can edit this file.")
        header.add(" ")
        header.add("Reminder, all the times are in ticks! 20 ticks = 1 second.")
        header.add("-----------------------------------------")

        this.getConfig().options().setHeader(header)

        this.reloadConfig()
    }

    private fun getIDAmount(): Int {
        var i = 1
        while (getConfig().getConfigurationSection("$i") != null) {
            i++
        }
        return i - 1
    }

    /**
     * Plays the full show.
     */
    fun play(){
        object: BukkitRunnable(){
            var count = 0L
            var tasksDone = 0
            override fun run() {

                if(tasksDone >= getIDAmount()){
                    this.cancel()
                    return
                }

                var i = 1
                while (getConfig().getConfigurationSection("$i") != null) {
                    if (getConfig().getConfigurationSection("$i")!!.getLong("Delay") == count) {
                        if(getType(i)?.isSync() == true) {
                            object : BukkitRunnable() {
                                override fun run() {
                                    getType(i)?.execute()
                                }
                            }.runTask(IngeniaMC.plugin)
                        }else {
                            getType(i)?.execute()
                        }
                        tasksDone++
                    }
                    i++
                }

                count++
            }
        }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)
    }

    /**
     * Starts the show from a specific moment.
     * @param id the ID from where to start the show.
     */
    fun playFrom(id: Int){
        object: BukkitRunnable(){
            var count = 0L
            var tasksDone = 0
            override fun run() {

                if(tasksDone >= getIDAmount()){
                    this.cancel()
                    return
                }

                var i = id
                while (getConfig().getConfigurationSection("$i") != null) {
                    if (getConfig().getConfigurationSection("$i")!!.getLong("Delay") == count) {
                        if(getType(i)?.isSync() == true) {
                            object : BukkitRunnable() {
                                override fun run() {
                                    getType(i)?.execute()
                                }
                            }.runTask(IngeniaMC.plugin)
                        }else {
                            getType(i)?.execute()
                        }
                        tasksDone++
                    }
                    i++
                }

                count++
            }
        }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)
    }

    /**
     * Only plays the effect with the given id.
     * @param id the ID of the effect that should be played.
     */
    fun playOnly(id: Int): Boolean{
        if(getConfig().getConfigurationSection("$id") == null) return false
        getType(id)?.execute()
        return true
    }

    fun getCategory(): String{
        return category
    }

    fun getName(): String{
        return name
    }

    fun getType(id: Int): EffectType? {
        if(getConfig().getString("$id.Type").equals("Animatronic", ignoreCase = true))
            return Animatronic(this, id)
        if(getConfig().getString("$id.Type").equals("Animatronic-Group", ignoreCase = true))
            return AnimatronicGroup(this, id)
        if(getConfig().getString("$id.Type").equals("Particle", ignoreCase = true))
            return Particle(this, id)
        if(getConfig().getString("$id.Type").equals("Particle-Emitter", ignoreCase = true))
            return ParticleEmitter(this, id)
        return null
    }

}