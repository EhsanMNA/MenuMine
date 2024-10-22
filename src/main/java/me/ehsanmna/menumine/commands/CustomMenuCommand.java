package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class CustomMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender,@Nonnull Command command,@Nonnull String s,@Nonnull String[] args) {

        if (sender instanceof Player player){
            if (MenuManager.commandsToMenu.containsKey(s))
                MenuManager.openModel(MenuManager.commandsToMenu.get(s),player,List.of());
        }else sender.sendMessage("Only players can execute this command!");

        return false;
    }
}
