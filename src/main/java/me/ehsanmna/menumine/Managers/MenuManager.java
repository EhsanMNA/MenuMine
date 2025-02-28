package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.Managers.controller.PlayerMenuController;
import me.ehsanmna.menumine.Managers.controller.SpecialMenuManager;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.commands.CustomMenuCommand;
import me.ehsanmna.menumine.models.Action;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.models.PMenuModel;
import me.ehsanmna.menumine.models.SpecialMenuModel;
import me.ehsanmna.menumine.nbt.NBTItem;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import me.ehsanmna.menumine.utils.xseries.XItemStack;
import me.ehsanmna.menumine.utils.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static me.ehsanmna.menumine.MenuMine.color;

public class MenuManager {


    // Hold itemStack in player inventory
    //static Map<Integer,ItemStack> inventoryItems = new HashMap<>();
    public static ItemStack main;
    static Inventory inventory;
    public static Map<Integer, List<MenuAction>> actionsManager = new HashMap<>();
    public static Map<String,MenuModel> commandsToMenu = new HashMap<>();
    public static int slot = 8;

    static File file;
    static File gui;
    static File playerMenu;
    public static YamlConfiguration yml;
    public static YamlConfiguration guiYml;
    public static YamlConfiguration pMYml;

    public static void setUp() {
        try {
            file = new File(MenuMine.getInstance().getDataFolder(), "Menu.yml");
            gui = new File(MenuMine.getInstance().getDataFolder(), "Guis.yml");
            playerMenu = new File(MenuMine.getInstance().getDataFolder(), "PlayerMenu.yml");
            File messages = new File(MenuMine.getInstance().getDataFolder(), "Messages.yml");
            if (file.createNewFile()) MenuMine.getInstance().saveResource("Menu.yml",true);
            if (gui.createNewFile()) MenuMine.getInstance().saveResource("Guis.yml",true);
            if (playerMenu.createNewFile()) MenuMine.getInstance().saveResource("PlayerMenu.yml",true);
            if (messages.createNewFile()) MenuMine.getInstance().saveResource("Messages.yml",true);

            yml = YamlConfiguration.loadConfiguration(file);
            guiYml = YamlConfiguration.loadConfiguration(gui);
            pMYml = YamlConfiguration.loadConfiguration(playerMenu);
        }catch (IOException error){error.printStackTrace();}
        loadMenu();
        loadMenuModels();
        // loadPMenuModels();
        PlayerManager.loadMessages();
        slot = yml.getInt("menu.slot");
    }

    public static void loadMenu(){
        //long time = System.currentTimeMillis();
        actionsManager.clear();
        yml.options().copyDefaults();
        yml = YamlConfiguration.loadConfiguration(file);
        try {main = ItemWrapper.wrapItem(yml.getConfigurationSection("menu"));
        }catch (Exception error){main = new ItemStack(Material.STONE);}
        /*
        for (String itemId : yml.getKeys(false)){
            ConfigurationSection itemSection = yml.getConfigurationSection(itemId);
            ItemStack itemStack = ItemWrapper.wrapItem(itemSection);
            if (itemSection.contains("toggle")) {main = itemStack; continue;}
            inventoryItems.put(itemSection.getInt("slot"), itemStack);
        }*/
        MenuMine.mainActions.clear();
        for (String actionId : yml.getConfigurationSection("menu").getStringList("actions")){
            MenuAction action = new MenuAction();
            String actionEnumId = actionId.split("-")[0];
            action.act = Action.valueOf(actionEnumId);
            action.action = actionId.replaceAll(actionEnumId + "-","");
            MenuMine.mainActions.add(action);
        }
        if (yml.contains("menu.moveItem")) Storage.moveItem = yml.getBoolean("menu.moveItem");
        if (yml.contains("menu.dropItem")) Storage.dropItem = yml.getBoolean("menu.dropItem");
    }

