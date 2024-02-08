package me.m64diamondstar.ingeniamccore.games.wandclash.util

import org.bukkit.entity.Player

interface ClashWand {

    /**
     * The ID/Name of the wand
     * For example "freeze_wand"
     */
    fun getID(): String

    /**
     * The display name for the wand
     * For example: "Freeze Wand"
     */
    fun getDisplayName(): String

    /**
     * Sets the type of the wand
     * @see ClashWandType for all available types
     */
    fun getType(): ClashWandType

    /**
     * Sets the mana cost of the wand
     * as a value between 0 and 100
     */
    fun getManaCost(): Int

    /**
     * Sets the cooldown of the wand in milliseconds
     * This is only there to prevent the player from
     * spamming the wand.
     */
    fun getCooldown(): Int

    /**
     * This method is called when the wand is used
     * @param player the player who uses the wand
     */
    fun execute(player: Player): Boolean

}