package me.m64diamondstar.ingeniamccore.utils.entities;

import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistry {

    private static final Map<Integer, Class<? extends Entity>> map = new HashMap<>();

    public static void addEntity(Integer id, Class<? extends Entity> entityClass){
        map.put(id, entityClass);
    }

    public static Map<Integer, Class<? extends Entity>> getMap(){
        return map;
    }

    public static boolean containsId(int id){
        return getMap().containsKey(id);
    }

}
