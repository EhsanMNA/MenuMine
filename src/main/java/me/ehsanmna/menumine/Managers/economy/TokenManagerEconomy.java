package me.ehsanmna.menumine.Managers.economy;

import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TokenManagerEconomy implements Economy{

    static TokenManager api;

    @Override
    public double getMoney(Player player) {
        return (double) api.getTokens(player).getAsLong();
    }

    @Override
    public boolean hasMoney(Player player, float money) {
        return api.getTokens(player).equals(money);
    }

    @Override
    public void takeMoney(Player player, float money) {
        api.removeTokens(player, (long) money);
    }

    @Override
    public void addMoney(Player player, float money) {
        api.addTokens(player, (long) money);
    }

    @Override
    public boolean setupEconomy() {
        try {api = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
        }catch (Exception ignored){return false;}
        return true;
    }
}
