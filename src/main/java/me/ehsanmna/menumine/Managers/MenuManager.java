package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.nbt.NBTItem;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MenuManager {


    // Hold itemStack in player inventory
    static ItemStack menu;
    static Inventory inventory;
    public static HashMap<Integer, List<MenuAction>> actionsManager = new HashMap<>();

    static File file;
    static File gui;
    public static YamlConfiguration yml;
    public static YamlConfiguration guiYml;

    public static void setUp() {
        try {
            file = new File(MenuMine.getInstance().getDataFolder(), "Menu.yml");
            gui = new File(MenuMine.getInstance().getDataFolder(), "Guis.yml");
            File messages = new File(MenuMine.getInstance().getDataFolder(), "Messages.yml");
            if (file.createNewFile()) MenuMine.getInstance().saveResource("Menu.yml",true);
            if (gui.createNewFile()) MenuMine.getInstance().saveResource("Guis.yml",true);
            if (messages.createNewFile()) MenuMine.getInstance().saveResource("Messages.yml",true);

            yml = YamlConfiguration.loadConfiguration(file);
            guiYml = YamlConfiguration.loadConfiguration(gui);
        }catch (IOException error){
            error.printStackTrace();
        }
        loadMenu();
        loadMenuModels();
        PlayerManager.loadMessages();
    }

    public static void loadMenu(){
        long time = System.currentTimeMillis();
        actionsManager.clear();
        yml.options().copyDefaults();
        yml = YamlConfiguration.loadConfiguration(file);
        try {
            menu = ItemWrapper.wrapItem(yml,yml.getConfigurationSection("menu"));
        }catch (Exception error){
            menu = new ItemStack(Material.STONE);
        }
        // inventory
        try {
            inventory = Bukkit.createInventory(null, yml.getInt("menu.rows") * 9,MenuMine.color(yml.getString("menu.name")));
        }catch (Exception error){
            inventory = Bukkit.createInventory(null, InventoryType.CHEST, MenuMine.color("&b&lMenu"));
        }
        // filter
        if (yml.contains("menu")){
            for (String itemId : yml.getConfigurationSection("menu.filter").getKeys(false)){
                ConfigurationSection section = yml.getConfigurationSection("menu.filter."+itemId);
                ItemStack item= ItemWrapper.wrapItem(yml,section);
                String type = Objects.requireNonNull(section.getString("type")).toLowerCase();
                switch (type){
                    case "fill":
                        for (int slot = 0; slot < inventory.getSize(); slot++)
                            if (inventory.getItem(slot) == null || inventory.getItem(slot).getType().equals(Material.AIR)) inventory.setItem(slot,item);
                        break;
                    case "slot":
                        for (int slot : section.getIntegerList("slots")) inventory.setItem(slot,item);
                        break;
                }
            }
        }


        for (String itemId : Objects.requireNonNull(yml.getConfigurationSection("menu.items.")).getKeys(false)){
            ConfigurationSection section = yml.getConfigurationSection("menu.items."+itemId);
            ItemStack item = ItemWrapper.wrapItem(yml,section);
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
            inventory.setItem(yml.getInt("menu.items." + itemId + ".slot"),item);
        }
        Bukkit.getServer().getConsoleSender().sendMessage(MenuMine.color("&7[&f"+(System.currentTimeMillis()-time) +"ms&7]&bSuccessfully loaded &3Main model&b."));
    }

    public static void loadMenuModels(){
        long time = System.currentTimeMillis();
        guiYml.options().copyDefaults();
        guiYml = YamlConfiguration.loadConfiguration(gui);

        for (String modelName : guiYml.getKeys(false)){
            ConfigurationSection menuSection = guiYml.getConfigurationSection(modelName);
            MenuModel model = new MenuModel();
            int rows = menuSection.getInt("rows")*9;
            String name = MenuMine.color(menuSection.getString("name"));
            Inventory inventory = Bukkit.createInventory(null,rows,name);
            if (menuSection.contains("filter"))
                for (String filterId : menuSection.getConfigurationSection("filter.").getKeys(false)){
                    ConfigurationSection section = menuSection.getConfigurationSection("filter." + filterId);
                    ItemStack item = ItemWrapper.wrapItem(guiYml,section);
                    try {
                        NBTItem nbt = NBTItemManager.createNBTItem(item);
                        nbt.setTag("MenuItem",true);
                        nbt.setTag("FilterItem",true);
                        nbt.setTag("MenuModel",modelName);
                        nbt.save();
                        item = nbt.getItem();
                    }catch (Exception error){
                        System.out.println("Could not save nbt item in filters!!!");
                    }
                    switch (section.getString("type").toLowerCase()){
                        case "fill":
                            for (int i = 0; i < inventory.getSize();){
                                inventory.setItem(i,item);
                                i++;
                        }
                            break;
                        case "slot":
                            for (int slot: section.getIntegerList("slots")) inventory.setItem(slot,item);
                            break;
                    }
                }

            ConfigurationSection content = menuSection.getConfigurationSection("content");
            for (String itemId : content.getKeys(false)){
                ConfigurationSection itemSection = content.getConfigurationSection(itemId);
                ItemStack item = ItemWrapper.wrapItem(guiYml,itemSection);
                int slot = itemSection.getInt("slot");
                List<String> actionsId = itemSection.getStringList("actions");
                try {
                    NBTItem nbt = NBTItemManager.createNBTItem(item);
                    nbt.setTag("MenuItem",true);
                    nbt.setTag("MenuModel",modelName);
                    nbt.save();
                    item = nbt.getItem();
                }catch (Exception error){
                    System.out.println("Could not load nbt item in "+modelName +"!!!");
                }

                inventory.setItem(slot,item);
                for (String actionId : actionsId){
                    MenuAction action = new MenuAction();
                    String actionEnumId = actionId.split("-")[0];
                    action.act = Action.valueOf(actionEnumId);
                    action.action = actionId.replaceAll(actionEnumId + "-","");
                    model.addAction(slot,action);
                }
            }

            model.setName(modelName);
            model.setDisplayName(name);
            model.setId(modelName);
            model.setInv(inventory);

            MenuModel.addModel(modelName,model);
            Bukkit.getServer().getConsoleSender().sendMessage(MenuMine.color("&7[&f"+(System.currentTimeMillis()-time) +"ms&7]&bLoaded &9"+modelName+"&b menu model."));
        }

    }

    public static void disableMenu(Player player){
        player.getInventory().setItem(yml.getInt("menu.slot"),null);
        Storage.disabledMenus.add(player.getUniqueId());
    }

    public static void enableMenu(Player player){
        setItemToInventory(player);
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
        int slot = yml.getInt("menu.slot");
        NBTItem nbt = NBTItemManager.createNBTItem(menu);
        nbt.setTag("menu","menu");
        nbt.save();
        player.getInventory().setItem(slot,nbt.getItem());
    }
    public static void openModel(String modelName,Player player){
        MenuModel model = MenuModel.getModels().get(modelName);
        if (model == null) {player.sendMessage(MenuMine.color("&cThat menu does not exists.")); return;}
        model.openMenu(player);
    }

    public static void saveMenuModel(MenuModel model){
        guiYml.set(model.getId()+".name",model.getDisplayName());
        guiYml.set(model.getId()+".rows",model.getInv().getSize()/9);
        guiYml.set(model.getId()+".game","This model created from game, please config actions and more...");
        guiYml.createSection(model.getId()+".content");

        int slot = 0;
        for (ItemStack itemStack : model.getInv().getContents()){
            try{
                if (itemStack != null) ItemWrapper.wrapItemToPath(guiYml.getConfigurationSection(model.getId()+".content"),itemStack,slot);
            }catch (NullPointerException e){
                if (MenuMine.logMessages) System.out.println("Error cause of " +e.getCause());
                if (MenuMine.logMessages) System.out.println("> Id: "+model.getId());
            }
            slot++;
        }

        try {
            guiYml.save(gui);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MenuManager.loadMenuModels();


    }
}
