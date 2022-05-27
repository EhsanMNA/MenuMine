package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Storage {

    public static HashSet<UUID> disabledMenus = new HashSet<>();

    static YamlConfiguration yamlConfiguration;
    static File file;

    public static void loadData(){
        try {
            for (String uuid : yamlConfiguration.getStringList("players")){
                try {
                    disabledMenus.add(UUID.fromString(uuid));
                }catch (Exception error){
                    System.out.println(uuid + " is not a valid uuid form.");
                }
            }
        }catch (Exception ignored){
            System.out.println("Couldn't find any data's");
        }
    }

    public static void refreshData() throws IOException {
        List<String> list = yamlConfiguration.getStringList("players");
        for (UUID uuid : disabledMenus){
            list.add(uuid.toString());
        }
        yamlConfiguration.set("players",list);
        yamlConfiguration.save(file);
    }

    public static void setupDataStorageYml(){
        file = new File(MenuMine.getInstance().getDataFolder(), "Data.yml");
        if (!file.exists()) {
            MenuMine.getInstance().saveResource("Data.yml",false);
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }


}
