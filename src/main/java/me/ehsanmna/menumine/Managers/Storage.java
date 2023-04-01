package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Storage {

    public static boolean economyUse = false;
    public static boolean papiUse = false;
    public static HashSet<UUID> disabledMenus = new HashSet<>();

    static YamlConfiguration yamlConfiguration;
    static File file;

    public static void loadData(){
        try {
            try {
                for (String uuid : yamlConfiguration.getStringList("players"))
                    try {disabledMenus.add(UUID.fromString(uuid));}catch (Exception error){if (MenuMine.logMessages) System.out.println("[MenuMine] "+uuid + " is not a valid uuid form.");}

            }catch (Exception error){
                disabledMenus.clear();
                if (MenuMine.logMessages) System.out.println("[MenuMine] Something went wrong while loading data's!");
            }
            if (yamlConfiguration.contains("languages"))
                for (String uuid : yamlConfiguration.getConfigurationSection("languages").getKeys(false)){
                    UUID id = UUID.fromString(uuid);
                    PlayerManager.playerLanguages.put(id,yamlConfiguration.getString("languages."+uuid));
                }

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


}
