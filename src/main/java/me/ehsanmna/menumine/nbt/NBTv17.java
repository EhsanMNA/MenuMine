package me.ehsanmna.menumine.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;

public class NBTv17 implements NBTItem{

    public NBTv17(org.bukkit.inventory.ItemStack item) {
        this.item =CraftItemStack.asNMSCopy(item);
        this.tag = this.item.hasTag() ? this.item.getTag() : new NBTTagCompound();
    }
    ItemStack item;
    NBTTagCompound tag;


    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(item);
    }

    @Override
    public boolean hasTag(String tag) {
        return this.tag.hasKey(tag);
    }

    @Override
    public void setTag(String key, String value) {
        tag.setString(key,value);
    }

    @Override
    public void setTag(String key, int value) {
        tag.setInt(key,value);
    }

    @Override
    public void setTag(String key, boolean value) {
        tag.setBoolean(key,value);
    }

    @Override
    public void setTag(String key, float value) {
        tag.setFloat(key,value);
    }

    @Override
    public void setTag(String key, double value) {
        tag.setDouble(key,value);
    }

    @Override
    public Object getTag(String key) {
        return tag.get(key);
    }

    @Override
    public void save() {
        item.setTag(tag);
    }
}
