package com.alant7_.util.holograms.listeners;

import com.alant7_.util.holograms.api.HologramManager;
import com.alant7_.util.holograms.api.objects.Hologram;
import com.alant7_.util.holograms.objects.CraftHologram;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class HologramEventListener implements Listener {

    private final HologramManager hologramManager;

    public HologramEventListener(HologramManager hologramManager) {
        this.hologramManager = hologramManager;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        for (Hologram hologram : hologramManager.getHolograms())
            ((CraftHologram) hologram).send(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Hologram hologram : hologramManager.getHolograms()) {
            ((CraftHologram) hologram).send(event.getPlayer());
        }
    }

}
