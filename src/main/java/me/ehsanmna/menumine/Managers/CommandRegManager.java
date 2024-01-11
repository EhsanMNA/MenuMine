package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandRegManager {

    public static void registerCommand(String command, CommandExecutor commandExecutor){
        try{
            final Server server = Bukkit.getServer();
            Field commandMapField = server.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            final CommandMap commandMap = (CommandMap) commandMapField.get(server);
            Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            commandConstructor.setAccessible(true);
            PluginCommand pluginCommand = commandConstructor.newInstance(command, MenuMine.getInstance());
            commandMap.register(command, pluginCommand);
            pluginCommand.setExecutor(commandExecutor);
            // pluginCommand.setTabCompleter(yourTabCompleter);
        } catch (NoSuchFieldException | InvocationTargetException
                 | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            //Error handling
        }
    }

    public static void updateCommands(){
        try{
            final Method syncCommandsMethod = Bukkit.getServer().getClass().getDeclaredMethod("syncCommands");
            syncCommandsMethod.setAccessible(true);
            syncCommandsMethod.invoke(Bukkit.getServer());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            if (MenuMine.logMessages) MenuMine.getInstance().getLogger().warning("Could not sync commands!");
        }
        for (Player player : Bukkit.getOnlinePlayers()) player.updateCommands();
    }

}
