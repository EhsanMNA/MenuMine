package me.ehsanmna.menumine;

import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.PlayerManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.Tasks.RefreshTask;
import me.ehsanmna.menumine.commands.MenuCommand;
import me.ehsanmna.menumine.commands.MenuTabCompleter;
import me.ehsanmna.menumine.events.InteractListener;
import me.ehsanmna.menumine.events.Listeners;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class MenuMine extends JavaPlugin {

    public static boolean logMessages = true;
    static MenuMine main = null;
    static RefreshTask task = new RefreshTask();

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
        long ticks = System.currentTimeMillis();
        main = this;
        saveDefaultConfig();

        if (!getConfig().getString("version").equalsIgnoreCase("1.2")) {
            try {
                getConfig().addDefault("defaultLanguage","en");
                getConfig().addDefault("logEnableMessages","true");
                getConfig().addDefault("version","1.2");
                getConfig().addDefault("InteractEvent","true");
                getConfig().addDefault("PlaceholderAPI_support","true");
                getConfig().save(new File(getDataFolder(),"config.yml"));
            }catch (Exception ignored){}
        }
        if (getConfig().contains("useSpigotAPI")) NBTItemManager.useSpigotAPI = getConfig().getBoolean("useSpigotAPI");
        if (getConfig().contains("logEnableMessages")) logMessages = getConfig().getBoolean("logEnableMessages");
        if (getConfig().contains("PlaceholderAPI_support")) Storage.papiUse = getConfig().getBoolean("PlaceholderAPI_support");

        Storage.setupDataStorageYml();
        MenuManager.setUp();

        task.run();

        getCommand("menu").setExecutor(new MenuCommand());
        getCommand("menu").setTabCompleter(new MenuTabCompleter());

        getServer().getPluginManager().registerEvents(new Listeners(),this);
        if (getConfig().getBoolean("InteractEvent")) getServer().getPluginManager().registerEvents(new InteractListener(),this);

        if (logMessages){
            long finalTicks = System.currentTimeMillis();
            getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
            getServer().getConsoleSender().sendMessage(color("&3MenuMine has been enabled."));
            getServer().getConsoleSender().sendMessage(color("&9" + (finalTicks - ticks) + "ms&3 take to load the plugin."));
            getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
        }


    }

    @Override
    public void onDisable() {
        try {task.stop();}catch (Exception ignored){}

        if (logMessages){
            getServer().getConsoleSender().sendMessage(color("&4----------=======----------"));
            getServer().getConsoleSender().sendMessage(color("&cMenuMine has been disabled."));
            getServer().getConsoleSender().sendMessage(color("&eCreated by Love."));
            getServer().getConsoleSender().sendMessage(color("&4----------=======----------"));
        }

    }

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&',message);
    }
    public static List<String> color(List<String> messages){
        List<String> list = new ArrayList<>();
        for (String str : messages){
            list.add(color(str));
        }
        return list;
    }

    public static MenuMine getInstance(){
        return main;
    }
}
