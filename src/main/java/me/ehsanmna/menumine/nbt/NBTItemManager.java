package me.ehsanmna.menumine.nbt;

import me.ehsanmna.menumine.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBTItemManager {

    public static boolean useSpigotAPI = false;

    public static NBTItem createNBTItem(ItemStack item){
        return getNewItem(item);
    }

    static NBTItem getNewItem(ItemStack item){
        //NBTItem i = null;
        //String version = Bukkit.getServer().getVersion();
        return new NBTReflection(item);
    }

}
