package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;

            switch (args.length){
                case 0:
                    MenuManager.open(player);
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("toggle")){
                        if (MenuManager.isMenuDisabled(player)){
                            MenuManager.enableMenu(player);
                        }else MenuManager.disableMenu(player);
                        player.sendMessage(MenuMine.color("&b&lMenu &3» &fMenu visible has been changed."));
                    }else if (args[0].equalsIgnoreCase("help")){
                        player.sendMessage(MenuMine.color("&b«-----------------»"));
                        player.sendMessage(MenuMine.color("&3/Menu &fOpen menu gui"));
                        player.sendMessage(MenuMine.color("&3/Menu toggle &fDisable/enable menu in inventory"));
                        if (player.hasPermission("mineMenu.developer")) player.sendMessage(MenuMine.color("&3/Menu reload &fReload plugin."));
                        player.sendMessage(MenuMine.color("&b«-----------------»"));
                    }else if (args[0].equalsIgnoreCase("reload")){
                        if (player.hasPermission("menuMine.developer")){
                            try {
                                Storage.refreshData();
                                MenuManager.loadMenu();
                                MenuManager.loadMenuModels();
                                player.sendMessage(MenuMine.color("&bPlugin has been reloaded."));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
        return false;
    }
}
