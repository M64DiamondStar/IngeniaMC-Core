package me.m64diamondstar.ingeniamccore.data

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class DataConfiguration(private val path: String, name: String) {

    private var config: YamlConfiguration? = null
    private val file: File by lazy { File(IngeniaMC.plugin.dataFolder, "$path/${name.replace(".yml", "")}.yml") }

    init {
        file.parentFile.mkdirs()
        createConfig()
    }

    fun deleteFile() {
        file.delete()
    }

    fun getConfig(): FileConfiguration {
        return config ?: throw IllegalStateException("Configuration is not initialized.")
    }

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
        LoadedFiles.loadFile(file.path, config!!)
    }

    fun save() {
        createConfig()
        try {
            config?.save(file)
            LoadedFiles.loadFile(file.path, config!!)
            config = LoadedFiles.getFile(file.path)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createConfig() {
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        if(LoadedFiles.isFileLoaded(file.path))
            config = LoadedFiles.getFile(file.path)
        else
            reload()
    }
}
