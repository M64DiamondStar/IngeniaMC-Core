package me.m64diamondstar.ingeniamccore.protect.moderation

import com.opencsv.CSVReaderBuilder
import java.io.InputStreamReader
import java.net.URL

object ModerationRegistry {

    private val blockedWordsList = ArrayList<String>()

    fun getBlockedWords(): ArrayList<String>{
        return blockedWordsList
    }

    /**
     * Register all the blocked words from GamerSafer's Chat Word Filter Blocklist.
     * https://gamersafer.com/wp-content/uploads/2022/08/Chat-Word-Filter-Blocklist.csv
     * ONLY USE THIS ON STARTUP!
     */
    fun registerBlockedWords(){
        try {
            val url = URL("https://gamersafer.com/wp-content/uploads/2022/08/Chat-Word-Filter-Blocklist.csv")
            val inputStream = url.openStream()

            InputStreamReader(inputStream).use { inputStreamReader ->
                val csvReader = CSVReaderBuilder(inputStreamReader).build()

                // Read all rows from the CSV file
                val rows = csvReader.readAll()

                // Assuming the first column contains the blocked words
                for (row in rows) {
                    if (row.isNotEmpty()) {
                        blockedWordsList.add(row[0])
                    }
                }
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., IOException)
            e.printStackTrace()
        }
    }

}