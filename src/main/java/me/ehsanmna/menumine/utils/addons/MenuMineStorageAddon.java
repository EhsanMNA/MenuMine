package me.ehsanmna.menumine.utils.addons;

import me.ehsanmna.menuMineStorage.api.StorageAPI;
import me.ehsanmna.menuMineStorage.models.StorageInventory;
import me.ehsanmna.menumine.Managers.controller.SpecialMenuManager;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.SpecialMenuModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuMineStorageAddon {

    public static boolean enabled = false;

    public static void initialize() {
        enabled = MenuMine.getInstance().getConfig().getBoolean("addons.MenuMineStorage",
                Bukkit.getPluginManager().getPlugin("MenuMineStorage") != null);
    }

    public static void openGUIofStorage(Player player) {
        SpecialMenuModel specialMenuModel = SpecialMenuManager.specialMenuModelMap.get("storage");
        List<Integer> storageSlots = specialMenuModel.getSavedDataListInteger().get("storageSlots");
        ItemStack lock = specialMenuModel.getSavedDataItemStack().get("lock").clone();
        ItemStack unlock = specialMenuModel.getSavedDataItemStack().get("unlock").clone();
        int i = 0;
        List<StorageInventory> lockStorageInventoryList = new ArrayList<>();
        for(StorageInventory storageInventory : StorageAPI.getPlayerStorage(player).getStorages()){
            if (!StorageAPI.canAccessStorage(player, storageInventory.getId())){
                lockStorageInventoryList.add(storageInventory);
                continue;
            }
            i = getI(specialMenuModel, unlock, i, storageInventory);
        }
        for (StorageInventory storageInventory : lockStorageInventoryList) {
            i = getI(specialMenuModel, lock, i, storageInventory);
        }
    }

    private static int getI(SpecialMenuModel specialMenuModel, ItemStack unlock, int i, StorageInventory storageInventory) {
        ItemStack item = unlock.clone();
        item.setAmount(i+1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(item.getItemMeta().getDisplayName().replace("<storageName>",storageInventory.getId()));
        item.setItemMeta(itemMeta);
        specialMenuModel.getMenuModel().getInv().setItem(i, item);
        i++;
        return i;
    }

}
