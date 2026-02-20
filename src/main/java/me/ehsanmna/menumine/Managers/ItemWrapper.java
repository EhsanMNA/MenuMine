package me.ehsanmna.menumine.Managers;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import com.cryptomorin.xseries.reflection.XReflection;
import me.ehsanmna.menumine.MenuMine;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemWrapper {

    public static void wrapItemToPath(ConfigurationSection section,ItemStack item,int slot){
        section.set(slot +".material", item.getType().toString());
        section.set(slot +".name", Objects.requireNonNull(item.getItemMeta()).getDisplayName());
        try {if (XReflection.supports(16)) if (item.getItemMeta().hasCustomModelData()) section.set(slot +".modeldata", item.getItemMeta().getCustomModelData());}
        catch (Exception ignored){}
        section.set(slot +".slot", slot);
        if (item.getItemMeta().hasLore()) section.set(slot +".lore", item.getItemMeta().getLore());
        if (!item.getItemMeta().getItemFlags().isEmpty()){
            Set<ItemFlag> flags = item.getItemMeta().getItemFlags();
            List<String> fStr = new ArrayList<>();
            for (ItemFlag f : flags) fStr.add(f.toString());
            try {section.set(slot + ".flags",fStr);
            }catch (IllegalArgumentException ignored){}
        }
        try {
            if (item.getItemMeta().hasCustomModelData())
                section.set(slot +".customModelData", item.getItemMeta().getCustomModelData());
        }catch (Exception ignored){}
        if (item.getType().equals(XMaterial.PLAYER_HEAD.get())) {
            section.set(slot + ".skull", XSkull.of(item).getProfileValue());
            section.set(slot +".material", "skull");
        }
    }

    public static ItemStack wrapItem(ConfigurationSection section){
        ItemStack item = null;
        try {
            String materialStr = section.getString("material");
            assert materialStr != null;
            if (materialStr.equalsIgnoreCase("skull")) {
                if (section.contains("skull")) {
                    String skullId = section.getString("skull");
                    assert skullId != null;
                    item = XSkull.createItem().profile(Profileable.of(ProfileInputType.BASE64.getProfile(skullId), true)).apply();
                }
            }
            else if(section.getString("type","null").equalsIgnoreCase("potion")){
                // type:  POTION COLOR EFFECTS
                String[] colors = section.getString("color","100,100,100").split(",");
//                Set<PotionType> types = new HashSet<>();
//                for (String tName : section.getStringList("effects"))
//                    types.add(PotionType.valueOf(tName));
                item = XPotion.buildItemWithEffects(XMaterial.matchXMaterial(materialStr).orElse(XMaterial.POTION).get(),
                        Color.fromRGB(Integer.parseInt(colors[0]),Integer.parseInt(colors[1]),Integer.parseInt(colors[2])));
            }
            else {item = XMaterial.valueOf(materialStr.toUpperCase()).parseItem();}
        }catch (Exception error){
            item = XMaterial.STONE.parseItem();
            MenuMine.getInstance().getLogger().warning("Could not detect tje type of "+section.getName() +" material!");
        }
        String displayName = MenuMine.color(section.getString("name", "Not defined"));
        assert item != null;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        try {
            if (section.contains("customModelData"))
                meta.setCustomModelData(section.getInt("customModelData",0));
        }catch (Exception ignored){
            MenuMine.getInstance().getLogger().warning("Could not set custom model data!");
        }
        if (section.contains("lore")) meta.setLore(MenuMine.color(section.getStringList("lore")));
        if (section.contains("amount")) item.setAmount(section.getInt("amount",1));
        if (section.contains("glow") && !(item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())))
            if (section.getBoolean("glow")){
                meta.addEnchant(Enchantment.LURE,1,true);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
            }
        if (section.contains("flags")){
            List<String> flags = section.getStringList("flags");
            for (String flagName : flags) try {meta.addItemFlags(ItemFlag.valueOf(flagName));}catch (IllegalArgumentException ignored){}
        }
        item.setItemMeta(meta);

        return item;
    }

    public static void wrapFilterToPath(ConfigurationSection section,ItemStack item,int slot){
        if (section.contains("filter."+item.getType().name())){
            List<Integer> slots = section.getIntegerList("filter."+item.getType().name()+".slots");
            slots.add(slot);
            section.set("filter."+item.getType().name()+".slots",slots);
            return;
        }
        section.set("filter."+item.getType().name()+".material",item.getType().name());
        section.set("filter."+item.getType().name()+".type","slot");
        section.set("filter."+item.getType().name()+".name","&r ");
        section.set("filter."+item.getType().name()+".slots",List.of(slot));
    }

//    static ItemStack getPotionItemStack(Material potionType, PotionType type, int level, boolean extend, boolean upgraded){
//        ItemStack potion;
//        try {
//            PotionData potionData = new PotionData(type, extend, upgraded);
//            potion = new ItemStack(potionType);
//            PotionMeta meta = (PotionMeta) potion.getItemMeta();
//            assert meta != null;
//            meta.setBasePotionData(potionData);
//            potion.setItemMeta(meta);
//        }catch (NoClassDefFoundError error){
//            potion = XMaterial.STONE.parseItem();
//        }
//        return potion;
//    }


}
