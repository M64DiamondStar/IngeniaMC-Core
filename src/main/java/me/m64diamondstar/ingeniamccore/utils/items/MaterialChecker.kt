package me.m64diamondstar.ingeniamccore.utils.items

import org.bukkit.Material

class MaterialChecker(material: Material) {

    private val material: Material

    init{
        this.material = material
    }

    fun isFlower(): Boolean{

        if(material == Material.DANDELION
            || material == Material.POPPY
            || material == Material.BLUE_ORCHID
            || material == Material.ALLIUM
            || material == Material.AZURE_BLUET
            || material == Material.ORANGE_TULIP
            || material == Material.PINK_TULIP
            || material == Material.RED_TULIP
            || material == Material.WHITE_TULIP
            || material == Material.OXEYE_DAISY
            || material == Material.CORNFLOWER
            || material == Material.LILY_OF_THE_VALLEY
            || material == Material.WITHER_ROSE
            || material == Material.SUNFLOWER
            || material == Material.LILAC
            || material == Material.ROSE_BUSH
            || material == Material.PEONY)
            return true

        return false
    }

}