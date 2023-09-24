package me.ehsanmna.menumine.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTItemManager {

    public static String nbtSystem = "madeIn";

    public static NBTItem createNBTItem(ItemStack item){
        return getNewItem(item);
    }

    static NBTItem getNewItem(ItemStack item){
        if (nbtSystem.equalsIgnoreCase("madeIn"))
            return new NBTReflection(item);
        else if (nbtSystem.equalsIgnoreCase("RTag"))
            return new NBTRTag(item);
        else if (nbtSystem.equalsIgnoreCase("NBTAPI"))
            return new NBTAPI(item);

        return new NBTReflection(item);
    }

}
