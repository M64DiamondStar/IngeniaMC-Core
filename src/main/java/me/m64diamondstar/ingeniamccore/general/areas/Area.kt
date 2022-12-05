package me.m64diamondstar.ingeniamccore.general.areas

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.Polygonal2DRegion
import me.m64diamondstar.ingeniamccore.data.Configuration
import org.bukkit.Bukkit
import org.bukkit.World
import javax.swing.text.StyledEditorKit.BoldAction
import kotlin.random.Random

class Area(category: String, name: String): Configuration("area/$category", name.replace(".yml", ""), false, true) {

    val name: String
    val category: String

    init {
        this.name = name.replace(".yml", "")
        this.category = category
    }

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
        this.reloadConfig()
    }

    var minY: Int
        get() = this.getConfig().getInt("Min-Y")
        set(value) {
            this.getConfig().set("Min-Y", value)
            this.reloadConfig()
        }

    var maxY: Int
        get() = this.getConfig().getInt("Max-Y")
        set(value) {
            this.getConfig().set("Max-Y", value)
            this.reloadConfig()
        }

    var weight: Int
        get() = this.getConfig().getInt("Weight")
        set(value) {
            this.getConfig().set("Weight", value)
            this.reloadConfig()
        }

    var displayName: String
        get() = this.getConfig().getString("Display-Name")!!
        set(value) {
            this.getConfig().set("Display-Name", value)
            this.reloadConfig()
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
                this.reloadConfig()
            }
            AreaUtils.setArea(this)
        }

    fun setMusic(url: String){
        this.getConfig().set("Music", url)
        this.reloadConfig()
    }
    fun getMusic(): String?{
        return this.getConfig().getString("Music")
    }

}