package me.ehsanmna.menumine.Managers.controller;

import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.models.PMenuModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.ehsanmna.menumine.MenuMine.color;


public class PlayerMenuController {

   static Map<UUID,PMenuModel> pMenuModels = new HashMap<>();

   //public static

   private static PMenuModel createPMenu(UUID id){
      return new PMenuModel(id);
   }

   private static PMenuModel getPMenu(UUID id){
      if (!pMenuModels.containsKey(id)) return setupPMenu(id);
      return pMenuModels.get(id);
   }

   public static PMenuModel setupPMenu(UUID id){
      if (MenuMine.debug) Bukkit.getServer().getConsoleSender().sendMessage(color("&f[MenuMine] Setting up a PMenu for &e"+id));
      if (!pMenuModels.containsKey(id)) {
         PMenuModel model = createPMenu(id);
         pMenuModels.put(id,model);
         return model;
      }
      else return getPMenu(id);
   }

   public static void openMenuModel(Player player, String menuId, List<String> inputs){
      if (MenuMine.debug) Bukkit.getServer().getConsoleSender().sendMessage(color("&f[MenuMine] Opening a PMenu for &e"+player.getUniqueId()+" &6Id: "+menuId));
      UUID id = player.getUniqueId();
      PMenuModel pMenu = getPMenu(id);
      if (MenuMine.debug) Bukkit.getServer().getConsoleSender().sendMessage(color("&f - &e"+pMenu));
      //MenuManager.openModel(pMenu.getModel(menuId),player,inputs);
      pMenu.getModel(menuId).openMenu(player,inputs);
   }

   public static void openMenuModel(Player player, String menuId){
      openMenuModel(player,menuId,List.of());
   }

   public static Map<UUID, PMenuModel> getPMenuModels() {
      return pMenuModels;
   }

   public static void setPMenuModels(Map<UUID, PMenuModel> pMenuModels) {
      PlayerMenuController.pMenuModels = pMenuModels;
   }
}
