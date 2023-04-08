package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.PlayerManager;
import me.ehsanmna.menumine.models.MenuModel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuTabCompleter implements TabCompleter {

    List<String> tabs = Arrays.asList("help","toggle");

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,@NotNull Command command,@NotNull String alias, String[] args) {
        List<String> list = new ArrayList<>();
        Player player = null;
        if (sender instanceof Player)  player = (Player) sender;
        if (args.length == 1){
            for (String arg : tabs)
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) list.add(arg);
            if (player != null ) {
                if (player.hasPermission("menumine.command.reload")) if ("reload".startsWith(args[0])) list.add("reload");
                if (player.hasPermission("menumine.command.menulist")) if ("list".startsWith(args[0])) list.add("list");
                if (player.hasPermission("menumine.command.open")) if ("open".startsWith(args[0])) list.add("open");
                if (player.hasPermission("menumine.command.create")) if ("create".startsWith(args[0])) list.add("create");
                if (player.hasPermission("menumine.command.remove")) if ("remove".startsWith(args[0])) list.add("remove");
                if (player.hasPermission("menumine.command.language")) if ("language".startsWith(args[0])) list.add("language");
            }else if ("open".startsWith(args[0])) list.add("open");

            return list;
        }
        if (args.length == 2){
            List<String> tabs = new ArrayList<>();
            if (args[0].equalsIgnoreCase("language") || args[0].startsWith("lang")) {
                assert player != null;
                if (player.hasPermission("menumine.command.language")) {
                    try {
                        for (String language : PlayerManager.langs.keySet())
                            if (language.startsWith(args[1])) tabs.add(language);
                    }catch ( NullPointerException e){
                        tabs.add("language name");
                    }
                }
            } else if (args[0].equalsIgnoreCase("open"))
                for (MenuModel m : MenuModel.getModels().values()) if (m.getName().startsWith(args[1])) tabs.add(m.getName());
            return tabs;
        }
        return list;
    }
}
