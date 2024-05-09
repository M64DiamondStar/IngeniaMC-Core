package me.m64diamondstar.ingeniamccore.utils

import com.google.gson.Gson
import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.Bukkit
import org.json.simple.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

object EmojiUtils {

    val emojiMap = HashMap<String, String>()

    /**
     * Load all emojis from the json/emoji.json file in resources
     */
    fun loadEmojis(){
        val emojis = IngeniaMC.plugin.getResource("json/emoji.json")
        if(emojis == null){
            IngeniaMC.plugin.logger.warning("Emoji file not found, meaning emoji translations cannot be loaded!")
            return
        }
        val response = BufferedReader(InputStreamReader(emojis, Charsets.UTF_8)).use { it.readText() }
        val json = Gson().fromJson(response, JSONObject::class.java)
        json.keys.forEach { key ->
            val value = json[key].toString()
            emojiMap[key.toString()] = value
        }
        IngeniaMC.plugin.logger.info("Emojis loaded ✓")
    }

    /**
     * Translates emojis in the message.
     * For example :grinning:
     * The emoji translations will be loaded from the json/emoji.json file in resources
     * When it fails to load, it will stop the translation
     * @param message the message
     * @return the message with the emojis
     */
    fun addEmoji(message: String): String{
        var count = 0
        for (char in message) {
            if (char == ':') {
                count++
            }
        }
        if(count < 2) return message

        if(emojiMap.isEmpty()) {
            val emojis = IngeniaMC.plugin.getResource("json/emoji.json")
            if(emojis == null){
                IngeniaMC.plugin.logger.warning("Emoji file not found, meaning emoji translations cannot be loaded!")
                return message
            }
            val response = BufferedReader(InputStreamReader(emojis, Charsets.UTF_8)).use { it.readText() }
            val json = Gson().fromJson(response, JSONObject::class.java)
            json.keys.forEach { key ->
                val value = json[key].toString()
                emojiMap[key.toString()] = value
            }
        }

        var result = message
        emojiMap.forEach { (key, value) ->
            result = result.replace(key, value)
        }
        return result
    }

    fun getAllEmojiKeys(): List<String>{
        return emojiMap.keys.toList()
    }

}