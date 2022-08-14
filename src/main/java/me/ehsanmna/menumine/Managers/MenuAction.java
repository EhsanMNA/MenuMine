package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MenuModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuAction {

    Action act;
    String action;

    public void run(Player player){
        switch (act){
            case MESSAGE : player.sendMessage(MenuMine.color(action)); break;
            case COMMAND: Bukkit.getServer().dispatchCommand(player,action); break;
            case CLOSE: player.closeInventory(); break;
            case MENU:
                if (action.equalsIgnoreCase("Main")) MenuManager.open(player);
                else {
                    try {
                        MenuModel model = MenuModel.getModels().get(action);
                        model.openMenu(player);
                    }catch (Exception error){
                        player.sendMessage(MenuMine.color("&cThat menu does not exists."));
                    }
                }
                break;
        }
    }

}
