package me.ehsanmna.menumine.commands;

import me.ehsanmna.menumine.Managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuTabCompleter implements TabCompleter {

    List<String> tabs = Arrays.asList("help","toggle");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
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
                if (player.hasPermission("menumine.command.language")) if ("language".startsWith(args[0])) list.add("language");
            }else if ("open".startsWith(args[0])) list.add("open");

            return list;
        }
        if (args.length == 2){
            List<String> tabs = List.of("---");
            if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang")) {
                assert player != null;
                if (player.hasPermission("menumine.command.language")) {
                    try {
                        for (String language : PlayerManager.langs.keySet())
                            if (language.startsWith(args[1])) list.add(language);
                    }catch ( NullPointerException e){
                        list.add("language name");
                    }
                }
            else if (args[0].equalsIgnoreCase("open"))
                for (Player p : Bukkit.getOnlinePlayers()) if (p.getName().startsWith(args[1])) list.add(p.getName());
            }
            return tabs;
        }
        return list;
    }
}
