package me.ehsanmna.menumine;

import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.Managers.economy.EconomyManager;
import me.ehsanmna.menumine.Managers.economy.EconomyType;
import me.ehsanmna.menumine.commands.MenuCommand;
import me.ehsanmna.menumine.commands.MenuTabCompleter;
import me.ehsanmna.menumine.events.InteractListener;
import me.ehsanmna.menumine.events.Listeners;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import me.ehsanmna.menumine.utils.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MenuMine extends JavaPlugin {

    public static boolean logMessages = true;
    static MenuMine main = null;

    @Override
    public void onEnable() {
        long ticks = System.currentTimeMillis();
        main = this;
        saveDefaultConfig();

        if (!Objects.requireNonNull(getConfig().getString("version")).equalsIgnoreCase("1.4"))
            try {
                getConfig().set("version","1.4");
                getConfig().set("Metrics",true);
                saveDefaultConfig();
            }catch (Exception ignored){}
        if (getConfig().contains("useSpigotAPI")) NBTItemManager.useSpigotAPI = getConfig().getBoolean("useSpigotAPI");
        if (getConfig().contains("logEnableMessages")) logMessages = getConfig().getBoolean("logEnableMessages");
        if (getConfig().contains("PlaceholderAPI_support")) Storage.papiUse = getConfig().getBoolean("PlaceholderAPI_support");
        if (getConfig().contains("Economy")) {
            Storage.papiUse = getConfig().getBoolean("Economy.enabled");
            EconomyManager.setup(EconomyType.valueOf(Objects.requireNonNull(getConfig().getString("Economy.type")).toUpperCase()));
        }

        if (logMessages) getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
        try {
            if (getConfig().getBoolean("Metrics")) {
                new Metrics(this,18107);
                System.out.println(color("&3Metrics has been set."));
            }
        }catch (Exception ignored){}
        Storage.setupDataStorageYml();
        MenuManager.setUp();

        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand());
        Objects.requireNonNull(getCommand("menu")).setTabCompleter(new MenuTabCompleter());

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
        try {Storage.refreshData();}
        catch (IOException ignored) {}

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
