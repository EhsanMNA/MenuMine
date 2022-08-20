package me.ehsanmna.menumine.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTItemManager {


    public static NBTItem createNBTItem(ItemStack item){
        return getNewItem(item);
    }

    static NBTItem getNewItem(ItemStack item){
        /*if (Bukkit.getServer().getVersion().contains("1.17")) i = new NBTv17(item);
        *if (Bukkit.getServer().getVersion().contains("1.16")) i = new NBTv16(item);
        *if (Bukkit.getServer().getVersion().contains("1.12")) i = new NBTv12(item);
        *if (Bukkit.getServer().getVersion().contains("1.8")) i = new NBTv8(item);
        *if (Bukkit.getServer().getVersion().contains("1.9")) i = new NBTv9(item);
        */
        return new NBTReflection(item);
    }




}
