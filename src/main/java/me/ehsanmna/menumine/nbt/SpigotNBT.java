package me.ehsanmna.menumine.nbt;

import me.ehsanmna.menumine.MenuMine;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class SpigotNBT implements NBTItem{

    Map<NamespacedKey,String> nbtMapString = new HashMap<>();
    Map<NamespacedKey,Integer> nbtMapInt = new HashMap<>();
    Map<NamespacedKey,Boolean> nbtMapBool = new HashMap<>();
    Map<NamespacedKey,Double> nbtMapDouble = new HashMap<>();
    Map<NamespacedKey,Float> nbtMapFloat = new HashMap<>();
    ItemStack itemStack;
    MenuMine main = MenuMine.getInstance();

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public boolean hasTag(String tag) {
        NamespacedKey keyTag = new NamespacedKey(main, tag);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (container.has(keyTag , PersistentDataType.STRING)) return true;
        if (container.has(keyTag , PersistentDataType.DOUBLE)) return true;
        if (container.has(keyTag , PersistentDataType.FLOAT)) return true;
        if (container.has(keyTag , PersistentDataType.INTEGER)) return true;
        return false;
    }

    @Override
    public void setTag(String key, String value) {
        nbtMapString.put(new NamespacedKey(main, key),value);
    }

    @Override
    public void setTag(String key, int value) {
        nbtMapInt.put(new NamespacedKey(main, key),value);
    }

    @Override
    public void setTag(String key, boolean value) {
        nbtMapBool.put(new NamespacedKey(main, key),value);
    }

    @Override
    public void setTag(String key, float value) {
        nbtMapFloat.put(new NamespacedKey(main, key),value);
    }

    @Override
    public void setTag(String key, double value) {
        nbtMapDouble.put(new NamespacedKey(main, key),value);
    }

    @Override
    public Object getTag(String key) {
        return null;
    }

    @Override
    public String getString(String key) {
        NamespacedKey keyTag = new NamespacedKey(main, key);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(keyTag , PersistentDataType.STRING)) return container.get(keyTag, PersistentDataType.STRING);
        return null;
    }

    @Override
    public int getInt(String key) {
        NamespacedKey keyTag = new NamespacedKey(main, key);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(keyTag , PersistentDataType.INTEGER)) return container.get(keyTag, PersistentDataType.INTEGER);
        return 0;
    }

    @Override
    public boolean getBoolean(String key) {
        NamespacedKey keyTag = new NamespacedKey(main, key);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(keyTag , PersistentDataType.INTEGER)) return container.get(keyTag, PersistentDataType.INTEGER).equals(1);
        return false;
    }

    @Override
    public float getFloat(String key) {
        NamespacedKey keyTag = new NamespacedKey(main, key);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(keyTag , PersistentDataType.FLOAT)) return container.get(keyTag, PersistentDataType.FLOAT);
        return 0;
    }

    @Override
    public double getDouble(String key) {
        NamespacedKey keyTag = new NamespacedKey(main, key);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(keyTag , PersistentDataType.DOUBLE)) return container.get(keyTag, PersistentDataType.DOUBLE);
        return 0;
    }

    @Override
    public void save() {
        ItemMeta meta = itemStack.getItemMeta();
        try {
            assert  meta != null;
            for (NamespacedKey key : nbtMapString.keySet()) meta.getPersistentDataContainer().set(key, PersistentDataType.STRING,nbtMapString.get(key));
            for (NamespacedKey key : nbtMapInt.keySet()) meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER,nbtMapInt.get(key));
            for (NamespacedKey key : nbtMapFloat.keySet()) meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT,nbtMapFloat.get(key));
            for (NamespacedKey key : nbtMapDouble.keySet()) meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE,nbtMapDouble.get(key));
            for (NamespacedKey key : nbtMapBool.keySet())
                if (nbtMapBool.get(key)) meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER,1);
                else meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER,0);

            itemStack.setItemMeta(meta);
        }catch (Exception ignored){}

    }
}
