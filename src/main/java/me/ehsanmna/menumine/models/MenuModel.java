package me.ehsanmna.menumine.models;

import me.ehsanmna.menumine.Managers.MenuAction;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.utils.XSound;
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

    private static HashMap<String,MenuModel> models = new HashMap<>();

    Inventory inv;
    String id;
    String name;
    String displayName;
    HashMap<Integer, ArrayList<MenuAction>> actions = new HashMap<>();
    XSound openSound;

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
        if (openSound != null) openSound.play(player);
        if (Storage.papiUse){
            int slot = 0;
            for (ItemStack item : inv.getContents()){
                if (item != null || !item.getType().equals(Material.AIR)){
                    ItemMeta meta = item.getItemMeta();
                    String name = meta.getDisplayName();
                    meta.setDisplayName(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player,name));
                    try{
                        int lo = 0;
                        List<String> lore = meta.getLore();
                        for (String l : lore){lore.set(lo,me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player,l));lo++;}
                        meta.setLore(lore);
                    }catch (Exception ignored){}
                    item.setItemMeta(meta);
                    inv.setItem(slot,item);
                }
                slot++;
            }
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

    public ArrayList<MenuAction> getActions(int slot){
        return actions.get(slot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuModel menuModel = (MenuModel) o;
        return Objects.equals(id, menuModel.id) && Objects.equals(name, menuModel.name) && Objects.equals(displayName, menuModel.displayName);
    }
}
