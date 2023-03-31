package me.ehsanmna.menumine.Managers.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomy implements Economy {

    net.milkbowl.vault.economy.Economy econ = null;


    @Override
    public double getMoney(Player player) {
        return econ.getBalance(player);
    }

    @Override
    public boolean hasMoney(Player player, float money) {
        return econ.has(player,money);
    }

    @Override
    public void takeMoney(Player player, float money) {
        econ.withdrawPlayer(player,money);
    }

    @Override
    public void addMoney(Player player, float money) {
        econ.depositPlayer(player,money);
    }

    @Override
    public boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


}
