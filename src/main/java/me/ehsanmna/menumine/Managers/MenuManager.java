package me.ehsanmna.menumine.Managers;

import de.tr7zw.nbtapi.NBTItem;
import me.ehsanmna.menumine.MenuMine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class MenuManager {

    static ItemStack menu;
    static Inventory inventory;
    public static HashMap<Integer, List<MenuAction>> actionsManager = new HashMap<>();

    static File file;
    public static YamlConfiguration yml;

    public static void setUp(){
        file = new File(MenuMine.getInstance().getDataFolder(), "Menu.yml");
        if (!file.exists()) {
            MenuMine.getInstance().saveResource("Menu.yml",false);
        }
        yml = YamlConfiguration.loadConfiguration(file);
    }

    public static void loadMenu(){
        yml.options().copyDefaults();
        try {
            MenuMine.getInstance().saveResource("Menu.yml",false);
            yml.save(file);
        } catch (IOException ignored) {}
        try {
            menu = new ItemStack(Material.valueOf(yml.getString("menu.material")));
        }catch (Exception error){
            menu = new ItemStack(Material.STONE);
        }
        ItemMeta meta = menu.getItemMeta();
        assert meta != null;
        meta.setDisplayName(MenuMine.color(yml.getString("menu.itemName")));
        meta.setLore(MenuMine.color(yml.getStringList("menu.itemlore")));
        menu.setItemMeta(meta);
        NBTItem nbt = new NBTItem(menu);
        nbt.setString("menu","menu");
        nbt.applyNBT(menu);
        // inventory
        inventory = Bukkit.createInventory(null, yml.getInt("menu.rows") * 9,MenuMine.color(yml.getString("menu.name")));
        for (String itemId : Objects.requireNonNull(yml.getConfigurationSection("menu.items.")).getKeys(false)){
            ItemStack item = null;
            try {
                item = new ItemStack(Material.valueOf(yml.getString("menu.items." + itemId + ".material")));
            }catch (Exception error){
                item = new ItemStack(Material.STONE);
            }

            ItemMeta m = item.getItemMeta();
            assert m != null;
            m.setDisplayName(MenuMine.color(yml.getString("menu.items." + itemId + ".name")));
            m.setLore(MenuMine.color(yml.getStringList("menu.items." + itemId + ".lore")));
            for (String action : yml.getStringList("menu.items." + itemId + ".actions")){
                String a = "CANCEL";
                if (action.contains("-")) a = action.split("-")[0];
                Action act = Action.valueOf(a);
                MenuAction action1 = new MenuAction();
                action1.act = act;
                action1.action = action.strip().replaceAll(a + "-", "");
                if (actionsManager.containsKey(yml.getInt("menu.items." + itemId + ".slot"))){
                    actionsManager.get(yml.getInt("menu.items." + itemId + ".slot")).add(action1);
                }else {
                    List<MenuAction> actions = new ArrayList<>();
                    actions.add(action1);
                    actionsManager.put(yml.getInt("menu.items." + itemId + ".slot"),actions);
                }
            }
            item.setItemMeta(m);
            inventory.setItem(yml.getInt("menu.items." + itemId + ".slot"),item);
        }
    }

    public static void disableMenu(Player player){
        player.getInventory().setItem(yml.getInt("menu.slot"),null);
        Storage.disabledMenus.add(player.getUniqueId());
    }

    public static void enableMenu(Player player){
        player.getInventory().setItem(yml.getInt("menu.slot"),menu);
        Storage.disabledMenus.remove(player.getUniqueId());
    }

    public static boolean isMenuDisabled(Player player){
        return Storage.disabledMenus.contains(player.getUniqueId());
    }

    public static void open(Player player){
        player.openInventory(inventory);
    }

    public static ItemStack getMenuItem(){
        return menu;
    }

    public static Inventory getGUI(){
        return inventory;
    }

    public static void setItemToInventory(Player player){
        player.getInventory().setItem(yml.getInt("menu.slot"),menu);
    }
}
