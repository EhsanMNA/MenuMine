package me.ehsanmna.menumine.nbt;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBTItemManager {

    public static boolean useSpigotAPI = false;

    public static NBTItem createNBTItem(ItemStack item){
        return getNewItem(item);
    }

    static NBTItem getNewItem(ItemStack item){
        NBTItem i = null;
        String version = Bukkit.getServer().getVersion();
        if (version.contains("1.17")) if (useSpigotAPI) i = new SpigotNBT(); else i = new NBTv17(item);
        else if (version.contains("1.16")) if (useSpigotAPI) i = new SpigotNBT(); else i = new NBTv16(item);
        else if (version.contains("1.12")) i = new NBTv12(item);
        else if (version.contains("1.9")) i = new NBTv9(item);
        else if (version.contains("1.8")) i = new NBTv8(item);

        if (i == null) if (useSpigotAPI) i = new SpigotNBT(); else i = new NBTReflection(item);

        return i;
    }

}
