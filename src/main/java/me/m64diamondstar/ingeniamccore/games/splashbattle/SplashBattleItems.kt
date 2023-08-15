package me.m64diamondstar.ingeniamccore.games.splashbattle

import me.m64diamondstar.ingeniamccore.IngeniaMC
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.persistence.PersistentDataType

object SplashBattleItems {

    fun getInvisibleBalloon(): ItemStack {
        val waterBalloon = ItemStack(Material.FEATHER)
        val waterBalloonMeta = waterBalloon.itemMeta!!

        waterBalloonMeta.setDisplayName(Colors.format("#3393D4&lWater Balloon"))
        waterBalloonMeta.setCustomModelData(1)
        waterBalloon.itemMeta = waterBalloonMeta

        return waterBalloon
    }

    fun getWaterBalloon(): ItemStack {
        val waterBalloon = ItemStack(Material.SNOWBALL)
        val waterBalloonMeta = waterBalloon.itemMeta!!

        waterBalloonMeta.setDisplayName(Colors.format("#3393D4&lWater Balloon"))
        waterBalloonMeta.setCustomModelData(1)
        waterBalloon.itemMeta = waterBalloonMeta

        return waterBalloon
    }

    fun getWaterBalloonStack(): ItemStack {
        val waterBalloon = ItemStack(Material.SNOWBALL)
        val waterBalloonMeta = waterBalloon.itemMeta!!

        waterBalloonMeta.setDisplayName(Colors.format("#3393D4&lWater Balloon"))
        waterBalloonMeta.setCustomModelData(1)
        waterBalloon.itemMeta = waterBalloonMeta
        waterBalloon.amount = 16

        return waterBalloon
    }

    fun getWaterPistol(): ItemStack {
        val waterPistol = ItemStack(Material.CROSSBOW)
        val waterPistolMeta = waterPistol.itemMeta as CrossbowMeta

        waterPistolMeta.addChargedProjectile(ItemStack(Material.ARROW))
        waterPistolMeta.setDisplayName(Colors.format("#3393D4&lWater Pistol"))
        waterPistolMeta.lore = listOf(Colors.format("#868686Pew Pew"))
        waterPistolMeta.setCustomModelData(1)

        for(flag in ItemFlag.values())
            waterPistolMeta.addItemFlags(flag)

        waterPistol.itemMeta = waterPistolMeta

        return waterPistol
    }

    fun getWaterGun(): ItemStack {
        val waterGun = ItemStack(Material.CROSSBOW)
        val waterGunMeta = waterGun.itemMeta as CrossbowMeta

        waterGunMeta.addChargedProjectile(ItemStack(Material.ARROW))
        waterGunMeta.setDisplayName(Colors.format("#3393D4&lWater Gun"))
        waterGunMeta.lore = listOf(Colors.format("#868686More Pew Pew"))
        waterGunMeta.setCustomModelData(2)

        for(flag in ItemFlag.values())
            waterGunMeta.addItemFlags(flag)

        waterGun.itemMeta = waterGunMeta

        return waterGun
    }

    fun getWaterAmmoEmpty(): ItemStack {
        val waterAmmo = ItemStack(Material.BUCKET)
        val waterAmmoMeta = waterAmmo.itemMeta!!

        waterAmmoMeta.setDisplayName(Colors.format("#3393D4&lWater Ammo &r#868686(Empty)"))
        waterAmmoMeta.lore = listOf(Colors.format("#868686Shift while looking at water to refill"))
        waterAmmoMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "ammo"), PersistentDataType.INTEGER, 0)
        waterAmmo.itemMeta = waterAmmoMeta

        return waterAmmo
    }

    fun getWaterAmmoFilled(level: Int): ItemStack {
        if(level <= 0)
            return getWaterAmmoEmpty()

        val waterAmmo = ItemStack(Material.WATER_BUCKET)
        val waterAmmoMeta = waterAmmo.itemMeta!!

        waterAmmoMeta.setDisplayName(Colors.format("#3393D4&lWater Ammo &r#868686$level/10"))
        waterAmmoMeta.lore = listOf(Colors.format("#868686Shift while looking at water to refill"))
        waterAmmoMeta.persistentDataContainer.set(NamespacedKey(IngeniaMC.plugin, "ammo"), PersistentDataType.INTEGER, level)

        for(flag in ItemFlag.values())
            waterAmmoMeta.addItemFlags(flag)

        waterAmmo.itemMeta = waterAmmoMeta

        if(level >= 10)
            waterAmmo.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
        waterAmmo.amount = level

        return waterAmmo
    }
}