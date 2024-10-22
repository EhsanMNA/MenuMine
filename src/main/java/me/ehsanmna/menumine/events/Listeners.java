package me.ehsanmna.menumine.events;

import me.ehsanmna.menumine.Managers.MenuAction;
import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.PlayerManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.Action;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.nbt.NBTItem;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Listeners implements org.bukkit.event.Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (!MenuMine.menuItem) return;
        if (!MenuManager.isMenuDisabled(e.getPlayer()))
            MenuManager.setItemToInventory(e.getPlayer());

        if (!e.getPlayer().hasPlayedBefore())PlayerManager.playerLanguages.put(e.getPlayer().getUniqueId(), MenuMine.getInstance().getConfig().getString("defaultLanguage"));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (!MenuMine.menuItem) return;
        if (Storage.dropItem) return;
        NBTItem nbt = NBTItemManager.createNBTItem(e.getItemDrop().getItemStack());
        if (nbt.hasTag("menu") || nbt.hasTag("MenuItem")) e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if (!MenuMine.menuItem) return;
        if (Storage.dropItem) return;
        if (e.getDrops().isEmpty()) return;
        for (ItemStack item : e.getDrops()){
            NBTItem nbt = NBTItemManager.createNBTItem(item);
            if (nbt.hasTag("menu")||nbt.hasTag("MenuItem")) {e.getDrops().remove(item); return;}
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        if (!MenuMine.menuItem) return;
        if (!MenuManager.isMenuDisabled(e.getPlayer())) MenuManager.setItemToInventory(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCLick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        try {
            NBTItem nbt = null;
            try {if (item != null) nbt = NBTItemManager.createNBTItem(item);
            }catch (Exception ignored){}
            if (nbt != null){
                if (nbt.hasTag("menu") && !Storage.moveItem) e.setCancelled(true);
                if (nbt.hasTag("MenuItem")){
                    if (nbt.hasTag("FilterItem")) e.setCancelled(true);
                    if (!e.isCancelled()){
                        MenuModel model = MenuModel.getModels().get(nbt.getString("MenuModel"));
                        if (model.getActions(e.getSlot())==null || model.getActions(e.getSlot()).isEmpty()) return;
                        for (MenuAction action : model.getActions(e.getSlot()))
                            try {
                                if (!model.isItemMove() || action.getAction().equals(Action.CANCEL)) e.setCancelled(true);
                                if (PlayerManager.debugers.contains(player.getUniqueId()))
                                    player.sendMessage("Action: "+action.getAction().name() +", Arguments: "+action.getActionArgument()
                                    +", Inputs: "+action.getArguments().toString());
                                if (!action.run(player,item)){
                                    if (model.getActionsDeny()!=null && !model.getActionsDeny().isEmpty())
                                        if (model.getDenyActions(e.getSlot()) !=null && !model.getDenyActions(e.getSlot()).isEmpty())
                                            for (MenuAction denyAction : model.getDenyActions(e.getSlot()))
                                                if (!denyAction.run(player,item)) break;
                                    break;
                                }
                            }catch (Exception error){error.printStackTrace();}
                    }
                }
            }
            assert item != null;
            if (!Storage.moveItem && MenuMine.menuItem)
                if (item.getType().equals(Material.AIR))
                    if (e.getHotbarButton() == MenuManager.slot && e.getSlotType().equals(InventoryType.SlotType.QUICKBAR)
                            && e.getClick().equals(ClickType.NUMBER_KEY) && e.getAction().equals(InventoryAction.HOTBAR_SWAP)) e.setCancelled(true);
        }catch (Exception ignored){}

        try{
            if (Objects.equals(e.getView().getTopInventory(), MenuManager.getGUI())){
                e.setCancelled(true);
                if (Objects.equals(e.getClickedInventory(), MenuManager.getGUI()))
                    if (MenuManager.actionsManager.containsKey(e.getSlot()))
                        for (MenuAction action : MenuManager.actionsManager.get(e.getSlot()))
                            if (!action.run(player,item)) break;
            }
        }catch (Exception ignored){}
    }




}
