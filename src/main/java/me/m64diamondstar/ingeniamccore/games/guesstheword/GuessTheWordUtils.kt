package me.m64diamondstar.ingeniamccore.games.guesstheword

import me.m64diamondstar.ingeniamccore.IngeniaMC
import kotlin.random.Random

object GuessTheWordUtils {

    fun addWord(word: String){
        val stringList = IngeniaMC.plugin.config.getStringList("Guess-The-Word")
        stringList.add(word.replace("-", " ").replace("_", " "))
        IngeniaMC.plugin.config.set("GuessTheWord", stringList)
        IngeniaMC.plugin.reloadConfig()
    }

    fun removeWord(word: String): Boolean{
        val stringList = IngeniaMC.plugin.config.getStringList("Guess-The-Word")
        val success = stringList.remove(word.replace("-", " ").replace("_", " "))
        IngeniaMC.plugin.config.set("GuessTheWord", stringList)
        IngeniaMC.plugin.reloadConfig()
        return success
    }

    /**
     * Gets a random word from the list in the config.yml
     * @return the random word
     */
    fun getRandomWord(): String{
        val stringList = IngeniaMC.plugin.config.getStringList("Guess-The-Word")
        return stringList[Random.nextInt(0, stringList.size - 1)]
    }

    /**
     * Scramble a word
     * @return the scrambled word
     */
    fun scramble(word: String): String{
        val stringBuilder = StringBuilder()

        for(letter in 1.. word.chunked(1).size){
            if(word.chunked(1)[letter - 1] == "-" || word.chunked(1)[letter - 1] == " ")
                stringBuilder.append(" ")
            else
                stringBuilder.append("_")
        }

        return stringBuilder.toString()
    }

    /**
     * Unscrambles one unknown letter from a scrambled word
     * @return the new word with the unscrambled letter
     */
    fun unscramble(scrambledWord: String, unscrambledWord: String): String {
        val scrambledWordFormatted = scrambledWord.chunked(1)
        val unscrambledWordFormatted = unscrambledWord.chunked(1)
        val length = scrambledWordFormatted.size
        var randomIndex = Random.nextInt(length - 1)
        val scrambledLetters = ArrayList<Int>()

        for(letterIndex in 0 until length)
            if(scrambledWordFormatted[letterIndex] == "_") scrambledLetters.add(letterIndex)

        if(scrambledLetters.size <= 1)
            return unscrambledWord

        while (!scrambledLetters.contains(randomIndex)){
            randomIndex = Random.nextInt(length - 1)
        }

        val stringBuilder = StringBuilder()

        for(index in 0 until length){
            if(index != randomIndex)
                stringBuilder.append(scrambledWordFormatted[index])
            else
                stringBuilder.append(unscrambledWordFormatted[index])
        }

        return stringBuilder.toString()
    }

}