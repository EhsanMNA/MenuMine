package me.ehsanmna.menumine.commands;

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
                if (player.hasPermission("menumine.command.open")) if ("open".startsWith(args[0])) list.add("open");
                if (player.hasPermission("menumine.command.create")) if ("create".startsWith(args[0])) list.add("create");
                if (player.hasPermission("menumine.command.language")) if ("language".startsWith(args[0])) list.add("language");
            }else if ("open".startsWith(args[0])) list.add("open");

            return list;
        }
        return list;
    }
}
