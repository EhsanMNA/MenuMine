package me.ehsanmna.menumine;

import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.Tasks.RefreshTask;
import me.ehsanmna.menumine.commands.Menu;
import me.ehsanmna.menumine.events.Join;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MenuMine extends JavaPlugin {

    static MenuMine main = null;
    static RefreshTask task = new RefreshTask();

    @Override
    public void onEnable() {
        main = this;

        saveDefaultConfig();
        Storage.setupDataStorageYml();
        MenuManager.setUp();
        MenuManager.loadMenu();

        task.run();


        getCommand("menu").setExecutor(new Menu());

        getServer().getPluginManager().registerEvents(new Join(),this);

        getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
        getServer().getConsoleSender().sendMessage(color("&3MenuMine has been enabled."));
        getServer().getConsoleSender().sendMessage(color("&eCreated by EhsanMNA. V0.2"));
        getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
    }

    @Override
    public void onDisable() {
        try {
            task.stop();
        }catch (Exception ignored){}


        getServer().getConsoleSender().sendMessage(color("&4----------=======----------"));
        getServer().getConsoleSender().sendMessage(color("&cMenuMine has been disabled."));
        getServer().getConsoleSender().sendMessage(color("&eCreated by EhsanMNA."));
        getServer().getConsoleSender().sendMessage(color("&4----------=======----------"));
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
