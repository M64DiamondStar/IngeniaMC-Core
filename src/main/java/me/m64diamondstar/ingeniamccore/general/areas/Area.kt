package me.m64diamondstar.ingeniamccore.general.areas

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.Polygonal2DRegion
import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import org.bukkit.Bukkit
import org.bukkit.World

class Area(val category: String, name: String): DataConfiguration("area/$category", name.replace(".yml", "")) {

    val name: String = name.replace(".yml", "")

    fun createArea(world: World){
        this.getConfig().set("Display-Name", name)
        this.getConfig().set("Weight", 1)
        this.getConfig().set("World", world.name)
        this.getConfig().set("Min-Y", -64)
        this.getConfig().set("Max-Y", 319)

        val list = ArrayList<String>()

        list.add("An area is used so that the player can locate himself, and to play the correct music.")
        list.add("You can change the Display-Name to whatever you like, but NO COLOR CODES!")
        list.add("If two or more areas are overlapping, the area with the highest Weight will be selected.")

        this.getConfig().options().setHeader(list)
        this.save()
    }

    var minY: Int
        get() = this.getConfig().getInt("Min-Y")
        set(value) {
            this.getConfig().set("Min-Y", value)
            this.save()
        }

    var maxY: Int
        get() = this.getConfig().getInt("Max-Y")
        set(value) {
            this.getConfig().set("Max-Y", value)
            this.save()
        }

    var weight: Int
        get() = this.getConfig().getInt("Weight")
        set(value) {
            this.getConfig().set("Weight", value)
            this.save()
        }

    var displayName: String
        get() = this.getConfig().getString("Display-Name")!!
        set(value) {
            this.getConfig().set("Display-Name", value)
            this.save()
        }

    var area: Polygonal2DRegion?
        get() {
            if(this.getConfig().get("World") == null || this.getConfig().get("Vertices") == null)
                return null
            val list = ArrayList<BlockVector3>()
            this.getConfig().getStringList("Vertices").forEach { list.add(BlockVector3.fromLongPackedForm(it.toLong())) }
            val polygonal2DRegion = Polygonal2DRegion(BukkitAdapter.adapt(Bukkit.getWorld(this.getConfig().getString("World")!!)))
            list.forEach { polygonal2DRegion.addPoint(it) }
            return polygonal2DRegion
        }
        set(value) {
            if(value == null){
                this.getConfig().set("Vertices", null)
            }else {
                val list = ArrayList<String>()
                value.points.forEach { list.add(it.toBlockVector3().toLongPackedForm().toString()) }
                this.getConfig().set("Vertices", list)
                this.save()
            }
        }

    fun setMusic(url: String){
        this.getConfig().set("Music", url)
        this.save()
    }
    fun getMusic(): String?{
        return this.getConfig().getString("Music")
    }

}