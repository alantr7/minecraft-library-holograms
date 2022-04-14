package com.alant7_.util.holograms.tasks;

import com.alant7_.util.PeriodicTask;
import com.alant7_.util.holograms.api.HologramManager;
import com.alant7_.util.holograms.api.objects.Hologram;
import com.alant7_.util.holograms.objects.CraftHologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HologramUpdateTask extends PeriodicTask {

    private final HologramManager manager;

    public HologramUpdateTask(HologramManager manager) {
        this.manager = manager;
    }

    @Override
    protected void tick() {
        for (Hologram hologram: manager.getHolograms()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ((CraftHologram) hologram).update(player);
            }
        }
    }

}
