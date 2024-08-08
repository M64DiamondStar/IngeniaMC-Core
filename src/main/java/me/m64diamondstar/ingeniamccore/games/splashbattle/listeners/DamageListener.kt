package me.m64diamondstar.ingeniamccore.games.splashbattle.listeners

import io.papermc.paper.entity.TeleportFlag
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattle
import me.m64diamondstar.ingeniamccore.games.splashbattle.SplashBattleUtils
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent

class DamageListener: Listener {

    @EventHandler
    fun onEntityHeal(event: EntityRegainHealthEvent){
        if(event.entityType != EntityType.PLAYER) return
        val player = event.entity as Player

        if(!SplashBattleUtils.players.contains(player)) return

        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        val entity = e.entity
        if (e.cause == EntityDamageEvent.DamageCause.PROJECTILE) return
        if (e.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        if (e.cause == EntityDamageEvent.DamageCause.FALL) e.isCancelled = true
        if (entity is Player && !SplashBattleUtils.players.contains(entity)) e.isCancelled =
            true
    }

    @EventHandler
    fun onEntityDamage(e: EntityDamageByEntityEvent) {
        if (e.damager.type != EntityType.PLAYER) return
        if (e.entity.type != EntityType.PLAYER) return
        if ((e.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            && (!SplashBattleUtils.players.contains(e.entity as Player) || !SplashBattleUtils.players.contains(e.damager as Player))
        ) e.isCancelled = true

        if (SplashBattleUtils.players.contains(e.entity as Player) && (e.entity as Player).health - 2 <= 0) damageEvent(
            e.damager as Player,
            e.entity as Player
        )
    }

    @EventHandler
    fun onProjectileDamage(e: ProjectileHitEvent) {

        //Check if entity is snowball and if it hits a player
        if (e.entity.type != EntityType.SNOWBALL) return
        if (e.hitEntity == null) return
        if (e.hitEntity!!.type != EntityType.PLAYER) return
        val snowball = e.entity as Snowball

        //Check if shooter is player
        if (snowball.shooter !is Player) return
        val player = snowball.shooter as Player?
        val target = e.hitEntity as Player?
        if (!SplashBattleUtils.players.contains(player) || !SplashBattleUtils.players.contains(target)) return
        if (player!!.uniqueId === target!!.uniqueId) {
            (player as Audience).sendActionBar(MiniMessage.miniMessage().deserialize("<#36b6ba>Don't attack yourself!"))
            return
        }

        (target as Audience).sendActionBar(MiniMessage.miniMessage().deserialize("<#3671ba>You're being attacked!"))

        damageEvent(player!!, target)
    }

    private fun damageEvent(player: Player, target: Player) {
        if (target.health - 2 <= 0) {
            if (!SplashBattleUtils.players.contains(target)) return
            SplashBattleUtils.dead.add(target)

            player.sendMessage(Colors.format("#365bbaYou soaked ${target.name}, well done! #868686(+1 Soak)"))
            val splashBattle = SplashBattle(SplashBattleUtils.games[player]!!)
            splashBattle.getLeaderboard().addSoaks(player, 1)
            splashBattle.getLeaderboard().spawnSoaksSign()

            target.sendTitle(Colors.format("#365bba&lYou got soaked!"), Colors.format("${player.name} took the last shot!"), 10, 60, 10)
            target.teleport(splashBattle.getRandomSpawnPoint(target.location), TeleportFlag.EntityState.RETAIN_PASSENGERS)
            target.health = 10.0

            return
        }

        SplashBattleUtils.damagePlayer(target, player, 2)
        SplashBattleUtils.playerCombat[target] = System.currentTimeMillis()
        SplashBattleUtils.playerCombat[player] = System.currentTimeMillis()

        player.noDamageTicks = 0
    }

}