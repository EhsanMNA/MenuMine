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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {


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
                    model.setDisplayName("&a"+modelName);
                    model.setName(modelName);
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
                        if (MenuMine.logMessages) System.out.println(ChatColor.YELLOW + "[MenuMine] Something went wrong while saving model to yml file! please content to plugin developer:\n"+
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
                    for (MenuAction action : MenuMine.mainActions) action.run(player,MenuManager.getMenuItem());
                    e.setCancelled(true);
                }
            }

        }

    }


}
