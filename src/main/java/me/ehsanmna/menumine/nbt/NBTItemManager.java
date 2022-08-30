package me.ehsanmna.menumine.nbt;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBTItemManager {

    public static NBTItem createNBTItem(ItemStack item){
        return getNewItem(item);
    }

    static NBTItem getNewItem(ItemStack item){
        NBTItem i = null;
        if (Bukkit.getServer().getVersion().contains("1.17")) i = new NBTv17(item);
        else if (Bukkit.getServer().getVersion().contains("1.16")) i = new NBTv16(item);
        else if (Bukkit.getServer().getVersion().contains("1.12")) i = new NBTv12(item);
        else if (Bukkit.getServer().getVersion().contains("1.8")) i = new NBTv8(item);
        else if (Bukkit.getServer().getVersion().contains("1.9")) i = new NBTv9(item);
        else i = new NBTReflection(item);

        return i;
    }

}
