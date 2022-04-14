package com.alant7_.util.holograms.objects.nms;

import com.alant7_.util.holograms.objects.CraftHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class HologramLineNMS<PacketClass, EntityClass, ArmorStandClass extends EntityClass, SlimeClass extends EntityClass> {

    protected final CraftHologramLine hologramLine;

    protected final ArmorStandClass armorStand;

    protected final SlimeClass slime;

    public HologramLineNMS(CraftHologramLine hologramLine) {
        this.hologramLine = hologramLine;
        this.armorStand = createArmorStand();
        this.slime = createSlime();
    }

    public abstract void createLine(@NotNull Player player);

    public abstract void updateLine(@NotNull Player player);

    public abstract void setEntityLocation(EntityClass entity, Location location);

    public void setLocation(Location location) {
        setEntityLocation(armorStand, location);
        setEntityLocation(slime, location);

        Bukkit.getOnlinePlayers().forEach(p -> {
            sendEntityTeleportPacket(p, armorStand);
            sendEntityTeleportPacket(p, slime);
        });
    }

    public void removeLine(Player player) {
        sendEntityRemovePacket(player, armorStand);
        sendEntityRemovePacket(player, slime);
    }

    public abstract int getEntityId(EntityClass entity);

    public int getArmorStandId() {
        return getEntityId(armorStand);
    }

    public int getSlimeId() {
        return getEntityId(slime);
    }

    public abstract void sendEntityRemovePacket(Player player, EntityClass entity);

    public abstract void sendEntityTeleportPacket(Player player, EntityClass entity);

    protected abstract void sendPacket(@NotNull Player player, @NotNull PacketClass packet);

    protected abstract @NotNull ArmorStandClass createArmorStand();

    protected abstract @NotNull SlimeClass createSlime();

}
