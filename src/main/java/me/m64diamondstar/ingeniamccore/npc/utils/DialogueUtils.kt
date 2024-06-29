package me.m64diamondstar.ingeniamccore.npc.utils

import me.m64diamondstar.ingeniamccore.IngeniaMC
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object DialogueUtils {

    fun getDialogueFormat(lines: List<String>, backdropType: DialogueBackdropType?, backdropColor: TextColor?): Component{
        return getDialogueFormat(lines.getOrNull(0), lines.getOrNull(1), lines.getOrNull(2), lines.getOrNull(3), lines.getOrNull(4), backdropType, backdropColor)
    }

    fun getDialogueFormat(line1: String?, line2: String?, line3: String?, line4: String?, line5: String?, backdropType: DialogueBackdropType?, backdropColor: TextColor?): Component{
        val safeLine1 = ArrayList<Pair<Char, Int>>()
        val safeLine2 = ArrayList<Pair<Char, Int>>()
        val safeLine3 = ArrayList<Pair<Char, Int>>()
        val safeLine4 = ArrayList<Pair<Char, Int>>()
        val safeLine5 = ArrayList<Pair<Char, Int>>()
        line1?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine1.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line2?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine2.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line3?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine3.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line4?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine4.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line5?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine5.add(Pair(it, CharWidth.getAsMap()[it]!!)) }

        val result = Component.text()
            .append(Component.text().content(backdropType?.getBackdrop() ?: "\uEE01").color(backdropColor ?: TextColor.color(255, 255, 255)))
            .append(Component.text().content("\uF80C\uF80C\uF808\uF801"))
            .append(Component.text().content("\uF829\uE001\uF828\uF821")) // Second char is the skin
            .append(Component.text().content(safeLine1.map { it.first }.joinToString("")).font(Key.key("ingeniamc:dialogue_line_1")))
            .append(Component.text().content(safeLine1.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
            .append(Component.text().content(safeLine2.map { it.first }.joinToString("")).font(Key.key("ingeniamc:dialogue_line_2")))
            .append(Component.text().content(safeLine2.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
            .append(Component.text().content(safeLine3.map { it.first }.joinToString("")).font(Key.key("ingeniamc:dialogue_line_3")))
            .append(Component.text().content(safeLine3.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
            .append(Component.text().content(safeLine4.map { it.first }.joinToString("")).font(Key.key("ingeniamc:dialogue_line_4")))
            .append(Component.text().content(safeLine4.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
            .append(Component.text().content(safeLine5.map { it.first }.joinToString("")).font(Key.key("ingeniamc:dialogue_line_5")))
            .append(Component.text().content(safeLine5.map { '\uF800'.plus(it.second) }.joinToString("")).font(Key.key("minecraft:default")))
            .append(Component.text().content("\uF82C\uF82C\uF828\uF821\uF809\uF80B\uF808\uF801"))
            .build()

        return result
    }

    fun sendProgressiveDialogue(player: Player, lines: List<String>, backdropType: DialogueBackdropType?, backdropColor: TextColor?): BukkitTask{
        return sendProgressiveDialogue(player, lines.getOrNull(0), lines.getOrNull(1), lines.getOrNull(2), lines.getOrNull(3), lines.getOrNull(4), backdropType, backdropColor)
    }

    fun sendProgressiveDialogue(player: Player, line1: String?, line2: String?, line3: String?, line4: String?, line5: String?, backdropType: DialogueBackdropType?, backdropColor: TextColor?): BukkitTask{
        val safeLine1 = ArrayList<Pair<Char, Int>>()
        val safeLine2 = ArrayList<Pair<Char, Int>>()
        val safeLine3 = ArrayList<Pair<Char, Int>>()
        val safeLine4 = ArrayList<Pair<Char, Int>>()
        val safeLine5 = ArrayList<Pair<Char, Int>>()
        val progressiveLine1 = ArrayList<Pair<Char, Int>>()
        val progressiveLine2 = ArrayList<Pair<Char, Int>>()
        val progressiveLine3 = ArrayList<Pair<Char, Int>>()
        val progressiveLine4 = ArrayList<Pair<Char, Int>>()
        val progressiveLine5 = ArrayList<Pair<Char, Int>>()
        line1?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine1.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line2?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine2.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line3?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine3.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line4?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine4.add(Pair(it, CharWidth.getAsMap()[it]!!)) }
        line5?.forEach { if(CharWidth.getAsMap().containsKey(it)) safeLine5.add(Pair(it, CharWidth.getAsMap()[it]!!)) }

        val totalChars = (line1?.length ?: 0) + (line2?.length ?: 0) + (line3?.length ?: 0) + (line4?.length ?: 0) + (line5?.length ?: 0)

        val task = object: BukkitRunnable(){
            var i = 0
            override fun run() {
                    val result = Component.text()
                        .append(Component.text().content(backdropType?.getBackdrop() ?: "\uEE01").color(backdropColor ?: TextColor.color(255, 255, 255)))
                        .append(Component.text().content("\uF80C\uF80C\uF808\uF801"))
                        .append(Component.text().content("\uF829\uE001\uF828\uF821"))
                        .append(
                            Component.text().content(progressiveLine1.map { it.first }.joinToString(""))
                                .font(Key.key("ingeniamc:dialogue_line_1"))
                        )
                        .append(
                            Component.text().content(progressiveLine1.map { '\uF800'.plus(it.second) }.joinToString(""))
                                .font(Key.key("minecraft:default"))
                        )
                        .append(
                            Component.text().content(progressiveLine2.map { it.first }.joinToString(""))
                                .font(Key.key("ingeniamc:dialogue_line_2"))
                        )
                        .append(
                            Component.text().content(progressiveLine2.map { '\uF800'.plus(it.second) }.joinToString(""))
                                .font(Key.key("minecraft:default"))
                        )
                        .append(
                            Component.text().content(progressiveLine3.map { it.first }.joinToString(""))
                                .font(Key.key("ingeniamc:dialogue_line_3"))
                        )
                        .append(
                            Component.text().content(progressiveLine3.map { '\uF800'.plus(it.second) }.joinToString(""))
                                .font(Key.key("minecraft:default"))
                        )
                        .append(
                            Component.text().content(progressiveLine4.map { it.first }.joinToString(""))
                                .font(Key.key("ingeniamc:dialogue_line_4"))
                        )
                        .append(
                            Component.text().content(progressiveLine4.map { '\uF800'.plus(it.second) }.joinToString(""))
                                .font(Key.key("minecraft:default"))
                        )
                        .append(
                            Component.text().content(progressiveLine5.map { it.first }.joinToString(""))
                                .font(Key.key("ingeniamc:dialogue_line_5"))
                        )
                        .append(
                            Component.text().content(progressiveLine5.map { '\uF800'.plus(it.second) }.joinToString(""))
                                .font(Key.key("minecraft:default"))
                        )
                        .append(Component.text().content("\uF82C\uF82C\uF828\uF821\uF809\uF80B\uF808\uF801"))
                        .build()

                    if (i < (line1?.length ?: 0)) {
                        progressiveLine1.add(Pair(safeLine1[i].first, safeLine1[i].second))
                    } else if (i < (line1?.length ?: 0) + (line2?.length ?: 0)) {
                        progressiveLine2.add(
                            Pair(safeLine2[i - (line1?.length ?: 0)].first, safeLine2[i - (line1?.length ?: 0)].second)
                        )
                    } else if (i < (line1?.length ?: 0) + (line2?.length ?: 0) + (line3?.length ?: 0)) {
                        progressiveLine3.add(
                            Pair(
                                safeLine3[i - (line1?.length ?: 0) - (line2?.length ?: 0)].first,
                                safeLine3[i - (line1?.length ?: 0) - (line2?.length ?: 0)].second
                            )
                        )
                    } else if (i < (line1?.length ?: 0) + (line2?.length ?: 0) + (line3?.length ?: 0) + (line4?.length
                            ?: 0)
                    ) {
                        progressiveLine4.add(
                            Pair(
                                safeLine4[i - (line1?.length ?: 0) - (line2?.length ?: 0) - (line3?.length ?: 0)].first,
                                safeLine4[i - (line1?.length ?: 0) - (line2?.length ?: 0) - (line3?.length ?: 0)].second
                            )
                        )
                    } else if (i < (line1?.length ?: 0) + (line2?.length ?: 0) + (line3?.length ?: 0) + (line4?.length
                            ?: 0) + (line5?.length ?: 0)
                    ) {
                        progressiveLine5.add(
                            Pair(
                                safeLine5[i - (line1?.length ?: 0) - (line2?.length ?: 0) - (line3?.length
                                    ?: 0) - (line4?.length ?: 0)].first,
                                safeLine5[i - (line1?.length ?: 0) - (line2?.length ?: 0) - (line3?.length
                                    ?: 0) - (line4?.length ?: 0)].second
                            )
                        )
                    }

                    (player as Audience).sendActionBar(result)

                    if (i == totalChars) {
                        this.cancel()
                        return
                    }

                    i++
            }
        }.runTaskTimerAsynchronously(IngeniaMC.plugin, 0L, 1L)
        return task
    }

}