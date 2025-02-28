package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.*;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.models.MessageModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;

public class MenuCommand implements CommandExecutor {

    String prefix = "&bMenuMine &f» ";

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player){
            MessageModel messageModel = PlayerManager.getPlayerLanguage(player);
            try {
                prefix = messageModel.prefix;
            }catch (Exception error){
                if (MenuMine.logMessages){
                    System.out.println("Could not find language for "+player.getName()+" setting default language!");
                    String defaultLanguage = MenuMine.getInstance().getConfig().getString("defaultLanguage","en");
                    PlayerManager.playerLanguages.put(player.getUniqueId(),defaultLanguage);
                    System.out.println(player.getName() + " is now "+PlayerManager.playerLanguages.get(player.getUniqueId())+" speaker!");
                }
            }

            switch (args.length){
                case 0:
                    for (MenuAction action : MenuMine.mainActions) action.run(player,MenuManager.getMenuItem());
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("toggle")){
                        if (MenuManager.isMenuDisabled(player)) MenuManager.enableMenu(player);
                        else MenuManager.disableMenu(player);
                        player.sendMessage(MenuMine.color(prefix + messageModel.visibilityChange));

                    }else if (args[0].equalsIgnoreCase("help")){
                        MenuMine.sendMessages(player,messageModel.help);

                    }else if (args[0].equalsIgnoreCase("language")){
                        player.sendMessage(MenuMine.color(prefix + messageModel.language));

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
                                Storage.refreshPMenus();
                                MenuManager.loadMenu();
                                MenuManager.loadMenuModels();
                                CommandRegManager.updateCommands();
                                player.sendMessage(MenuMine.color(prefix + messageModel.reload));
                            } catch (IOException e) {
                                player.sendMessage(MenuMine.color(prefix + messageModel.failed));
                                if (MenuMine.logMessages) System.out.println("Something went wring while reloading the plugin! This is not a bug or plugin problem! check your configs and reload again");
                                if (MenuMine.logMessages) e.printStackTrace();
                            }
                        }
                    }else if (args[0].equalsIgnoreCase("debug")) {
                        if(!player.hasPermission("menu.command.debug")) return false;
                        if (PlayerManager.debugers.contains(player.getUniqueId())){
                            PlayerManager.debugers.remove(player.getUniqueId());
                            player.sendMessage(MenuMine.color(prefix + "&c Debugging off!"));
                        }else {
                            PlayerManager.debugers.add(player.getUniqueId());
                            player.sendMessage(MenuMine.color(prefix + "&a Debugging on!"));
                        }
                    }
                    break;
                case 2:
                    if (args[0].equalsIgnoreCase("create")){
                        if (player.hasPermission("menuMine.developer")){
                            if (!MenuModel.getModels().containsKey(args[1])){
                                PlayerManager.playersReadyToInteract.put(player.getUniqueId(), args[1]);
                                player.sendMessage(MenuMine.color(prefix + messageModel.chestClick));
                            }else player.sendMessage(MenuMine.color(prefix + messageModel.failed));
                        }
                    } else if (args[0].equalsIgnoreCase("remove")){
                        if (player.hasPermission("menuMine.developer")){
                            if (MenuModel.getModels().containsKey(args[1])){
                                MenuManager.removeMenuModel(args[1]);
                                player.sendMessage(MenuMine.color(prefix + messageModel.successfully));
                            }else player.sendMessage(MenuMine.color(prefix + messageModel.failed));
                        }
                    }
                    else if (args[0].equalsIgnoreCase("open"))
                        MenuManager.openModel(args[1],player);
                    else if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang")){
                        if (PlayerManager.langs.containsKey(args[1])){
                            PlayerManager.playerLanguages.put(player.getUniqueId(),args[1]);
                            player.sendMessage(MenuMine.color(prefix + messageModel.successfully));
                        }else player.sendMessage(MenuMine.color(prefix + messageModel.failed));
                    }else if (args[0].equalsIgnoreCase("debug")) {
                        if(!player.hasPermission("menu.command.debug")) return false;
                        if (args[1].equalsIgnoreCase("console")) MenuMine.debug = true;
                        player.sendMessage(MenuMine.color(prefix + messageModel.successfully));
                    }
            }

        }else {
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("reload")){
                    try {
                        Storage.refreshData();
                        MenuManager.loadMenu();
                        MenuManager.loadMenuModels();
                        if (MenuMine.logMessages) System.out.println("Reloaded MenuMine!");
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
                    if (MenuMine.logMessages) System.out.println("Language changed!");
                }
            }
        }
        return false;
    }
}
