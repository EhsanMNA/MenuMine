package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.MenuAction;
import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.PlayerManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MenuModel;
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
                if (MenuMine.logMessages){
                    System.out.println(ChatColor.YELLOW + "Could not find language for "+player.getName()+" setting default language!");
                    String defaultLanguage = MenuMine.getInstance().getConfig().getString("defaultLanguage");
                    System.out.println(ChatColor.YELLOW + "I think default language is "+defaultLanguage+"!");
                    PlayerManager.playerLanguages.put(player.getUniqueId(),defaultLanguage);
                    System.out.println(ChatColor.YELLOW + player.getName() + " is now "+PlayerManager.playerLanguages.get(player.getUniqueId())+" speaker!");
                }
            }

            switch (args.length){
                case 0:
                    for (MenuAction action : MenuMine.mainActions) action.run(player,MenuManager.getMenuItem());
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("toggle")){
                        if (MenuManager.isMenuDisabled(player)){
                            MenuManager.enableMenu(player);
                        }else MenuManager.disableMenu(player);
                        player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).visibilityChange));
                    }else if (args[0].equalsIgnoreCase("help")){
                        MenuMine.sendMessages(player,PlayerManager.getPlayerLanguage(player).help);
                    }else if (args[0].equalsIgnoreCase("language")){
                        player.sendMessage(MenuMine.color("&3/Menu language &f[lang] select your language"));
                    }else if (args[0].equalsIgnoreCase("list")){
                        if (player.hasPermission("menuMine.developer")){
                            player.sendMessage(MenuMine.color("&a&m---------------------"));
                            try {
                                for (MenuModel model : MenuModel.getModels().values())
                                    player.sendMessage(MenuMine.color("&f - "+model.getName() + "&2 "+model.getId()));
                            } catch (Exception error) {
                                player.sendMessage(MenuMine.color("&eError!"));
                            }
                            player.sendMessage(MenuMine.color("&a&m---------------------"));
                        }
                    }else if (args[0].equalsIgnoreCase("reload")){
                        if (player.hasPermission("menuMine.developer")){
                            try {
                                Storage.refreshData();
                                MenuManager.loadMenu();
                                MenuManager.loadMenuModels();
                                player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).reload));
                            } catch (IOException e) {
                                player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).failed));
                                if (MenuMine.logMessages) System.out.println("Something went wring while reloading the plugin! This is not a bug or plugin problem! check your configs and reload again");
                                if (MenuMine.logMessages) e.printStackTrace();
                            }
                        }
                    }
                    break;
                case 2:
                    if (args[0].equalsIgnoreCase("create")){
                        if (player.hasPermission("menuMine.developer")){
                            if (!MenuModel.getModels().containsKey(args[1])){
                                PlayerManager.playersReadyToInteract.put(player.getUniqueId(), args[1]);
                                player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).chestClick));
                            }else player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).failed));
                        }
                    } else if (args[0].equalsIgnoreCase("remove")){
                        if (player.hasPermission("menuMine.developer")){
                            if (MenuModel.getModels().containsKey(args[1])){
                                MenuManager.removeMenuModel(args[1]);
                                player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).successfully));
                            }else player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).failed));
                        }
                    }
                    else if (args[0].equalsIgnoreCase("open"))
                        MenuManager.openModel(args[1],player);
                    else if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang")){
                        if (PlayerManager.langs.containsKey(args[1])){
                            PlayerManager.playerLanguages.put(player.getUniqueId(),args[1]);
                            player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).successfully));
                        }else player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).failed));
                    }
            }

        }else {
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("reload")){
                    try {
                        Storage.refreshData();
                        MenuManager.loadMenu();
                        MenuManager.loadMenuModels();
                        if (MenuMine.logMessages) System.out.println("[MenuMine] Done!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (args.length == 3){
                if (args[0].equalsIgnoreCase("open"))
                    MenuManager.openModel(args[1],Bukkit.getPlayer(args[2]));
                else if (args[0].equalsIgnoreCase("language")){
                    Player player = Bukkit.getPlayer(args[1]);
                    assert player != null;
                    PlayerManager.playerLanguages.put(player.getUniqueId(),args[2]);
                    if (MenuMine.logMessages) System.out.println("[MenuMine] Done!");
                }
            }
        }
        return false;
    }
}
