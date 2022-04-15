package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuAction {

    Action act;
    String action;

    public void run(Player player){
        switch (act){
            case MESSAGE : player.sendMessage(MenuMine.color(action)); break;
            case COMMAND: Bukkit.getServer().dispatchCommand(player,action); break;
            case CLOSE: player.closeInventory();
        }
    }

}
