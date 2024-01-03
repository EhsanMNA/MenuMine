package me.ehsanmna.menumine;

import me.ehsanmna.menumine.Managers.MenuAction;
import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.Managers.economy.EconomyManager;
import me.ehsanmna.menumine.Managers.economy.EconomyType;
import me.ehsanmna.menumine.commands.MenuCommand;
import me.ehsanmna.menumine.commands.MenuTabCompleter;
import me.ehsanmna.menumine.events.InteractListener;
import me.ehsanmna.menumine.events.Listeners;
import me.ehsanmna.menumine.events.SwapHandItemsEvent;
import me.ehsanmna.menumine.nbt.NBTItem;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import me.ehsanmna.menumine.utils.Metrics;
import me.ehsanmna.menumine.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MenuMine extends JavaPlugin {

    public static boolean logMessages = true;
    static MenuMine main = null;

    public static List<MenuAction> mainActions = new ArrayList<>();

    @Override
    public void onEnable() {
        long ticks = System.currentTimeMillis();
        main = this;
        saveDefaultConfig();

        if (getConfig().contains("logEnableMessages")) logMessages = getConfig().getBoolean("logEnableMessages");
        if (getConfig().contains("NBTSystem")) NBTItemManager.nbtSystem = getConfig().getString("NBTSystem");
        if (getConfig().contains("PlaceholderAPI_support")) Storage.papiUse = getConfig().getBoolean("PlaceholderAPI_support");
        if (getConfig().contains("Economy")) {
            Storage.economyUse = getConfig().getBoolean("Economy.enabled");
            EconomyManager.setup(EconomyType.valueOf(Objects.requireNonNull(getConfig().getString("Economy.type")).toUpperCase()));
        }
        if (getConfig().contains("MenuItemCheckerTask"))
            if (getConfig().getBoolean("MenuItemCheckerTask.enabled")) runCheckerTask(getConfig().getInt("MenuItemCheckerTask.time"));

        if (logMessages) getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
        try {
            if (getConfig().getBoolean("Metrics")) {
                new Metrics(this,18107);
                if (logMessages) getServer().getConsoleSender().sendMessage(color("&3 Metrics has been enabled."));
            }
        }catch (Exception ignored){}
        Storage.setupDataStorageYml();
        MenuManager.setUp();

        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand());
        Objects.requireNonNull(getCommand("menu")).setTabCompleter(new MenuTabCompleter());

        getServer().getPluginManager().registerEvents(new Listeners(),this);
        if (getConfig().getBoolean("InteractEvent")) {
            getServer().getPluginManager().registerEvents(new InteractListener(),this);
            if (ReflectionUtils.supports(9)) getServer().getPluginManager().registerEvents(new SwapHandItemsEvent(),this);
        }

        if (logMessages){
            long finalTicks = System.currentTimeMillis();
            getServer().getConsoleSender().sendMessage(color("&b----------=======----------"));
            getServer().getConsoleSender().sendMessage(color("&3MenuMine has been enabled."));
            getServer().getConsoleSender().sendMessage(color("&9" + (finalTicks - ticks) + "ms&3 took to load the plugin."));
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
            getServer().getConsoleSender().sendMessage(color("&4----------=======----------"));
        }

    }

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&',message);
    }
    public static List<String> color(List<String> messages){
        List<String> list = new ArrayList<>();
        for (String str : messages) list.add(color(str));
        return list;
    }
    public static void sendMessages(Player player,List<String> messages){
        for (String message : messages)
            player.sendMessage(color(message));
    }

    public static MenuMine getInstance(){
        return main;
    }

    public void runCheckerTask(int seconds){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()){
                Inventory inv = player.getInventory();
                try {
                    int slot = 0;
                    for (ItemStack i : inv.getContents()){
                        if (slot == MenuManager.slot) {slot++; continue;}
                        if (i == null || i.getType().equals(Material.AIR)) {slot++; continue;}
                        if (i.getType().equals(MenuManager.getMenuItem().getType())){
                            NBTItem nbtItem = NBTItemManager.createNBTItem(i);
                            if (nbtItem.hasTag("menu")) {
                                inv.remove(i);
                                MenuManager.setItemToInventory(player);
                            }
                        }
                        slot++;
                    }
                }catch (Exception ignored){}
            }
        }, 0,seconds);
    }
}
