package me.m64diamondstar.ingeniamccore.shows.type

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.shows.EffectShow
import me.m64diamondstar.ingeniamccore.shows.utils.Effect
import me.m64diamondstar.ingeniamccore.utils.LocationUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class Firework(effectShow: EffectShow, id: Int) : Effect(effectShow, id) {

    override fun execute(players: List<Player>?) {
        val location = LocationUtils.getLocationFromString(getSection().getString("Location")!!) ?: return
        val velocity = if (getSection().get("Velocity") != null)
            if (LocationUtils.getVectorFromString(getSection().getString("Velocity")!!) != null)
                LocationUtils.getVectorFromString(getSection().getString("Velocity")!!)!!
            else Vector(0.0, 0.0, 0.0)
        else Vector(0.0, 0.0, 0.0)

        val colors = if (getSection().get("Colors") != null) Colors.getBukkitColorList(getSection().getString("Colors")!!) else listOf(
            Color.WHITE)
        val fadeColors = if (getSection().get("FadeColors") != null) Colors.getBukkitColorList(getSection().getString("FadeColors")!!) else emptyList()

        val power = if (getSection().get("Power") != null) getSection().getInt("Power") else 2
        var shape = FireworkEffect.Type.BALL
        val shotAtAngle = if (getSection().get("ShotAtAngle") != null) getSection().getBoolean("ShotAtAngle") else false
        val flicker = if (getSection().get("Flicker") != null) getSection().getBoolean("Flicker") else false
        val trail = if (getSection().get("Trail") != null) getSection().getBoolean("Trail") else false

        try{
            shape = if(getSection().get("FireworkShape") != null) FireworkEffect.Type.valueOf(getSection().getString("FireworkShape")!!) else FireworkEffect.Type.BALL
        }catch (_: NullPointerException){ }

        val firework = location.world!!.spawnEntity(location, EntityType.FIREWORK) as Firework
        val fireworkMeta = firework.fireworkMeta

        firework.velocity = velocity
        firework.isShotAtAngle = shotAtAngle
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(colors)
                .withFade(fadeColors)
                .flicker(flicker)
                .trail(trail)
                .with(shape)
                .build()
        )
        fireworkMeta.power = power

        firework.fireworkMeta = fireworkMeta

    }

    override fun getType(): Type {
        return Type.FIREWORK
    }

    override fun isSync(): Boolean {
        return true
    }

    override fun getDefaults(): List<Pair<String, Any>> {
        val list = ArrayList<Pair<String, Any>>()
        list.add(Pair("Type", "FIREWORK"))
        list.add(Pair("Location", "world, 0, 0, 0"))
        list.add(Pair("Velocity", "0.0, 0.0, 0.0"))
        list.add(Pair("Colors", "#ffffff, #000000"))
        list.add(Pair("FadeColors", " "))
        list.add(Pair("Power", 2))
        list.add(Pair("FireworkShape", "BALL"))
        list.add(Pair("ShotAtAngle", false))
        list.add(Pair("Flicker", false))
        list.add(Pair("Trail", false))
        list.add(Pair("Delay", 0))
        return list
    }
}