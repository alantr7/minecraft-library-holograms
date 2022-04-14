package com.alant7_.util.holograms.objects;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.holograms.api.HologramManager;
import com.alant7_.util.holograms.api.objects.Hologram;
import com.alant7_.util.holograms.listeners.HologramPacketListener;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CraftHologramManager implements HologramManager {

    private final AlanJavaPlugin plugin;

    private final Map<UUID, Hologram> holograms = new ConcurrentHashMap<>();

    private final Map<Integer, Hologram> hologramInteractableEntities = new ConcurrentHashMap<>();

    public CraftHologramManager(AlanJavaPlugin plugin) {
        this.plugin = plugin;
        plugin.hook("ProtocolLib", () -> ProtocolLibrary.getProtocolManager().addPacketListener(new HologramPacketListener(plugin)));
    }

    @Override
    public Hologram createHologram(@NotNull Location location) {
        var hologram = new CraftHologram(plugin, location);
        holograms.put(hologram.getIdentifier(), hologram);

        return hologram;
    }

    @Override
    public @NotNull Hologram[] getHolograms() {
        return holograms.values().toArray(new Hologram[0]);
    }

    @Override
    public void deleteHolograms() {
        holograms.values().forEach(hologram -> ((CraftHologram) hologram).destroy());
        holograms.clear();
    }

    @Override
    public void deleteHologram(@NotNull Hologram hologram) {
        ((CraftHologram) hologram).destroy();
        holograms.remove(hologram.getIdentifier());
    }

    public void registerInteractableHologram(int entityId, Hologram hologram) {
        hologramInteractableEntities.put(entityId, hologram);
    }

    public Hologram getInteractableHologram(int entityId) {
        return hologramInteractableEntities.get(entityId);
    }

    public void unregisterInteractableHologram(int entityId) {
        hologramInteractableEntities.remove(entityId);
    }

    public void unregisterInteractableHologram(Hologram hologram) {
        for (int key : new ArrayList<>(hologramInteractableEntities.keySet())) {
            if (hologramInteractableEntities.get(key) == hologram)
                hologramInteractableEntities.remove(key);
        }
    }

    public void unregisterListeners() {
        plugin.hook("ProtocolLib", () -> ProtocolLibrary.getProtocolManager().removePacketListeners(plugin));
    }

}
