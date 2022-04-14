package com.alant7_.util.holograms.objects.nms;

import com.alant7_.util.holograms.objects.CraftHologramLine;
import com.alant7_.util.holograms.objects.CraftTextLine;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class v1_18_R2 extends HologramLineNMS<Packet<?>, Entity, EntityArmorStand, EntitySlime> {

    public v1_18_R2(CraftHologramLine hologramLine) {
        super(hologramLine);
    }

    @Override
    public void createLine(@NotNull Player player) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(armorStand);
        PacketPlayOutSpawnEntity packet1 = new PacketPlayOutSpawnEntity(slime);
        sendPacket(player, packet);
        sendPacket(player, packet1);
    }

    @Override
    public void updateLine(@NotNull Player player) {
        var formattedLine = ChatColor.translateAlternateColorCodes('&', ((CraftTextLine) hologramLine).getText());
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            formattedLine = PlaceholderAPI.setPlaceholders(player, formattedLine);

        armorStand.a(new ChatComponentText(formattedLine));
        sendPacket(player, new PacketPlayOutEntityMetadata(armorStand.ae(), armorStand.ai(), true));
    }

    @Override
    public void setEntityLocation(Entity entity, Location location) {
        entity.b(new Vec3D(location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public int getEntityId(Entity entity) {
        return entity.ae();
    }

    @Override
    public void sendEntityRemovePacket(Player player, Entity entity) {
        sendPacket(player, new PacketPlayOutEntityDestroy(entity.ae()));
    }

    @Override
    public void sendEntityTeleportPacket(Player player, Entity entity) {
        sendPacket(player, new PacketPlayOutEntityTeleport(entity));
    }

    @Override
    protected void sendPacket(@NotNull Player player, @NotNull Packet<?> packet) {
        ((CraftPlayer) player).getHandle().b.a(packet);
    }

    @Override
    protected @NotNull EntityArmorStand createArmorStand() {
        var location = hologramLine.getLocation();
        var stand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        stand.n(true);
        stand.t(true);
        stand.j(true);
        stand.e(true);

        return stand;
    }

    @Override
    protected @NotNull EntitySlime createSlime() {
        Location location = hologramLine.getLocation();
        EntitySlime slime = new EntitySlime(EntityTypes.aD, ((CraftWorld) location.getWorld()).getHandle());
        slime.b(new Vec3D(location.getX(), location.getY(), location.getZ()));
        slime.dX().put(MobEffectList.a(14), new MobEffect(MobEffectList.a(14), 0, 0, false, false));

        try {
            Method m = EntityLiving.class.getDeclaredMethod("F");
            m.setAccessible(true);
            m.invoke(slime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return slime;
    }

}
