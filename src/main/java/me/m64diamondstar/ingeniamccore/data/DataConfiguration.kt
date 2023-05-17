package me.m64diamondstar.ingeniamccore.data

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class DataConfiguration (path: String, name: String) {

    private var config: FileConfiguration?
    private var path: File
    private var file: File?
    private var name: String

    /**
     * Constructor for making/changing a Config File
     */
    init {

        this.path = File(IngeniaMC.plugin.dataFolder, path)
        this.name = name.replace(".yml", "")
        this.file = null
        this.config = null

        this.path.mkdirs()

        createConfig()

    }

    /**
     * Returns the configuration
     */
    fun getConfig(): FileConfiguration{
        return config!!
    }

    /**
     * Reloads the file
     * Used when the file is still null
     */
    private fun reloadFile(): File{
        file = File(path, "$name.yml")
        return file as File
    }

    /**
     * Loads the configuration from the file
     */
    private fun reloadConfig(): FileConfiguration{
        if(file == null)
            reloadFile()
        config = YamlConfiguration.loadConfiguration(file!!)
        return config!!
    }

    /**
     * Reloads both the file and the configuration
     */
    fun reload(){
        reloadFile()
        reloadConfig()
    }

    /**
     * Saves the config in the file
     */
    fun save(){
        if(config == null || file == null)
            createConfig()

        try{
            config!!.save(file!!)
        } catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    /**
     * Create file if it doesn't exist
     */
    private fun createConfig(){

        if(file == null)
            reloadFile()

        // File already created if it was null before
        if(!file!!.exists()) {

            file!!.parentFile.mkdirs()

            try {
                file!!.createNewFile() // Create file if it didn't exist already
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        // File is loaded and created

        // Create config if it doesn't exist
        if(config == null)
            reloadConfig()

    }

}