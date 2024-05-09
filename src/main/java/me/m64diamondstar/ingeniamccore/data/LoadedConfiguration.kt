package me.m64diamondstar.ingeniamccore.data

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths

abstract class LoadedConfiguration (path: String, name: String, private val resource: Boolean, instantCreation: Boolean) {

    private lateinit var config: FileConfiguration
    private var path: File = File(IngeniaMC.plugin.dataFolder, path)
    private lateinit var file: File
    private var name: String = name.replace(".yml", "")

    /**
     * Constructor for making/changing a Config File
     */
    init {

        this.path.mkdirs()

        if(instantCreation)
            createConfig()

    }

    /**
     * Rename the config file
     */
    fun rename(name: String){
        val source = Paths.get("$file")
        Files.move(source, source.resolveSibling("$name.yml"))
    }

    /**
     * Get FileConfiguration
     */
    fun getConfig(): FileConfiguration {
        return config
    }

    /**
     * Delete the File
     */
    fun deleteFile(){
        file.delete()
    }

    /**
     * Saves the file
     */
    private fun saveConfig() {
        if(!file.exists())
            createConfig()
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
    fun reloadConfig(): FileConfiguration {
        saveConfig()
        return config
    }

    /**
     * Check if the configuration file exists
     * @return Boolean
     */
    fun existsConfig(): Boolean{
        file = File(path, "$name.yml")
        return file.exists()
    }

    /**
     * Create file if it doesn't exist
     */
    private fun createConfig(){

        file = File(path, "$name.yml")

        if(!file.exists()) {

            file.createNewFile()
            config = YamlConfiguration()

            saveConfig()
        }else{
            config = YamlConfiguration.loadConfiguration(file)
        }

    }



}