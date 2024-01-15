package me.m64diamondstar.ingeniamccore.attractions.custom.utils

object CountdownRegistry {

    private val countingDown = ArrayList<Pair<String, String>>()

    fun setCountingDown(name: String, category: String, boolean: Boolean){
        if(boolean)
            countingDown.add(Pair(name, category))
        else
            countingDown.remove(Pair(name, category))
    }

    fun isCountingDown(name: String, category: String): Boolean{
        return countingDown.contains(Pair(name, category))
    }

}