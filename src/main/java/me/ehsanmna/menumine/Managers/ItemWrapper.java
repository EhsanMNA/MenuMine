package me.ehsanmna.menumine.Managers;

import me.ehsanmna.menumine.MenuMine;
import me.ehsanmna.menumine.utils.SkullUtils;
import me.ehsanmna.menumine.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class ItemWrapper {

    public static void wrapItemToPath(ConfigurationSection section,ItemStack item,int slot){
        section.set(slot +".material", item.getType().toString());
        section.set(slot +".name", item.getItemMeta().getDisplayName());
        section.set(slot +".slot", slot);
        if (item.getItemMeta().hasLore()) section.set(slot +".lore", item.getItemMeta().getLore());
    }

    public static ItemStack wrapItem(YamlConfiguration yml, ConfigurationSection section){
        ItemStack item = null;
        try {
            String materialStr = section.getString("material");
            assert materialStr != null;
            if (materialStr.equalsIgnoreCase("skull")) {
                if (section.contains("skull")) {
                    String skullId = section.getString("skull");
                    item = XMaterial.PLAYER_HEAD.parseItem();
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    assert skullId != null;
                    SkullUtils.applySkin(meta, skullId);
                    item.setItemMeta(meta);
                }
            } else if(materialStr.equalsIgnoreCase("potion"))
                item = getPotionItemStack(Material.valueOf(materialStr),
                        PotionType.valueOf(section.getString("potion")),
                        Integer.parseInt(section.getString("level")),
                        false, false);
            else item = XMaterial.valueOf(materialStr.toUpperCase()).parseItem();
        }catch (Exception error){
            item = XMaterial.STONE.parseItem();
        }
        String displayName = MenuMine.color(section.getString("name"));
        assert item != null;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        if (section.contains("lore")) meta.setLore(MenuMine.color(section.getStringList("lore")));
        if (section.contains("glow") && !(item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())))
            if (section.getBoolean("glow")){
                meta.addEnchant(Enchantment.LURE,1,true);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
            }
        if (section.contains("flags")){
            List<String> flags = section.getStringList("flags");
            for (String flagName : flags)
                try {
                    meta.addItemFlags(ItemFlag.valueOf(flagName));
                }catch (IllegalArgumentException error){}

        }
        item.setItemMeta(meta);

        return item;
    }

    static ItemStack getPotionItemStack(Material potionType, PotionType type, int level, boolean extend, boolean upgraded){
        ItemStack potion;
        try {
            PotionData potionData = new PotionData(type, extend, upgraded);
            potion = new ItemStack(potionType);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            assert meta != null;
            meta.setBasePotionData(potionData);
            potion.setItemMeta(meta);
        }catch (NoClassDefFoundError error){
            potion = XMaterial.STONE.parseItem();
        }
        return potion;
    }


}
