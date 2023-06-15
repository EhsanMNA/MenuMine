package me.ehsanmna.menumine.nbt;

import me.ehsanmna.menumine.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBTItemManager {


    public static NBTItem createNBTItem(ItemStack item){
        return getNewItem(item);
    }

    static NBTItem getNewItem(ItemStack item){
        return new NBTReflection(item);
    }

}
