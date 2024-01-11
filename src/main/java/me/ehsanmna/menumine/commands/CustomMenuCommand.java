package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] sargstrings) {

        if (sender instanceof Player player){
            if (MenuManager.commandsToMenu.containsKey(s))
                MenuManager.commandsToMenu.get(s).openMenu(player);
        }else sender.sendMessage("Only players can execute this command!");

        return false;
    }
}
