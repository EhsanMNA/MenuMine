package me.ehsanmna.menumine.Managers.controller;

import me.ehsanmna.menumine.Managers.ItemWrapper;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.models.SpecialMenuModel;
import me.ehsanmna.menumine.utils.addons.MenuMineStorageAddon;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SpecialMenuManager {

    public static Map<String, SpecialMenuModel> specialMenuModelMap = new HashMap<>();

    public static void setup(ConfigurationSection section, MenuModel menuModel) {
        SpecialMenuModel specialMenuModel = new SpecialMenuModel(menuModel);
        ConfigurationSection specialSection = section.getConfigurationSection("special");
        for (String key : specialSection.getKeys(false)) {
            if (specialSection.isList(key)) specialMenuModel.addData(key,specialSection.getIntegerList(key));
            else if (specialSection.isString(key)) specialMenuModel.addData(key,specialSection.getString(key));
            else {
                ConfigurationSection specialSection2 = specialSection.getConfigurationSection(key);
                for (String key2 : specialSection2.getKeys(false)){
                    ItemStack itemStack = ItemWrapper.wrapItem(specialSection2.getConfigurationSection(key2));
                    specialMenuModel.addData(key2,itemStack);
                }
            }
        }
        specialMenuModelMap.put(menuModel.getId(), specialMenuModel);
    }

    public static void openSpecialMenu(Player player, String name){
    }
}
