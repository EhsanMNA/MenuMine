package me.ehsanmna.menumine.models;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialMenuModel {

    MenuModel menuModel;
    Map<String, List<Integer>> savedDataListInteger = new HashMap<>();
    Map<String, String> savedDataString = new HashMap<>();
    Map<String, ItemStack> savedDataItemStack = new HashMap<>();


    public SpecialMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public Map<String, List<Integer>> getSavedDataListInteger() {
        return savedDataListInteger;
    }

    public Map<String, String> getSavedDataString() {
        return savedDataString;
    }

    public Map<String, ItemStack> getSavedDataItemStack() {
        return savedDataItemStack;
    }

    public void addData(String key, ItemStack itemStack){
        savedDataItemStack.put(key, itemStack);
    }

    public void addData(String key, String value){
        savedDataString.put(key, value);
    }

    public void addData(String key, List<Integer> value){
        savedDataListInteger.put(key, value);
    }

    public void removeData(String key){
        savedDataListInteger.remove(key);
        savedDataString.remove(key);
        savedDataItemStack.remove(key);
    }
}
