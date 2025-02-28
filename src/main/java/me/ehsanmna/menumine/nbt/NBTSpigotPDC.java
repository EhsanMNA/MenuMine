package me.ehsanmna.menumine.nbt;

import me.ehsanmna.menumine.MenuMine;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTSpigotPDC implements NBTItem {

    private final ItemStack item;
    private final JavaPlugin plugin = MenuMine.getInstance();
    private ItemMeta meta;

    public NBTSpigotPDC(ItemStack item) {
        this.item = item.clone();
        this.meta = item.getItemMeta();
        if (this.meta == null) {
            this.meta = plugin.getServer().getItemFactory().getItemMeta(item.getType());
        }
    }

    @Override
    public ItemStack getItem() {
        save();
        return item.clone();
    }

    @Override
    public boolean hasTag(String tag) {
        NamespacedKey key = getKey(tag);
        return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING) ||
                meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER) ||
                meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE) || // Use BYTE for boolean
                meta.getPersistentDataContainer().has(key, PersistentDataType.FLOAT) ||
                meta.getPersistentDataContainer().has(key, PersistentDataType.DOUBLE);
    }

    @Override
    public void setTag(String key, String value) {
        meta.getPersistentDataContainer().set(getKey(key), PersistentDataType.STRING, value);
    }

    @Override
    public void setTag(String key, int value) {
        meta.getPersistentDataContainer().set(getKey(key), PersistentDataType.INTEGER, value);
    }

    @Override
    public void setTag(String key, boolean value) {
        meta.getPersistentDataContainer().set(getKey(key), PersistentDataType.BYTE, (byte) (value ? 1 : 0));
    }

    @Override
    public void setTag(String key, float value) {
        meta.getPersistentDataContainer().set(getKey(key), PersistentDataType.FLOAT, value);
    }

    @Override
    public void setTag(String key, double value) {
        meta.getPersistentDataContainer().set(getKey(key), PersistentDataType.DOUBLE, value);
    }

    @Override
    public Object getTag(String key) {
        NamespacedKey namespacedKey = getKey(key);

        if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
            return getString(key);
        }
        if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.INTEGER)) {
            return getInt(key);
        }
        if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.BYTE)) {
            return getBoolean(key);
        }
        if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.FLOAT)) {
            return getFloat(key);
        }
        if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) {
            return getDouble(key);
        }
        return null;
    }

    @Override
    public String getString(String key) {
        return meta.getPersistentDataContainer().getOrDefault(getKey(key), PersistentDataType.STRING, "");
    }

    @Override
    public int getInt(String key) {
        return meta.getPersistentDataContainer().getOrDefault(getKey(key), PersistentDataType.INTEGER, 0);
    }

    @Override
    public boolean getBoolean(String key) {
        Byte value = meta.getPersistentDataContainer().getOrDefault(getKey(key), PersistentDataType.BYTE, (byte) 0);
        return value != 0;
    }

    @Override
    public float getFloat(String key) {
        return meta.getPersistentDataContainer().getOrDefault(getKey(key), PersistentDataType.FLOAT, 0.0f);
    }

    @Override
    public double getDouble(String key) {
        return meta.getPersistentDataContainer().getOrDefault(getKey(key), PersistentDataType.DOUBLE, 0.0);
    }

    @Override
    public void save() {
        item.setItemMeta(meta);
    }

    private NamespacedKey getKey(String key) {
        return new NamespacedKey(plugin, key);
    }
}