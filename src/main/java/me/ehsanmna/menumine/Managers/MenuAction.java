package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.Managers.economy.EconomyManager;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.utils.ActionBar;
import me.ehsanmna.menumine.utils.Titles;
import me.ehsanmna.menumine.utils.XSound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuAction {

    Action act;
    String action;

    public boolean run(Player player){
        switch (act){
            case ACTIONBAR:
                ActionBar.sendActionBar(player,MenuMine.color(action));
                return true;
            case SOUND:  XSound.play(player,action); return true;
            case TITLE:
                String title = action.split("-")[0];
                String subTitle = action.split("-")[1];
                Titles.sendTitle(player,MenuMine.color(title),MenuMine.color(subTitle));
                return true;
            case HASMONEY: if (!EconomyManager.economy.hasMoney(player, Float.parseFloat(action))){
                player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).money));
                return false;
            }else return true;
            case GIVEMONEY: EconomyManager.economy.addMoney(player,Float.parseFloat(action)); return true;
            case TAKEMONEY:
                if (EconomyManager.economy.hasMoney(player,Float.parseFloat(action))) {
                    EconomyManager.economy.takeMoney(player,Float.parseFloat(action));
                    return true;
                } else return false;
            case MESSAGE: player.sendMessage(MenuMine.color(action)); return true;
            case COMMAND: Bukkit.getServer().dispatchCommand(player,action); return true;
            case CONSOLE: Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),action);
            case CLOSE: player.closeInventory(); return true;
            case MENU:
                if (action.equalsIgnoreCase("Main")) {
                    player.closeInventory();
                    MenuManager.open(player);
                }
                else
                    try {
                        MenuModel model = MenuModel.getModels().get(action);
                        model.openMenu(player);
                    }catch (Exception error){player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).prefix +PlayerManager.getPlayerLanguage(player).failed));}

                return true;
        }
        return false;
    }

}
