package me.ehsanmna.menumine.Managers.economy;

import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;

public class EconomyManager {

    public static EconomyType economyType;
    public static Economy economy;





    public static void setup(EconomyType type){
        boolean log = MenuMine.logMessages;
        try {
            switch (type) {
                case VAULT -> {
                    economyType = type;
                    Storage.economyUse = true;
                    try {
                        economy = new VaultEconomy();
                        if (!economy.setupEconomy())
                            if (log) System.out.println(MenuMine.color("&cEconomy system got an error!"));
                    } catch (Exception error) {
                        if (log)
                            System.out.println(MenuMine.color("&cCouldn't setup the economy system! because of " + error.getMessage() + " &e" + error.getCause()));
                    }
                }
                case TOKENMANAGER -> {
                    economyType = type;
                    Storage.economyUse = true;
                    try {
                        economy = new TokenManagerEconomy();
                        if (!economy.setupEconomy())
                            if (log) System.out.println(MenuMine.color("&cEconomy system got an error!"));
                    } catch (Exception error) {
                        if (log)
                            System.out.println(MenuMine.color("&cCouldn't setup the economy system! because of " + error.getMessage() + " &e" + error.getCause()));
                    }
                }
            }
        }catch (Exception error){
            if (log) System.out.println(MenuMine.color("&cCouldn't setup the economy system!"));
        }
    }



}
