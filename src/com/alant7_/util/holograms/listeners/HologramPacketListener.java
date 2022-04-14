package com.alant7_.util.holograms.listeners;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.holograms.api.objects.Hologram;
import com.alant7_.util.holograms.objects.CraftHologram;
import com.alant7_.util.holograms.objects.CraftHologramManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramPacketListener extends PacketAdapter {

    private final Map<UUID, Long> cooldown = new HashMap<>();

    public HologramPacketListener(Plugin plugin) {
        super(plugin, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        int entityId = event.getPacket().getIntegers().read(0);
        Hologram hologram = ((CraftHologramManager) ((AlanJavaPlugin) plugin).getHologramManager()).getInteractableHologram(entityId);

        if (hologram != null) {
            if (cooldown.containsKey(event.getPlayer().getUniqueId())) {
                if (cooldown.get(event.getPlayer().getUniqueId()) > System.currentTimeMillis())
                    return;
            }
            ((CraftHologram) hologram).notifyInteractionListeners(event.getPlayer());
            cooldown.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 250);
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {}

}
