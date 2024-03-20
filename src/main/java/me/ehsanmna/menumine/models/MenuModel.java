package me.ehsanmna.menumine.models;

import me.ehsanmna.menumine.Managers.MenuAction;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MenuModel {

    private static final HashMap<String,MenuModel> models = new HashMap<>();

    boolean copy = false;
    Inventory inv;
    String id;
    String name;
    String displayName;
    HashMap<Integer, ArrayList<MenuAction>> actions = new HashMap<>();
    HashMap<Integer, ArrayList<MenuAction>> actionsDeny = new HashMap<>();

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

    public HashMap<Integer, ArrayList<MenuAction>> getActions() {
        return actions;
    }

    public void setActions(HashMap<Integer, ArrayList<MenuAction>> actions) {
        this.actions = actions;
    }

    public HashMap<Integer, ArrayList<MenuAction>> getActionsDeny() {
        return actionsDeny;
    }

    public void setActionsDeny(HashMap<Integer, ArrayList<MenuAction>> actionsDeny) {
        this.actionsDeny = actionsDeny;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }
    public void openMenu(Player player){
        if (copy || Storage.papiUse){
            Inventory cloneInv = Bukkit.createInventory(null,inv.getSize(),displayName);
            int slot = 0;
            for (ItemStack i : inv.getContents()){
                if (i != null && !i.getType().equals(Material.AIR)){
                    ItemStack item = i.clone();
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    String name = meta.getDisplayName();
                    try {if (Storage.papiUse) meta.setDisplayName(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player,name));
                    }catch (NoClassDefFoundError e){Storage.papiUse = false;
                        if (MenuMine.logMessages) Bukkit.getConsoleSender().sendMessage(MenuMine.color("&c[MenuMine] &fCould not detect &c&nPlaceHolderAPI&f!"));}
                    try{
                        int lo = 0;
                        List<String> lore = meta.getLore();
                        assert lore != null;
                        if (Storage.papiUse) for (String l : lore){lore.set(lo,me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player,l));lo++;}
                        else for (String l : lore){lore.set(lo,l);lo++;}
                        meta.setLore(lore);
                    }catch (Exception ignored){}
                    item.setItemMeta(meta);
                    cloneInv.setItem(slot,item);
                }
                slot++;
            }
            player.openInventory(cloneInv);
            return;
        }
        player.openInventory(inv);
    }

    public void addAction(int slot, MenuAction action){
        if (!actions.containsKey(slot)){
            ArrayList<MenuAction> listOfActions = new ArrayList<>();
            listOfActions.add(action);
            actions.put(slot,listOfActions);
        }else actions.get(slot).add(action);
    }

    public void addDenyAction(int slot, MenuAction action){
        if (!actionsDeny.containsKey(slot)){
            ArrayList<MenuAction> listOfActions = new ArrayList<>();
            listOfActions.add(action);
            actionsDeny.put(slot,listOfActions);
        }else actionsDeny.get(slot).add(action);
    }

    public ArrayList<MenuAction> getActions(int slot){
        return actions.get(slot);
    }
    public ArrayList<MenuAction> getDenyActions(int slot){
        return actionsDeny.get(slot);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuModel menuModel = (MenuModel) o;
        return Objects.equals(id, menuModel.id) && Objects.equals(name, menuModel.name) && Objects.equals(displayName, menuModel.displayName);
    }
}
