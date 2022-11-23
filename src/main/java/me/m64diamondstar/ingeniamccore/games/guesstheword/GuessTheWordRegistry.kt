package me.m64diamondstar.ingeniamccore.games.guesstheword

import org.bukkit.entity.Player

object GuessTheWordRegistry {

    private val guessTheWord = ArrayList<GuessTheWord>()

    fun add(word: GuessTheWord) = guessTheWord.add(word)
    fun remove(word: GuessTheWord) = guessTheWord.remove(word)
    fun getRunningWords(): List<String>{
        val list = ArrayList<String>()
        guessTheWord.forEach { list.add(it.getWord()) }
        return list
    }
    fun containsWord(word: String): Boolean {
        for(words in guessTheWord){
            if(words.getWord().equals(word, ignoreCase = true)) return true
        }
        return false
    }
    fun addPlayer(player: Player, word: String){
        for(words in guessTheWord){
            if(words.getWord().equals(word, ignoreCase = true)) words.addPlayer(player)
        }
    }
    fun containsPlayer(player: Player): Boolean{
        for(words in guessTheWord){
            if(words.getPlayers().contains(player.uniqueId)) return true
        }
        return false
    }
    fun isRunning(): Boolean = guessTheWord.isNotEmpty()




}