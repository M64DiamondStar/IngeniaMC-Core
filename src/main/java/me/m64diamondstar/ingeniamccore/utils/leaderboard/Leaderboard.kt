package me.m64diamondstar.ingeniamccore.utils.leaderboard

import me.m64diamondstar.ingeniamccore.IngeniaMC
import org.bukkit.entity.Player
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException

class Leaderboard (scores: Map<String, Int>, backgroundColor: Color, outlineColor: Color,
                   titleColor: Color, positionColor: Color, nameColor: Color, lineColor: Color) {

    private var scores: Map<String, Int> = LinkedHashMap()

    private val backgroundColor: Color
    private val outlineColor: Color
    private val titleColor: Color
    private val positionColor: Color
    private val nameColor: Color
    private val lineColor: Color

    private lateinit var bufferedImage: BufferedImage
    private lateinit var graphics2D: Graphics2D

    init{
        this.scores = scores

        this.backgroundColor = backgroundColor
        this.outlineColor = outlineColor
        this.titleColor = titleColor
        this.positionColor = positionColor
        this.nameColor = nameColor
        this.lineColor = lineColor

    }

    private fun writeCenteredText(text: String, y: Int) {
        graphics2D.drawString(text, (256 - graphics2D.fontMetrics.stringWidth(text)) / 2, y)
    }


    private fun writeName(place: Int, score: Int, name: String?, isOwn: Boolean) {
        if (name == null) return
        var y = place * 46 + 10

        if (isOwn) {
            y = 210
            //POSITION + COUNT
            graphics2D.color = positionColor
            writeCenteredText("#$place - $score", y)

            //NAME
            writeCenteredText(name, y + 16)
        }
        else {
            //POSITION + COUNT
            graphics2D.color = positionColor
            writeCenteredText("#$place - $score", y)

            //NAME
            graphics2D.color = nameColor
            writeCenteredText(name, y + 16)

            //LINE
            graphics2D.color = lineColor
            graphics2D.stroke = BasicStroke(2F)
            graphics2D.drawLine(30, y + 24, 225, y + 24)
        }
    }

    private fun sortedScores(): List<Map.Entry<String, Int>> {
        val linkedScores = LinkedHashMap<String, Int>()
        linkedScores.putAll(scores)

        val resultScores = linkedScores.entries.sortedBy { it.value }.associate { it.toPair() }

        return resultScores.asIterable().reversed()
    }

    fun getImage(player: Player): BufferedImage{

        bufferedImage = BufferedImage(256, 256, BufferedImage.TYPE_INT_BGR)
        graphics2D = bufferedImage.createGraphics()

        graphics2D.color = backgroundColor
        graphics2D.fillRect(0, 0, 256, 256)

        graphics2D.color = outlineColor
        graphics2D.stroke = BasicStroke(8F)
        graphics2D.drawRect(0, 0, 256, 256)

        graphics2D.color = titleColor

        try {
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, File(IngeniaMC.plugin.dataFolder, "fonts/Minecraft.otf")).deriveFont(20F))
        }catch (e: IOException){
            e.printStackTrace()
        }

        graphics2D.font = Font.createFont(Font.TRUETYPE_FONT, File(IngeniaMC.plugin.dataFolder, "fonts/Minecraft.otf")).deriveFont(20F)
        writeCenteredText("Top Ridecount", 25)

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