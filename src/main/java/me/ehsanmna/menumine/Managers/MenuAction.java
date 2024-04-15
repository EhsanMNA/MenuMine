package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.Managers.economy.EconomyManager;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.Action;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import me.ehsanmna.menumine.utils.ActionBar;
import me.ehsanmna.menumine.utils.Titles;
import me.ehsanmna.menumine.utils.XMaterial;
import me.ehsanmna.menumine.utils.XSound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuAction {

    Action act;
    String action;
    List<String> arguments = new ArrayList<>();

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
            case HASMONEY:
                if (!EconomyManager.economy.hasMoney(player, Float.parseFloat(action))){
                if (Storage.autoSendMessage) player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).money));
                return false;
                }else return true;
            case GIVEMONEY: EconomyManager.economy.addMoney(player,Float.parseFloat(action)); return true;
            case TAKEMONEY:
                if (EconomyManager.economy.hasMoney(player,Float.parseFloat(action))) {
                    if (Storage.autoSendMessage) EconomyManager.economy.takeMoney(player,Float.parseFloat(action));
                    return true;
                } else return false;
            case PERMISSION:
                if (player.hasPermission(action)) return true;
                else {
                    if (Storage.autoSendMessage) player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).permission));
                    return false;
                }
            case MESSAGE: player.sendMessage(MenuMine.color(action)); return true;
            case COMMAND: Bukkit.getServer().dispatchCommand(player,action); return true;
            case CONSOLE: Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),action.replace("%player%", player.getName()));
            case CLOSE: player.closeInventory(); return true;
            case MENU:
                try {
                    if (arguments.isEmpty()) MenuManager.openModel(action,player);
                    else MenuManager.openModel(action,player,arguments);
                }catch (Exception error){player.sendMessage(MenuMine.color(PlayerManager.getPlayerLanguage(player).prefix +PlayerManager.getPlayerLanguage(player).failed));}
                return true;
            case CHANGE:
                ItemMeta meta = item.getItemMeta();
                String type = arguments.get(0);
                if (type.equalsIgnoreCase("MODEL"))
                    meta.setCustomModelData(Integer.valueOf(action));
                else if (type.equalsIgnoreCase("NAME"))
                    meta.setDisplayName(MenuMine.color(action));
                else if (type.equalsIgnoreCase("ITEM"))
                    item.setType(Objects.requireNonNull(XMaterial.valueOf(action).parseMaterial()));

                item.setItemMeta(meta);
        }
        return false;
    }

    public Action getAction(){
        return act;
    }

    public String getActionArgument() {
        return action;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
