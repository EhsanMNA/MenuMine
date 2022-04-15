package me.ehsanmna.menumine.events;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import me.ehsanmna.menumine.Managers.MenuAction;
import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (!MenuManager.isMenuDisabled(e.getPlayer())){
            MenuManager.setItemToInventory(e.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        switch (e.getAction()){
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;
                if (!(e.getItem().getType() == MenuManager.getMenuItem().getType())) return;
                NBTItem nbt = new NBTItem(e.getItem());
                if (nbt.hasKey("menu")){
                    e.getPlayer().openInventory(MenuManager.getGUI());
                    e.setCancelled(true);
                }
                break;
        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        NBTItem nbt = new NBTItem(e.getItemDrop().getItemStack());
        if (nbt.hasKey("menu")) e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        for (ItemStack item : e.getDrops()){
            NBTItem nbt = new NBTItem(item);
            if (nbt.hasKey("menu")) e.getDrops().remove(item);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        if (!MenuManager.isMenuDisabled(e.getPlayer())){
            MenuManager.setItemToInventory(e.getPlayer());
        }
    }

    @EventHandler
    public void onCLick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        try {
            NBTItem nbt = null;
            if (item != null) nbt = new NBTItem(item);
            if (nbt != null){
                if (nbt.hasKey("menu")){
                    e.setCancelled(true);
                    return;
                }
            }
        }catch (Exception ignored){}


        if (Objects.equals(e.getView().getTopInventory(), MenuManager.getGUI())){
            e.setCancelled(true);
            if (Objects.equals(e.getClickedInventory(), MenuManager.getGUI())){
                if (MenuManager.actionsManager.containsKey(e.getSlot())){

                    for (MenuAction action : MenuManager.actionsManager.get(e.getSlot())){
                        action.run((Player) e.getWhoClicked());
                    }

                }
            }
        }
    }



}
