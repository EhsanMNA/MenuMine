package me.ehsanmna.menumine.nbt;

import com.saicone.rtag.RtagItem;
import org.bukkit.inventory.ItemStack;

public class NBTRTag implements NBTItem{

    RtagItem tag;

    public NBTRTag(ItemStack itemStack){
        tag = new RtagItem(itemStack);
    }

    @Override
    public ItemStack getItem() {
        return tag.loadCopy();
    }

    @Override
    public boolean hasTag(String tag) {
        return this.tag.hasTag(tag);
    }

    @Override
    public void setTag(String key, String value) {
        tag.set(value, key);
    }

    @Override
    public void setTag(String key, int value) {
        tag.set(value, key);
    }

    @Override
    public void setTag(String key, boolean value) {
        tag.set(value, key);
    }

    @Override
    public void setTag(String key, float value) {
        tag.set(value, key);
    }

    @Override
    public void setTag(String key, double value) {
        tag.set(value, key);
    }

    @Override
    public Object getTag(String key) {
        return tag;
    }

    @Override
    public String getString(String key) {
        return tag.get(key);
    }

    @Override
    public int getInt(String key) {
        return tag.get(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return tag.get(key);
    }

    @Override
    public float getFloat(String key) {
        return tag.get(key);
    }

    @Override
    public double getDouble(String key) {
        return tag.get(key);
    }

    @Override
    public void save() {
        tag.load();
        tag.update();
    }
}
