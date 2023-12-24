package me.m64diamondstar.ingeniamccore.utils.leaderboard

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.Times
import org.bukkit.entity.Player
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException

class Leaderboard(
    private val scores: Map<String, Long>, private val backgroundColor: Color, private val outlineColor: Color,
    private val titleColor: Color, private val positionColor: Color, private val nameColor: Color,
    private val lineColor: Color, private val isTime: Boolean) {

    private lateinit var bufferedImage: BufferedImage
    private lateinit var graphics2D: Graphics2D

    private fun writeCenteredText(text: String, y: Int) {
        graphics2D.drawString(text, (128 - graphics2D.fontMetrics.stringWidth(text)) / 2, y)
    }


    private fun writeName(place: Int, rawScore: Long, name: String?, isOwn: Boolean) {
        var score = rawScore.toString()
        if(isTime){
            score = Times.formatTime(rawScore)
        }

        if (name == null) return
        var y = place * 23 + 5

        if (isOwn) {
            y = 105
            //POSITION + COUNT
            graphics2D.color = positionColor
            writeCenteredText("#$place - $score", y)

            //NAME
            writeCenteredText(name, y + 8)
        }
        else {
            //POSITION + COUNT
            graphics2D.color = positionColor
            writeCenteredText("#$place - $score", y)

            //NAME
            graphics2D.color = nameColor
            writeCenteredText(name, y + 8)

            //LINE
            graphics2D.color = lineColor
            graphics2D.stroke = BasicStroke(1F)
            graphics2D.drawLine(15, y + 12, 113, y + 12)
        }
    }

    private fun sortedScores(): List<Map.Entry<String, Long>> {
        val linkedScores = LinkedHashMap<String, Long>()
        linkedScores.putAll(scores)

        val resultScores = linkedScores.entries.sortedBy { it.value }.associate { it.toPair() }

        return if(isTime)
            resultScores.asIterable().toList()
        else
            resultScores.asIterable().reversed()
    }

    fun getImage(player: Player, title: String): BufferedImage{

        bufferedImage = BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB)
        graphics2D = bufferedImage.createGraphics()

        graphics2D.color = backgroundColor
        graphics2D.fillRect(0, 0, 128, 128)

        graphics2D.color = outlineColor
        graphics2D.stroke = BasicStroke(4F)
        graphics2D.drawRect(0, 0, 128, 128)

        graphics2D.color = titleColor

        try {
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, File(IngeniaMC.plugin.dataFolder, "fonts/Minecraft.otf")).deriveFont(10F))
        }catch (e: IOException){
            e.printStackTrace()
        }

        graphics2D.font = Font.createFont(Font.TRUETYPE_FONT, File(IngeniaMC.plugin.dataFolder, "fonts/Minecraft.otf")).deriveFont(10F)
        writeCenteredText(title, 13)

        for (top in 0..2){
            if(sortedScores().size > top)
                writeName(top + 1, sortedScores()[top].value, sortedScores()[top].key, false)
        }

        if(sortedScores().isNotEmpty())

        for(place in 0 until sortedScores().size){
            if(sortedScores()[place].key == player.name) {
                writeName(place + 1, sortedScores()[place].value, sortedScores()[place].key, true)
                break
            }
        }

        return bufferedImage
    }

}