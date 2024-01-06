package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.Managers.economy.EconomyManager;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.Action;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.utils.ActionBar;
import me.ehsanmna.menumine.utils.Titles;
import me.ehsanmna.menumine.utils.XSound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuAction {

    Action act;
    String action;

    public boolean run(Player player, ItemStack item){
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
            case CONSOLE: Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),action.replace("%player%", player.getName()));
            case CLOSE: player.closeInventory(); return true;
            case MENU:
                try {MenuModel.getModels().get(action).openMenu(player);
                }catch (Exception error){player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).prefix +PlayerManager.getPlayerLanguage(player).failed));}
                return true;

            /*case IF:
                // IF-[bool][value] action
                String act = action.split("]")[0].replace("[","").replace("]","");
                if (act.equalsIgnoreCase("CustomModelData")){
                    if (item.getItemMeta().getCustomModelData() == Integer.parseInt(action.split("]")[1].replace("[","").replace("]",""))) return true;
                    else return false;
                }else if (act.equalsIgnoreCase("Name")){
                    if (item.getItemMeta().getDisplayName().equalsIgnoreCase(action.split("]")[1].replace("[","").replace("]",""))) return true;
                    else return false;
                } else System.out.println("Could not done menu action! because the value of IF [boolean] is not correctly! input [true] or [false] for first one!!!");

            case ELSE:

            case CHANGE:
                String change = action.split("]")[0].replace("[","").replace("]","");
                ItemMeta meta = item.getItemMeta();
                if (change.equalsIgnoreCase("CustomModelData")){
                    meta.setCustomModelData(Integer.valueOf(action.split("]")[1]));
                }else if (change.equalsIgnoreCase("Name")){
                    meta.setDisplayName(MenuMine.color((action.split("]")[1])));
                } else if (change.equalsIgnoreCase("item")) {

                }
                item.setItemMeta(meta);*/
        }
        return false;
    }

}