    public static void loadMenuModels(){
        long time = System.currentTimeMillis();
        MenuModel.getModels().clear();
        guiYml.options().copyDefaults();
        guiYml = YamlConfiguration.loadConfiguration(gui);

        for (String modelName : guiYml.getKeys(false)){
            ConfigurationSection menuSection = guiYml.getConfigurationSection(modelName);
            MenuModel model = new MenuModel();
            int rows = menuSection.getInt("rows")*9;
            String name = MenuMine.color(menuSection.getString("name"));
            Inventory inventory = Bukkit.createInventory(null,rows,name);
            try {
                model.setItemMove(menuSection.getBoolean("moveItems",false));
                if (menuSection.contains("filter"))
                    for (String filterId : menuSection.getConfigurationSection("filter.").getKeys(false)){
                        ConfigurationSection section = menuSection.getConfigurationSection("filter." + filterId);
                        ItemStack item = ItemWrapper.wrapItem(section);
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
            }catch (Exception ignored){}
            model.setSpecialMenu(menuSection.getBoolean("specialMenu",false));
            if (!menuSection.getBoolean("pMenu", false) && !model.isSpecialMenu()){
                ConfigurationSection content = menuSection.getConfigurationSection("content");
                if (content != null)
                    for (String itemId : content.getKeys(false)){
                        ConfigurationSection itemSection = content.getConfigurationSection(itemId);
                        ItemStack item = ItemWrapper.wrapItem(itemSection);
                        int slot = itemSection.getInt("slot");
                        List<String> actionsId = itemSection.getStringList("actions");
                        List<String> denyActionsId = itemSection.getStringList("denyActions");
                        try {
                            NBTItem nbt = NBTItemManager.createNBTItem(item);
                            nbt.setTag("MenuItem",true);
                            nbt.setTag("MenuModel",modelName);
                            if ((itemSection.getString("material")).contains("<arg")) nbt.setTag("Material",itemSection.getString("material"));
                            nbt.save();
                            item = nbt.getItem();
                        }catch (Exception error){System.out.println("Could not load nbt item in "+modelName +"!!!");}

                        inventory.setItem(slot,item);
                        for (String actionId : actionsId)
                            model.addAction(slot,buildAction(actionId));
                        for (String actionId : denyActionsId)
                            model.addDenyAction(slot,buildAction(actionId));
                    }
            }

            model.setName(modelName);
            model.setDisplayName(name);
            model.setId(modelName);
            model.setInv(inventory);
            if (menuSection.contains("copy")) model.setCopy(menuSection.getBoolean("copy"));
            if (menuSection.contains("requires"))
                for (String actionString : menuSection.getStringList("requires"))
                    model.addRequire(buildAction(actionString));
            if (menuSection.contains("notAllow"))
                for (String actionString : menuSection.getStringList("notAllow"))
                    model.addNowAllowAction(buildAction(actionString));

            try {model.setOpenSound(XSound.valueOf(menuSection.getString("openSound")).parseSound());
            }catch (Exception ignored){}

            if (menuSection.contains("command")) {
                String command = menuSection.getString("command");
                commandsToMenu.put(command,model);
                CommandRegManager.registerCommand(command,new CustomMenuCommand());
            }

            MenuModel.addModel(modelName,model);
            // if (model.isSpecialMenu()) SpecialMenuManager.setup(menuSection,model);
            Bukkit.getServer().getConsoleSender().sendMessage(MenuMine.color("&7[&f"+(System.currentTimeMillis()-time) +"ms&7]&bLoaded &9"+modelName+"&b menu model."));
        }

    }

    public static void loadPMenuModels(){
        MenuMine.getInstance().getLogger().warning("Player menus are currently in development.");
        long time = System.currentTimeMillis();
        int n = 0;

        for (String id : pMYml.getKeys(false)){
            UUID uuid = UUID.fromString(id);
            for (String menuId : pMYml.getConfigurationSection(id).getKeys(false)){
                MenuModel model = (MenuModel) MenuModel.getModels().get(menuId).clone();
                ConfigurationSection menuSection = pMYml.getConfigurationSection(id+"."+menuId);
                for (String slotNumber : menuSection.getConfigurationSection("items").getKeys(false)){
                    int slot = Integer.parseInt(slotNumber);
                    try {
                        ItemStack item = XItemStack.deserialize(Objects.requireNonNull(menuSection.getConfigurationSection("items."+slotNumber)));
                        model.getInv().setItem(slot,item);
                    }catch (NullPointerException e){
                        MenuMine.getInstance().getLogger().warning("Cannot deserialize item from a configuration section.");
                    }
                }
                PMenuModel pMenuModel = PlayerMenuController.setupPMenu(uuid);
                pMenuModel.addModel(model);
                n++;
            }
        }
        Bukkit.getServer().getConsoleSender().sendMessage(MenuMine.color("&7[&f"+(System.currentTimeMillis()-time) +"&7]&bLoaded &9"+n+"&b players menu model."));
    }

    public static void disableMenu(Player player){
        player.getInventory().setItem(yml.getInt("menu.slot"),null);
        Storage.disabledMenus.add(player.getUniqueId());
    }

    public static void enableMenu(Player player){
        if (!MenuMine.menuItem) return;
        setItemToInventory(player);
        Storage.disabledMenus.remove(player.getUniqueId());
    }

    public static boolean isMenuDisabled(Player player){
        return Storage.disabledMenus.contains(player.getUniqueId());
    }

    public static void open(Player player){
        openModel("main",player);
    }

    public static ItemStack getMenuItem(){
        return main;
    }

    public static Inventory getGUI(){
        return inventory;
    }

    public static void setItemToInventory(Player player){
        NBTItem nbt = NBTItemManager.createNBTItem(main);
        nbt.setTag("menu","menu");
        nbt.save();
        player.getInventory().setItem(slot,nbt.getItem());
    }

    //public static MenuModel cloneMenuModel(MenuModel model){
    //    MenuModel newMenuModel = new MenuModel();
    //    newMenuModel = (MenuModel) model.clone();
    //    return newMenuModel;
    //}

    public static void openModel(MenuModel model,Player player,List<String> inputs){
        if (model == null) {player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).menuExist)); return;}

        boolean notAllow = false;
        for (MenuAction action : model.getRequireActions())
            if (!action.run(player,null)) {notAllow = true; break;}

        if (notAllow) {
            for (MenuAction action : model.getNotAllowActions())
                action.run(player,null);
            return;
        }

        if (model.isPlayerMenu())
            try {
                if (MenuMine.debug) Bukkit.getServer().getConsoleSender().sendMessage(color("&f[MenuMine] Opening a PMenu for "+player.getUniqueId()));
                PlayerMenuController.openMenuModel(player,model.getId(),inputs);
                return;
            }catch (Exception e){e.printStackTrace();}

        if (model.isSpecialMenu()){
            SpecialMenuManager.openSpecialMenu(player,model.getId());
            return;
        }

        model.openMenu(player,inputs);
    }
    public static void openModel(String modelName,Player player){
        MenuModel model = MenuModel.getModels().get(modelName);
        if (model == null) {player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).menuExist)); return;}
        openModel(MenuModel.getModels().get(modelName),player,List.of());
    }

    public static void openModel(String modelName,Player player,List<String> inputs){
        MenuModel model = MenuModel.getModels().get(modelName);
        if (model == null) {player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).menuExist)); return;}
        openModel(MenuModel.getModels().get(modelName),player,inputs);
    }

    private static MenuAction buildAction(String actionString){
        MenuAction action = new MenuAction();
        String actionId = actionString.split("-")[0];
        action.act = Action.valueOf(actionId);
        if (!actionString.contains("-")) return action;
        String actionArguments = actionString.split("-")[1];
        if (actionArguments.contains("]")){
            String actionDetails = actionArguments.split("]")[1];
            String actionInputs = actionArguments.split("]")[0].replace("[","");
            action.action = actionDetails;
            if (!actionDetails.isEmpty())
                action.arguments = List.of(actionInputs.split(","));
        }else action.action = actionArguments;
        return action;
    }

    public static String replacePlaceholders(String string,List<String> listStr){
        for (int i = 0; i<= listStr.size();i++)
            if (!(listStr.size() <= i))
                string = string.replaceAll("<arg"+i+">", listStr.get(i).replace("[","").replaceAll("]",""));
        return string;
    }

    public static List<String> replacePlaceholders(List<String> stringList,List<String> inputs){
        for (String str : stringList) replacePlaceholders(str,inputs);
        return stringList;
    }

    public static void saveMenuModel(MenuModel model){
        guiYml.set(model.getId()+".name",model.getDisplayName());
        guiYml.set(model.getId()+".rows",model.getInv().getSize()/9);
        guiYml.set(model.getId()+".game","This model created from game, please config actions and more...");
        guiYml.createSection(model.getId()+".content");

        int slot = 0;
        for (ItemStack itemStack : model.getInv().getContents()){
            try{
                if (itemStack != null){
                    if (itemStack.getType().name().contains("GLASS_PANE"))
                        ItemWrapper.wrapFilterToPath(guiYml.getConfigurationSection(model.getId()),itemStack,slot);
                    else
                        ItemWrapper.wrapItemToPath(guiYml.getConfigurationSection(model.getId()+".content"),itemStack,slot);
                }
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

    public static void removeMenuModel(String menu){
        guiYml.set(menu,null);
        MenuModel.getModels().remove(menu);
        try {
            guiYml.save(gui);
        } catch (IOException e) {throw new RuntimeException(e);}
        MenuManager.loadMenuModels();
    }

    public static Inventory copy(Inventory inventory) {
        Inventory inv = Bukkit.createInventory(null, inventory.getSize(), "");

        ItemStack[] orginal = inv.getContents();
        ItemStack[] clone = new ItemStack[orginal.length];

        System.arraycopy(orginal, 0, clone, 0, orginal.length);

        inv.setContents(clone);
        return inv;
    }

    public static void logError(String error){
        try {
            File file = new File(MenuMine.getInstance().getDataFolder(),"Errors.txt");
            if (!file.exists()) MenuMine.getInstance().saveResource("Errors.txt",false);
            FileWriter myWriter = new FileWriter("Errors.txt");
            myWriter.write(error + "\n");
            myWriter.close();
        }catch (Exception ignored){}
    }
    public static void logError(Exception error){
        try {
            File file = new File(MenuMine.getInstance().getDataFolder(),"Errors.txt");
            if (!file.exists()) MenuMine.getInstance().saveResource("Errors.txt",false);
            FileWriter myWriter = new FileWriter("Errors.txt");
            myWriter.write(error.toString() + "\n");
            myWriter.close();
        }catch (Exception ignored){}
    }
}
