package me.m64diamondstar.ingeniamccore.data

import me.m64diamondstar.ingeniamccore.Main
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.lang.Exception

abstract class Configuration (path: String, name: String, resource: Boolean, instantCreation: Boolean) {

    private lateinit var config: FileConfiguration
    private var path: File
    private lateinit var file: File
    private var name: String

    private val resource: Boolean

    /**
     * Constructor for making/changing a Config File
     */
    init {

        this.path = File(Main.plugin.dataFolder, path)
        this.name = name
        this.resource = resource

        this.path.mkdirs()

        if(instantCreation)
            create()

    }

    /**
     * Get FileConfiguration
     */
    fun getConfig(): FileConfiguration {
        return config
    }

    /**
     * Saves the file
     */
    private fun save() {
        try{
            config.save(file)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    /**
     * Reloads config
     * @return FileConfiguration
     */
    fun reload(): FileConfiguration {
        save()
        return config
    }

    /**
     * Check if the configuration file exists
     * @return Boolean
     */
    fun exists(): Boolean{
        file = File(path, "$name.yml")
        return file.exists()
    }

    /**
     * Create file if it doesn't exist
     */
    fun create(){

        file = File(path, "$name.yml")

        if(!file.exists()) {

            file.createNewFile()
            config = YamlConfiguration()

            save()
        }else{
            config = YamlConfiguration.loadConfiguration(file)
        }

    }



}