package me.ehsanmna.menumine.events;

import me.ehsanmna.menumine.Managers.MenuAction;
import me.ehsanmna.menumine.Managers.MenuManager;
import me.ehsanmna.menumine.Managers.PlayerManager;
import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.MenuModel;
import me.ehsanmna.menumine.nbt.NBTItem;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import me.ehsanmna.menumine.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Listeners implements org.bukkit.event.Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (!MenuManager.isMenuDisabled(e.getPlayer()))
            MenuManager.setItemToInventory(e.getPlayer());

        if (!e.getPlayer().hasPlayedBefore())PlayerManager.playerLanguages.put(e.getPlayer().getUniqueId(), MenuMine.getInstance().getConfig().getString("defaultLanguage"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        String prefix = PlayerManager.getPlayerLanguage(player).prefix;
        switch (e.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                if (e.getClickedBlock() != null && e.getClickedBlock().getType() == XMaterial.CHEST.parseMaterial() &&
                        PlayerManager.playersReadyToInteract.containsKey(player.getUniqueId())){
                    String modelName = PlayerManager.playersReadyToInteract.get(player.getUniqueId());
                    MenuModel model = new MenuModel();
                    model.setDisplayName(modelName);
                    model.setId(modelName);
                    Chest chest = (Chest) e.getClickedBlock().getState();
                    model.setInv(chest.getBlockInventory());

                    player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).blockDetection));

                    PlayerManager.playersReadyToInteract.remove(player.getUniqueId());
                    e.setCancelled(true);
                    try {
                        MenuModel.addModel(modelName,model);
                        MenuManager.saveMenuModel(model);
                        player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).successfully));
                    }catch (Exception error){
                        System.out.println(ChatColor.YELLOW + "Something went wrong while saving model to yml file! please content to plugin developer:\n"+
                                "-> Cause:"+error.getCause()+"\n"+
                                "-> Message:"+error.getMessage());
                        player.sendMessage(MenuMine.color(prefix + PlayerManager.getPlayerLanguage(player).failed));
                    }
                    return;
                }
                if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;
                if (!(e.getItem().getType() == MenuManager.getMenuItem().getType())) return;
                if ((player.getOpenInventory().getTopInventory() == (player.getInventory()))) return;
                NBTItem nbt = NBTItemManager.createNBTItem(e.getItem());
                if (nbt.hasTag("menu")) {
                    player.openInventory(MenuManager.getGUI());
                    e.setCancelled(true);
                }
            }

        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        NBTItem nbt = NBTItemManager.createNBTItem(e.getItemDrop().getItemStack());
        if (nbt.hasTag("menu") || nbt.hasTag("MenuItem")) e.setCancelled(true);

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if (e.getDrops().isEmpty()) return;
        for (ItemStack item : e.getDrops()){
            NBTItem nbt = NBTItemManager.createNBTItem(item);
            if (nbt.hasTag("menu")||nbt.hasTag("MenuItem")) {e.getDrops().remove(item); return;}
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        if (!MenuManager.isMenuDisabled(e.getPlayer()))
            MenuManager.setItemToInventory(e.getPlayer());
    }

    @EventHandler
    public void onCLick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();
        try {
            NBTItem nbt = null;
            if (item != null && !item.getType().equals(Material.AIR)) nbt = NBTItemManager.createNBTItem(item);
            if (nbt != null){
                if (nbt.hasTag("menu")) e.setCancelled(true);
                if (nbt.hasTag("MenuItem")){
                    if (nbt.hasTag("FilterItem")) e.setCancelled(true);
                    if (!e.isCancelled()){
                        MenuModel model = MenuModel.getModels().get(nbt.getString("MenuModel"));
                        for (MenuAction action : model.getActions(e.getSlot()))
                            try {action.run((Player) e.getWhoClicked());}catch (Exception ignored){}

                        e.setCancelled(true);
                    }
                }
            }
        }catch (Exception error){error.printStackTrace();}

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
