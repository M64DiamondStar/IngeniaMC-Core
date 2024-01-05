package me.m64diamondstar.ingeniamccore.warps

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class WarpManager: DataConfiguration("", "warps.yml") {

    fun existsWarp(id: String): Boolean {
        return this.getConfig().get(id) != null
    }

    fun createWarp(id: String, location: Location, type: WarpType) {
        this.getConfig().set("$id.location", location)
        this.getConfig().set("$id.type", type.toString())
        this.save()
    }

    fun deleteWarp(id: String) {
        this.getConfig().set(id, null)
        this.save()
    }

    fun setWarpLocation(id: String, location: Location) {
        this.getConfig().set("$id.location", location)
        this.save()
    }

    fun setWarpType(id: String, type: WarpType) {
        this.getConfig().set("$id.type", type.toString())
        this.save()
    }

    fun setWarpItem(id: String, itemStack: ItemStack) {
        this.getConfig().set("$id.item", itemStack)
        this.save()
    }

    fun getWarpLocation(id: String): Location? {
        return this.getConfig().getLocation("$id.location")?.clone()
    }

    fun getWarpType(id: String): WarpType {
        return WarpType.valueOf(this.getConfig().getString("$id.type")!!)
    }

    fun getWarpItem(id: String): ItemStack? {
        val item = this.getConfig().getItemStack("$id.item") ?: return null
        val meta = item.itemMeta ?: return null
        meta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "warp-id"), PersistentDataType.STRING, id)
        item.itemMeta = meta
        return this.getConfig().getItemStack("$id.item")
    }

    fun getAllIDs(): List<String> {
        val list = ArrayList<String>()
        this.getConfig().getKeys(false).forEach { list.add(it) }
        return list
    }

    fun getAllIDs(type: WarpType): List<String> {
        val list = ArrayList<String>()
        this.getConfig().getKeys(false).forEach { if (this.getWarpType(it) == type) list.add(it) }
        return list
    }

    fun getAllLocations(): List<Location> {
        val list = ArrayList<Location>()
        this.getConfig().getKeys(false).forEach { list.add(this.getWarpLocation(it)!!) }
        return list
    }

}