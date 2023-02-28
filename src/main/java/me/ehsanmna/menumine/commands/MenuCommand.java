package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.PlayerManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player){
            String prefix = "&bMenuMine &f» ";
            try {
                prefix = PlayerManager.getPlayerLanguage(player).prefix;
            }catch (Exception error){
                System.out.println(ChatColor.YELLOW + "Could not find language for "+player.getName()+" setting default language!");
                String defaultLanguage = MenuMine.getInstance().getConfig().getString("defaultLanguage");
                System.out.println(ChatColor.YELLOW + "I think default language is "+defaultLanguage+"!");
                PlayerManager.playerLanguages.put(player.getUniqueId(),defaultLanguage);
                System.out.println(ChatColor.YELLOW + player.getName() + " is now "+PlayerManager.playerLanguages.get(player.getUniqueId())+" speaker!");
            }


            switch (args.length){
                case 0:
                    MenuManager.open(player);
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("toggle")){
                        if (MenuManager.isMenuDisabled(player)){
                            MenuManager.enableMenu(player);
                        }else MenuManager.disableMenu(player);
                        player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).visibilityChange));
                    }else if (args[0].equalsIgnoreCase("help")){
                        player.sendMessage(MenuMine.color("&b«-----------------»"));
                        player.sendMessage(MenuMine.color("&3/Menu &fOpen menu gui"));
                        player.sendMessage(MenuMine.color("&3/Menu toggle &fDisable/enable menu in inventory"));
                        player.sendMessage(MenuMine.color("&3/Menu language &f[lang] select your language"));
                        if (player.hasPermission("mineMenu.developer")) {
                            player.sendMessage(MenuMine.color("&3/Menu reload &fReload plugin."));
                            player.sendMessage(MenuMine.color("&3/Menu create [name] &fCreate a new menu."));
                            player.sendMessage(MenuMine.color("&3/Menu open [menu] &fOpen a specify menu."));
                        }
                        player.sendMessage(MenuMine.color("&b«-----------------»"));
                    }else if (args[0].equalsIgnoreCase("reload")){
                        if (player.hasPermission("menuMine.developer")){
                            try {
                                Storage.refreshData();
                                MenuManager.loadMenu();
                                MenuManager.loadMenuModels();
                                player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).reload));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case 2:
                    if (args[0].equalsIgnoreCase("create")){
                        if (player.hasPermission("menuMine.developer")){
                            PlayerManager.playersReadyToInteract.put(player.getUniqueId(), args[1]);
                            player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).chestClick));
                        }
                    }
                    else if (args[0].equalsIgnoreCase("open"))
                        MenuManager.openModel(args[1],player);
                    else if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang")){
                        PlayerManager.playerLanguages.put(player.getUniqueId(),args[1]);
                        player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).successfully));
                    }
            }

        }else {
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("reload")){
                    try {
                        Storage.refreshData();
                        MenuManager.loadMenu();
                        MenuManager.loadMenuModels();
                        System.out.println("Done!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (args.length == 3){
                if (args[0].equalsIgnoreCase("open")){
                    MenuManager.openModel(args[1],Bukkit.getPlayer(args[2]));
                }
                else if (args[0].equalsIgnoreCase("language")){
                    Player player = Bukkit.getPlayer(args[1]);
                    assert player != null;
                    PlayerManager.playerLanguages.put(player.getUniqueId(),args[2]);
                    System.out.println("Done!");
                }
            }
        }
        return false;
    }
}
