package me.ehsanmna.menumine.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTAPI implements NBTItem{

    de.tr7zw.nbtapi.NBTItem nbti;

    public NBTAPI(ItemStack item){
        nbti = new de.tr7zw.nbtapi.NBTItem(item);
    }

    @Override
    public ItemStack getItem() {
        return nbti.getItem();
    }

    @Override
    public boolean hasTag(String tag) {
        return nbti.hasTag(tag);
    }

    @Override
    public void setTag(String key, String value) {
        nbti.setString(key,value);
    }

    @Override
    public void setTag(String key, int value) {
        nbti.setInteger(key,value);
    }

    @Override
    public void setTag(String key, boolean value) {
        nbti.setBoolean(key,value);
    }

    @Override
    public void setTag(String key, float value) {
        nbti.setFloat(key,value);
    }

    @Override
    public void setTag(String key, double value) {
        nbti.setDouble(key,value);
    }

    @Override
    public Object getTag(String key) {
        return nbti;
    }

    @Override
    public String getString(String key) {
        return nbti.getString(key);
    }

    @Override
    public int getInt(String key) {
        return nbti.getInteger(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return nbti.getBoolean(key);
    }

    @Override
    public float getFloat(String key) {
        return nbti.getFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return nbti.getDouble(key);
    }

    @Override
    public void save() {
        nbti.applyNBT(nbti.getItem());
    }
}
