package me.ehsanmna.menumine.Tasks;

import me.ehsanmna.menumine.Managers.Storage;
import me.ehsanmna.menumine.MenuMine;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;

public class RefreshTask{

    BukkitTask task;


        public void run(){
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Storage.refreshData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskLater(MenuMine.getInstance(),20 * 120 );
        }
        public void stop(){
            task.cancel();
        }
}
