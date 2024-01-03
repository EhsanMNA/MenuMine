package me.ehsanmna.menumine.events;

import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.nbt.NBTItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SwapHandItemsEvent implements Listener {

    @EventHandler
    public void onItemHandChangeEvent(PlayerSwapHandItemsEvent e){
        if (!Storage.moveItem)
            if (NBTItemManager.createNBTItem(e.getMainHandItem()).hasTag("menu") || NBTItemManager.createNBTItem(e.getOffHandItem()).hasTag("menu"))
                e.setCancelled(true);
    }

}
