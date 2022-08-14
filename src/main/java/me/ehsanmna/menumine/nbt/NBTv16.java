package me.ehsanmna.menumine.nbt;


import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

public class NBTv16 implements NBTItem{

    public NBTv16(org.bukkit.inventory.ItemStack item) {
        this.item = CraftItemStack.asNMSCopy(item);
        tag = this.item.getOrCreateTag();
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
    public String getString(String key) {
        return tag.getString(key);
    }

    @Override
    public int getInt(String key) {
        return tag.getInt(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return tag.getBoolean(key);
    }

    @Override
    public float getFloat(String key) {
        return tag.getFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return tag.getDouble(key);
    }

    @Override
    public void save() {
        item.setTag(tag);
    }

}
