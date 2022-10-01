package me.m64diamondstar.ingeniamccore.wands.wands

import me.m64diamondstar.ingeniamccore.Main
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import me.m64diamondstar.ingeniamccore.wands.Cooldowns
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.world.phys.Vec3D
import net.minecraft.world.entity.EnumMoveType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector
import java.util.*

class Sled(player: Player) {
    private fun south(sled: ArmorStand, seat: ArmorStand, player: Player) {
        object : BukkitRunnable() {
            var c = 0
            override fun run() {
                if (player.location.add(0.0, 1.0, 1.0).block.type != Material.AIR || player.location.add(
                        0.0,
                        2.0,
                        1.0
                    ).block.type != Material.AIR
                ) {
                    if (!player.location.add(0.0, 1.0, 1.0).block.type.toString().contains("BUTTON")
                        && !player.location.add(0.0, 1.0, 1.0).block.type.toString().contains("PRESSURE_PLATE")
                    ) {
                        player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!"))
                        )
                        sled.remove()
                        seat.remove()
                        player.teleport(player.location.add(0.0, 0.5, 0.0))
                        cancel()
                        return
                    }
                }


                //After 15 seconds
                if (c == 35) {
                    pillar(player)
                    cancel()
                    seat.remove()
                    sled.removePassenger(player)
                    sled.setGravity(true)
                    val vec3D = Vec3D(0.0, 2.0, 0.0)
                    (player as CraftPlayer).handle.a(EnumMoveType.e, vec3D)
                    (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                    player.setVelocity(Vector(0F, 0.8F, 2F))
                    sled.setVelocity(Vector(0F, 0.8F, 2F))
                    object : BukkitRunnable() {
                        var c2 = 0
                        override fun run() {
                            if (sled.isOnGround()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                                this.cancel()
                                return
                            }
                            sled.headPose = EulerAngle(Math.toRadians(c2.toDouble() * 1.5), 0.0, 0.0)
                            c2++
                        }
                    }.runTaskTimer(
                        Main.plugin, 0L, 1L
                    )
                    Bukkit.getScheduler().scheduleSyncDelayedTask(
                        Main.plugin, {
                            if (!sled.isDead()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                            }
                        }, 60L
                    )
                    return
                }
                val vec3D = Vec3D(0.0, 0.0, c.toDouble() / 90)
                (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                (seat as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                player.world.spawnParticle(
                    Particle.BLOCK_CRACK,
                    player.location.add(0.0, 0.3, 0.0),
                    30,
                    0.3,
                    0.0,
                    0.3,
                    0.0,
                    Material.SNOW_BLOCK.createBlockData()
                )
                val `as` = player.world.spawnEntity(
                    player.location.add(Random().nextDouble() - 0.5, -1.6, 0.0),
                    EntityType.ARMOR_STAND
                ) as ArmorStand
                `as`.headPose = EulerAngle(
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(
                        Math.random() * (15 + 15) - 15
                    )
                )
                `as`.setGravity(false)
                `as`.isInvisible = true
                Objects.requireNonNull(`as`.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { `as`.remove() }, 25L
                )
                c++
            }
        }.runTaskTimer(Main.plugin, 0L, 1L)
    }

    private fun west(sled: ArmorStand, seat: ArmorStand, player: Player) {
        object : BukkitRunnable() {
            var c = 0
            override fun run() {
                if (player.location.add(-1.0, 1.0, 0.0).block.type != Material.AIR || player.location.add(
                        -1.0,
                        2.0,
                        0.0
                    ).block.type != Material.AIR
                ) {
                    if (!player.location.add(-1.0, 1.0, 0.0).block.type.toString().contains("BUTTON")
                        && !player.location.add(-1.0, 1.0, 0.0).block.type.toString().contains("PRESSURE_PLATE")
                    ) {
                        player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!"))
                        )
                        sled.remove()
                        seat.remove()
                        player.teleport(player.location.add(0.0, 0.5, 0.0))
                        cancel()
                        return
                    }
                }


                //After 15 seconds
                if (c == 35) {
                    pillar(player)
                    cancel()
                    seat.remove()
                    sled.removePassenger(player)
                    sled.setGravity(true)
                    val vec3D = Vec3D(0.0, 2.0, 0.0)
                    (player as CraftPlayer).handle.a(EnumMoveType.e, vec3D)
                    (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                    player.setVelocity(Vector(-2F, 0.8F, 0F))
                    sled.setVelocity(Vector(-2F, 0.8F, 0F))
                    object : BukkitRunnable() {
                        var c2 = 0
                        override fun run() {
                            if (sled.isOnGround()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                                this.cancel()
                                return
                            }
                            sled.headPose = EulerAngle(Math.toRadians(c2.toDouble() * 1.5), Math.toRadians(90.0), 0.0)
                            c2++
                        }
                    }.runTaskTimer(
                        Main.plugin, 0L, 1L
                    )
                    Bukkit.getScheduler().scheduleSyncDelayedTask(
                        Main.plugin, {
                            if (!sled.isDead()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                            }
                        }, 60L
                    )
                    return
                }
                val vec3D = Vec3D(-(c.toDouble() / 90), 0.0, 0.0)
                (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                (seat as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                player.world.spawnParticle(
                    Particle.BLOCK_CRACK,
                    player.location.add(0.0, 0.3, 0.0),
                    30,
                    0.3,
                    0.0,
                    0.3,
                    0.0,
                    Material.SNOW_BLOCK.createBlockData()
                )
                val `as` = player.world.spawnEntity(
                    player.location.add(0.0, -1.6, Random().nextDouble() - 0.5),
                    EntityType.ARMOR_STAND
                ) as ArmorStand
                `as`.headPose = EulerAngle(
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(
                        Math.random() * (15 + 15) - 15
                    )
                )
                `as`.setGravity(false)
                `as`.isInvisible = true
                Objects.requireNonNull(`as`.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { `as`.remove() }, 25L
                )
                c++
            }
        }.runTaskTimer(Main.plugin, 0L, 1L)
    }

    private fun north(sled: ArmorStand, seat: ArmorStand, player: Player) {
        object : BukkitRunnable() {
            var c = 0
            override fun run() {
                if (player.location.add(0.0, 1.0, -1.0).block.type != Material.AIR || player.location.add(
                        0.0,
                        2.0,
                        -1.0
                    ).block.type != Material.AIR
                ) {
                    if (!player.location.add(0.0, 1.0, -1.0).block.type.toString().contains("BUTTON")
                        && !player.location.add(0.0, 1.0, -1.0).block.type.toString().contains("PRESSURE_PLATE")
                    ) {
                        player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!"))
                        )
                        sled.remove()
                        seat.remove()
                        player.teleport(player.location.add(0.0, 0.5, 0.0))
                        cancel()
                        return
                    }
                }


                //After 15 seconds
                if (c == 35) {
                    pillar(player)
                    cancel()
                    seat.remove()
                    sled.removePassenger(player)
                    sled.setGravity(true)
                    val vec3D = Vec3D(0.0, 2.0, 0.0)
                    (player as CraftPlayer).handle.a(EnumMoveType.e, vec3D)
                    (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                    player.setVelocity(Vector(0F, 0.8F, -2F))
                    sled.setVelocity(Vector(0F, 0.8F, -2F))
                    object : BukkitRunnable() {
                        var c2 = 0
                        override fun run() {
                            if (sled.isOnGround()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                                this.cancel()
                                return
                            }
                            sled.headPose = EulerAngle(Math.toRadians(c2.toDouble() * 1.5), Math.toRadians(180.0), 0.0)
                            c2++
                        }
                    }.runTaskTimer(
                        Main.plugin, 0L, 1L
                    )
                    Bukkit.getScheduler().scheduleSyncDelayedTask(
                        Main.plugin, {
                            if (!sled.isDead()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                            }
                        }, 60L
                    )
                    return
                }
                val vec3D = Vec3D(0.0, 0.0, -(c.toDouble() / 90))
                (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                (seat as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                player.world.spawnParticle(
                    Particle.BLOCK_CRACK,
                    player.location.add(0.0, 0.3, 0.0),
                    30,
                    0.3,
                    0.0,
                    0.3,
                    0.0,
                    Material.SNOW_BLOCK.createBlockData()
                )
                val `as` = player.world.spawnEntity(
                    player.location.add(Random().nextDouble() - 0.5, -1.6, 0.0),
                    EntityType.ARMOR_STAND
                ) as ArmorStand
                `as`.headPose = EulerAngle(
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(
                        Math.random() * (15 + 15) - 15
                    )
                )
                `as`.setGravity(false)
                `as`.isInvisible = true
                Objects.requireNonNull(`as`.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { `as`.remove() }, 25L
                )
                c++
            }
        }.runTaskTimer(Main.plugin, 0L, 1L)
    }

    private fun east(sled: ArmorStand, seat: ArmorStand, player: Player) {
        object : BukkitRunnable() {
            var c = 0
            override fun run() {
                if (player.location.add(1.0, 1.0, 0.0).block.type != Material.AIR || player.location.add(
                        1.0,
                        2.0,
                        0.0
                    ).block.type != Material.AIR
                ) {
                    if (!player.location.add(1.0, 1.0, 0.0).block.type.toString().contains("BUTTON")
                        && !player.location.add(1.0, 1.0, 0.0).block.type.toString().contains("PRESSURE_PLATE")
                    ) {
                        player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou bumped into a block!"))
                        )
                        sled.remove()
                        seat.remove()
                        player.teleport(player.location.add(0.0, 0.5, 0.0))
                        cancel()
                        return
                    }
                }


                //After 15 seconds
                if (c == 35) {
                    pillar(player)
                    cancel()
                    seat.remove()
                    sled.removePassenger(player)
                    sled.setGravity(true)
                    val vec3D = Vec3D(0.0, 2.0, 0.0)
                    (player as CraftPlayer).handle.a(EnumMoveType.e, vec3D)
                    (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                    player.setVelocity(Vector(2F, 0.8F, 0F))
                    sled.setVelocity(Vector(2F, 0.8F, 0F))
                    object : BukkitRunnable() {
                        var c2 = 0
                        override fun run() {
                            if (sled.isOnGround()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                                this.cancel()
                                return
                            }
                            sled.headPose = EulerAngle(Math.toRadians(c2.toDouble() * 1.5), Math.toRadians(-90.0), 0.0)
                            c2++
                        }
                    }.runTaskTimer(
                        Main.plugin, 0L, 1L
                    )
                    Bukkit.getScheduler().scheduleSyncDelayedTask(
                        Main.plugin, {
                            if (!sled.isDead()) {
                                sled.getWorld().spawnParticle(
                                    Particle.CLOUD,
                                    sled.getLocation().add(0.0, 1.4, 0.0),
                                    100,
                                    0.3,
                                    0.3,
                                    0.3,
                                    0.0
                                )
                                sled.remove()
                            }
                        }, 60L
                    )
                    return
                }
                val vec3D = Vec3D(c.toDouble() / 90, 0.0, 0.0)
                (sled as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                (seat as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                player.world.spawnParticle(
                    Particle.BLOCK_CRACK,
                    player.location.add(0.0, 0.3, 0.0),
                    30,
                    0.3,
                    0.0,
                    0.3,
                    0.0,
                    Material.SNOW_BLOCK.createBlockData()
                )
                val `as` = player.world.spawnEntity(
                    player.location.add(0.0, -1.6, Random().nextDouble() - 0.5),
                    EntityType.ARMOR_STAND
                ) as ArmorStand
                `as`.headPose = EulerAngle(
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(Math.random() * (15 + 15) - 15),
                    Math.toRadians(
                        Math.random() * (15 + 15) - 15
                    )
                )
                `as`.setGravity(false)
                `as`.isInvisible = true
                Objects.requireNonNull(`as`.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.plugin, { `as`.remove() }, 25L
                )
                c++
            }
        }.runTaskTimer(Main.plugin, 0L, 1L)
    }

    private fun pillar(player: Player) {
        for (i in 0..3) {
            val s1 = player.world.spawnEntity(
                player.location.add(0.4, -3 + i * 0.6, 0.4),
                EntityType.ARMOR_STAND
            ) as ArmorStand
            s1.headPose = EulerAngle(
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(
                    Math.random() * (15 + 15) - 15
                )
            )
            s1.setGravity(false)
            s1.isInvisible = true
            Objects.requireNonNull(s1.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
            val s2 = player.world.spawnEntity(
                player.location.add(-0.4, -3 + i * 0.6, -0.4),
                EntityType.ARMOR_STAND
            ) as ArmorStand
            s2.headPose = EulerAngle(
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(
                    Math.random() * (15 + 15) - 15
                )
            )
            s2.setGravity(false)
            s2.isInvisible = true
            Objects.requireNonNull(s2.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
            val s3 = player.world.spawnEntity(
                player.location.add(0.4, -3 + i * 0.6, -0.4),
                EntityType.ARMOR_STAND
            ) as ArmorStand
            s3.headPose = EulerAngle(
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(
                    Math.random() * (15 + 15) - 15
                )
            )
            s3.setGravity(false)
            s3.isInvisible = true
            Objects.requireNonNull(s3.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
            val s4 = player.world.spawnEntity(
                player.location.add(-0.4, -3 + i * 0.6, 0.4),
                EntityType.ARMOR_STAND
            ) as ArmorStand
            s4.headPose = EulerAngle(
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(Math.random() * (15 + 15) - 15),
                Math.toRadians(
                    Math.random() * (15 + 15) - 15
                )
            )
            s4.setGravity(false)
            s4.isInvisible = true
            Objects.requireNonNull(s4.equipment)?.helmet = ItemStack(Material.SNOW_BLOCK)
            object : BukkitRunnable() {
                var c = 0
                override fun run() {
                    var vec3D = Vec3D(0.0, 0.35, 0.0)
                    if (c <= 5) {
                        (s1 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                        (s2 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                        (s3 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                        (s4 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                    }
                    if (c > 40) {
                        vec3D = Vec3D(0.0, -0.15, 0.0)
                        (s1 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                        (s2 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                        (s3 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                        (s4 as CraftEntity).handle.a(EnumMoveType.e, vec3D)
                    }
                    if (c == 80) {
                        cancel()
                        s1.remove()
                        s2.remove()
                        s3.remove()
                        s4.remove()
                        return
                    }
                    c++
                }
            }.runTaskTimer(Main.plugin, 0L, 1L)
        }
    }

    init {
        properInit(player)
    }

    private fun properInit(player: Player) {
        if (!(player as CraftPlayer).isOnGround) {
            player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou can only use this on the ground!"))
            )
            return
        }
        val spawnLoc = player.getLocation()
        spawnLoc.add(0.0, -1.6, 0.0)
        spawnLoc.yaw = 0f
        val sled = player.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND) as ArmorStand
        sled.setGravity(false)
        sled.isInvisible = true
        sled.isInvulnerable = true
        spawnLoc.add(0.0, 0.3, 0.0)
        val seat = player.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND) as ArmorStand
        seat.setGravity(false)
        seat.isInvisible = true
        seat.isInvulnerable = true
        seat.addPassenger(player)
        seat.location.yaw = 0f
        val item = ItemStack(Material.DIAMOND_SWORD)
        val meta = item.itemMeta!!
        meta.setCustomModelData(8)
        item.itemMeta = meta
        Objects.requireNonNull(sled.equipment)?.helmet = item
        if (player.getLocation().yaw >= -45 && player.getLocation().yaw < 45) {
            south(sled, seat, player)
            sled.headPose = EulerAngle(0.0, Math.toRadians(0.0), 0.0)
        } else if (player.getLocation().yaw >= 45 && player.getLocation().yaw < 135) {
            west(sled, seat, player)
            sled.headPose = EulerAngle(0.0, Math.toRadians(90.0), 0.0)
        } else if (player.getLocation().yaw >= 135 || player.getLocation().yaw < -135) {
            north(sled, seat, player)
            sled.headPose = EulerAngle(0.0, Math.toRadians(180.0), 0.0)
        } else if (player.getLocation().yaw >= -135 && player.getLocation().yaw < -45) {
            east(sled, seat, player)
            sled.headPose = EulerAngle(0.0, Math.toRadians(-90.0), 0.0)
        }
        Cooldowns.addPlayer(player, 4000L, 7000L, 9000L, 12000L)
    }
}