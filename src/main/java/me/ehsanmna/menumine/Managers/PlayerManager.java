package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MessageModel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    public static HashMap<UUID,String> playersReadyToInteract = new HashMap<>();

    public static HashMap<UUID,String> playerLanguages = new HashMap<>();
    private static final HashMap<String,MessageModel> langs = new HashMap<>();

    public static void loadMessages(){
        File file = new File(MenuMine.getInstance().getDataFolder(),"Messages.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

        for (String lang : yml.getKeys(false)){
            ConfigurationSection language = yml.getConfigurationSection(lang);
            MessageModel model = new MessageModel();
            for (String messageName : language.getKeys(false)) for (Field msgField : model.getClass().getFields())
                    if (msgField.getName().equalsIgnoreCase(messageName))
                        try {
                            msgField.set(model,language.getString(messageName));
                        } catch (IllegalAccessException e) {throw new RuntimeException(e);}
            langs.put(lang,model);
        }

    }

    public static MessageModel getPlayerLanguage(Player player){
        return langs.get(playerLanguages.getOrDefault(player.getUniqueId(),"en"));
    }

}
