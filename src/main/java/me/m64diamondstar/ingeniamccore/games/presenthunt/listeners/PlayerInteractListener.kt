package me.m64diamondstar.ingeniamccore.games.presenthunt.listeners

import gg.flyte.twilight.scheduler.repeat
import me.m64diamondstar.ingeniamccore.discord.bot.DiscordLogType
import me.m64diamondstar.ingeniamccore.discord.bot.logToDiscord
import me.m64diamondstar.ingeniamccore.games.presenthunt.PresentHuntUtils
import me.m64diamondstar.ingeniamccore.general.player.IngeniaPlayer
import me.m64diamondstar.ingeniamccore.general.player.StatisticKey
import me.m64diamondstar.ingeniamccore.general.player.StatisticType
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType
import me.m64diamondstar.ingeniamccore.utils.messages.sendMini
import me.m64diamondstar.ingeniamccore.wands.wands.SnowCannon
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

class PlayerInteractListener: Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.clickedBlock!!.type != Material.PLAYER_HEAD) return
        if (!PresentHuntUtils.containsActivePresent(event.clickedBlock!!.location)) return

        val player = event.player
        val location = event.clickedBlock!!.location
        val presentHunt = PresentHuntUtils.getActivePresent(location)
        PresentHuntUtils.getActivePresent(location = location)?.spawnRandomPresent()

        PresentHuntUtils.removeActivePresent(location = location)

        val randomExp = Random.nextInt(1, 4)
        val randomStars = Random.nextInt(1, 3)
        val randomGolden = Random.nextInt(1, 201)

        var totalExp = randomExp
        var totalStars = randomStars

        if(randomGolden in 1..200) {
            player.sendMini(
                "\uD83C\uDF81 <#ededed>You found a present! " +
                        "You gained $randomExp exp and $randomStars<gs>!"
            )

            if (randomGolden in 1..3) {
                //Extra balance & exp
                val randomExtraExp = Random.nextInt(40, 60)
                val randomExtraGs = Random.nextInt(30, 40)

                player.sendMini(
                    "<${MessageType.BACKGROUND}><italic>Oh wait...</italic><br>" +
                            "<#fa0000><b>BONUS! <reset><#ededed>You found $randomExtraExp extra exp and $randomExtraGs<gs>!"
                )

                totalExp += randomExtraExp
                totalStars += randomExtraGs

                var c = 0
                repeat(3, false) {
                    if(c == 3) {
                        this.cancel()
                        return@repeat
                    }
                    val firework = location.clone().add(0.5, 0.5, 0.5).world!!.spawnEntity(
                        location,
                        EntityType.FIREWORK_ROCKET
                    ) as Firework
                    val fireworkMeta = firework.fireworkMeta
                    fireworkMeta.addEffect(
                        FireworkEffect.builder()
                            .withColor(Color.RED, Color.WHITE)
                            .flicker(true)
                            .with(FireworkEffect.Type.BALL)
                            .build()
                    )
                    firework.fireworkMeta = fireworkMeta
                    firework.detonate()
                    c++
                }
            }

            if(randomGolden == 11 && !player.hasPermission("ingeniawands.frostspray")){
                player.sendMini(
                    "<${MessageType.BACKGROUND}><italic>Oh wait...</italic><br>" +
                            "<b><gradient:#0898FB:#ADF3FD>CHILLY BONUS</gradient></b> <reset><#ededed>You found the <u>Frost Spray Wand</u> in a present!"
                )

                val ingeniaPlayer = IngeniaPlayer(player)
                ingeniaPlayer.givePermission("ingeniawands.frostspray")

                var c = 0
                repeat(3, false) {
                    if(c == 3) {
                        this.cancel()
                        return@repeat
                    }
                    val firework = location.clone().add(0.5, 0.5, 0.5).world!!.spawnEntity(
                        location,
                        EntityType.FIREWORK_ROCKET
                    ) as Firework
                    val fireworkMeta = firework.fireworkMeta
                    fireworkMeta.addEffect(
                        FireworkEffect.builder()
                            .withColor(Color.AQUA, Color.WHITE)
                            .flicker(true)
                            .with(FireworkEffect.Type.BALL)
                            .build()
                    )
                    firework.fireworkMeta = fireworkMeta
                    firework.detonate()
                    c++
                }
            }
        }

        presentHunt?.addPlayerPresent(player)
        presentHunt?.getLeaderboard()?.spawnSign()

        IngeniaPlayer(player).addExp(totalExp.toLong()) // Add total exp (maybe affected by random extra
        IngeniaPlayer(player).addBal(totalStars.toLong()) // Add total gs (maybe affected by random extra)

        val presentsFound = (IngeniaPlayer(player).getStatisticContainer().get(StatisticKey("presents-found", StatisticType.IntType)) ?: 0) + 1
        IngeniaPlayer(player).getStatisticContainer().set(StatisticKey("presents-found", StatisticType.IntType), presentsFound)

        logToDiscord(player, "+1 Present ($presentsFound total)", DiscordLogType.INFO)

        player.playSound(location.clone().add(0.5,0.5, 0.5), Sound.ITEM_ARMOR_EQUIP_TURTLE, 1f, 1f)
        player.world.spawnParticle(
            Particle.SNOWFLAKE, location.add(0.5, 0.35, 0.5),
            300, 0.15, 0.15, 0.15, 0.0
        )
        location.block.type = Material.AIR
    }

}