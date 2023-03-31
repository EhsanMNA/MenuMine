package me.ehsanmna.menumine.Managers.economy;

import org.bukkit.entity.Player;

public interface Economy {

    public double getMoney(Player player);

    public boolean hasMoney(Player player,float money);

    public void takeMoney(Player player,float money);

    public void addMoney(Player player,float money);

    public boolean setupEconomy();

}
