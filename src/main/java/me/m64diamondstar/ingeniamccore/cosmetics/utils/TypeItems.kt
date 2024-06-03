package me.m64diamondstar.ingeniamccore.cosmetics.utils

import org.bukkit.inventory.ItemStack

interface TypeItems {

    fun exists(id: String): Boolean

    fun create(id: String, item: ItemStack, override: Boolean): Boolean

    fun setItem(id: String, item: ItemStack)

    fun getItem(id: String?): ItemStack?

    fun delete(id: String)

    fun getAllIDs(): List<String>

    fun getID(item: ItemStack): String?

    fun applyID(item: ItemStack, id: String): ItemStack?

}