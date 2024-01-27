package me.m64diamondstar.ingeniamccore.discord.commands.utils.transcripts

import java.awt.Color
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.ln
import kotlin.math.pow

/**
 * Created by Ryzeon
 * Project: discord-html-transcripts
 * Date: 2/12/21 @ 00:32
 * Twitter: @Ryzeon_ 😎
 * GitHub: github.ryzeon.me
 */
class TranscriptFormatter {
    private val STRONG: Pattern = Pattern.compile("\\*\\*(.+?)\\*\\*")
    private val EM: Pattern = Pattern.compile("\\*(.+?)\\*")
    private val S: Pattern = Pattern.compile("~~(.+?)~~")
    private val U: Pattern = Pattern.compile("__(.+?)__")
    private val CODE: Pattern = Pattern.compile("```(.+?)```")
    private val CODE_1: Pattern = Pattern.compile("`(.+?)`")

    // conver this /(?:\r\n|\r|\n)/g to patter in java
    private val NEW_LINE: Pattern = Pattern.compile("\\n")

    fun formatBytes(bytes: Long): String {
        val unit = 1024
        if (bytes < unit) return "$bytes B"
        val exp = (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1].toString() + ""
        return String.format("%.1f %sB", bytes / (unit.toDouble()).pow(exp.toDouble()), pre)
    }

    fun format(originalText: String): String {
        println(originalText)
        var matcher: Matcher = STRONG.matcher(originalText)
        var newText = originalText
        while (matcher.find()) {
            val group: String = matcher.group()
            newText = newText.replace(
                group,
                "<strong>" + group.replace("**", "") + "</strong>"
            )
        }
        matcher = EM.matcher(newText)
        while (matcher.find()) {
            val group: String = matcher.group()
            newText = newText.replace(
                group,
                "<em>" + group.replace("*", "") + "</em>"
            )
        }
        matcher = S.matcher(newText)
        while (matcher.find()) {
            val group: String = matcher.group()
            newText = newText.replace(
                group,
                "<s>" + group.replace("~~", "") + "</s>"
            )
        }
        matcher = U.matcher(newText)
        while (matcher.find()) {
            val group: String = matcher.group()
            newText = newText.replace(
                group,
                "<u>" + group.replace("__", "") + "</u>"
            )
        }
        matcher = CODE.matcher(newText)
        var findCode = false
        while (matcher.find()) {
            val group: String = matcher.group()
            newText = newText.replace(
                group,
                "<div class=\"pre pre--multiline nohighlight\">"
                        + group.replace("```", "").substring(3, -3) + "</div>"
            )
            findCode = true
        }
        if (!findCode) {
            matcher = CODE_1.matcher(newText)
            while (matcher.find()) {
                val group: String = matcher.group()
                newText = newText.replace(
                    group,
                    "<span class=\"pre pre--inline\">" + group.replace("`", "") + "</span>"
                )
            }
        }
        matcher = NEW_LINE.matcher(newText)
        while (matcher.find()) {
            newText = newText.replace(matcher.group(), "<br />")
        }
        return newText
    }

    fun toHex(color: Color): String {
        var hex = Integer.toHexString(color.rgb and 0xffffff)
        while (hex.length < 6) {
            hex = "0$hex"
        }
        return hex
    }
}