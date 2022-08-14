package me.ehsanmna.menumine.nbt;

import org.bukkit.inventory.ItemStack;

public interface NBTItem {


    public ItemStack getItem();

    public boolean hasTag(String tag);

    public void setTag(String key,String value);

    public void setTag(String key,int value);

    public void setTag(String key,boolean value);

    public void setTag(String key,float value);

    public void setTag(String key,double value);

    public Object getTag(String key);

    public String getString(String key);

    public int getInt(String key);

    public boolean getBoolean(String key);

    public float getFloat(String key);

    public double getDouble(String key);

    public void save();



}
