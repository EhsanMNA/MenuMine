package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.Managers.controller.PlayerMenuController;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.models.PMenuModel;
import me.ehsanmna.menumine.nbt.NBTItem;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import me.ehsanmna.menumine.utils.xseries.XItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static me.ehsanmna.menumine.MenuMine.color;

public class Storage {

    public static boolean economyUse = false;
    public static boolean papiUse = false;
    public static boolean moveItem = false;
    public static boolean dropItem = false;
    public static boolean autoSendMessage = true;
    public static HashSet<UUID> disabledMenus = new HashSet<>();

    static YamlConfiguration yamlConfiguration;
    static File file;

    public static void loadData(){
        try {
            try {
                for (String uuid : yamlConfiguration.getStringList("players"))
                    try {disabledMenus.add(UUID.fromString(uuid));}
                    catch (Exception error){if (MenuMine.logMessages) System.out.println("[MenuMine] "+uuid + " is not a valid uuid form.");}
            }catch (Exception error){
                disabledMenus.clear();
                if (MenuMine.logMessages) System.out.println("[MenuMine] Something went wrong while loading data's!");
            }
            if (yamlConfiguration.contains("languages"))
                for (String uuid : yamlConfiguration.getConfigurationSection("languages").getKeys(false))
                    PlayerManager.playerLanguages.put(UUID.fromString(uuid),yamlConfiguration.getString("languages."+uuid));
        }catch (Exception ignored){
            if (MenuMine.logMessages) System.out.println("[MenuMine] Couldn't find any data's");
        }
    }

    public static void refreshData() throws IOException {
        List<String> list = yamlConfiguration.getStringList("players");
        for (UUID uuid : disabledMenus) if(!list.contains(uuid.toString())) list.add(uuid.toString());
        for (UUID player : PlayerManager.playerLanguages.keySet())
            yamlConfiguration.set("languages."+player.toString(),PlayerManager.playerLanguages.get(player));
        yamlConfiguration.set("players",list);
        yamlConfiguration.save(file);
    }

    public static void setupDataStorageYml(){
        file = new File(MenuMine.getInstance().getDataFolder(), "Data.yml");
        if (!file.exists()) {
            MenuMine.getInstance().saveResource("Data.yml",false);
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        loadData();
    }

    public static void refreshPMenus(){
        if (MenuMine.logMessages) Bukkit.getServer().getConsoleSender().sendMessage(color("&f[MenuMine] &bReloadingPMenus... "));
        YamlConfiguration yaml = MenuManager.pMYml;
        for (UUID uuid : PlayerMenuController.getPMenuModels().keySet()){
            if (MenuMine.debug) Bukkit.getServer().getConsoleSender().sendMessage(color("&fUUID is &a"+uuid));
            if (!yaml.contains(uuid.toString())) yaml.createSection(uuid.toString());
            PMenuModel pMenuModel = PlayerMenuController.getPMenuModels().get(uuid);
            for (MenuModel model : pMenuModel.getModels().values()){
                if (MenuMine.debug) Bukkit.getServer().getConsoleSender().sendMessage(color("&f- Model *&2"+model.getId()));
                String id = model.getId();
                ConfigurationSection modelSection;
                if (yaml.contains(uuid+"."+id)) modelSection = yaml.getConfigurationSection(uuid+"."+id+".items");
                else modelSection = yaml.createSection(uuid+"."+id+".items");
                int i = 0;
                for (ItemStack itemStack : model.getInv().getContents()){
                    if (itemStack != null && itemStack.getType()!= Material.AIR){
                        NBTItem nbtItem = NBTItemManager.createNBTItem(itemStack);
                        if (MenuMine.debug) Bukkit.getServer().getConsoleSender().sendMessage(color("&f- - ItemStack:&e "+itemStack));
                        if (!nbtItem.hasTag("FilterItem"))
                            XItemStack.serialize(itemStack, modelSection.createSection(i+""));
                    }
                    i++;
                }
            }
        }
        try {
            yaml.save(MenuManager.playerMenu);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
