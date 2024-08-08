package me.m64diamondstar.ingeniamccore.protect.moderation

import com.opencsv.CSVReaderBuilder
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ModerationRegistry {

    private val blockedWordsList = ArrayList<String>()

    fun getBlockedWords(): ArrayList<String>{
        return blockedWordsList
    }

    fun registerBlockedWords() {
        try {
            val url = URL("https://gamersafer.com/wp-content/uploads/2022/08/Chat-Word-Filter-Blocklist.csv")
            val connection = url.openConnection() as HttpURLConnection

            // Set timeouts
            connection.connectTimeout = 5_000  // 10 seconds for connection timeout
            connection.readTimeout = 5_000  // 10 seconds for read timeout

            val inputStream = connection.inputStream

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