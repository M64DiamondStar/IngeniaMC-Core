package me.m64diamondstar.ingeniamccore.data

import me.m64diamondstar.ingeniamccore.Main
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.lang.Exception

open class Configuration (path: String, name: String,resource: Boolean) {

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
     * Create file if it doesn't exist
     */
    private fun create(){

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