package me.ehsanmna.menumine.models;

import me.ehsanmna.menumine.Managers.MenuAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuModel {

    private static HashMap<String,MenuModel> models = new HashMap<>();

    Inventory inv;
    String id;
    String name;
    String displayName;
    HashMap<Integer, ArrayList<MenuAction>> actions = new HashMap<>();

    public static HashMap<String, MenuModel> getModels() {
        return models;
    }

    public static void addModel(String name,MenuModel model) {
        models.put(name,model);
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void openMenu(Player player){
        player.openInventory(inv);
    }

    public void addAction(int slot, MenuAction action){
        if (!actions.containsKey(slot)){
            ArrayList<MenuAction> listOfActions = new ArrayList<>();
            listOfActions.add(action);
            actions.put(slot,listOfActions);
        }else actions.get(slot).add(action);

    }

    public ArrayList<MenuAction> getActions(int slot){
        return actions.get(slot);
    }




}
