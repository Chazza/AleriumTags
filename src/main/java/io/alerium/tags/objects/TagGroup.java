package io.alerium.tags.objects;

import io.alerium.tags.TagsPlugin;
import io.alerium.tags.packetwrapper.AbstractPacket;
import io.alerium.tags.packetwrapper.WrapperPlayServerEntityDestroy;
import io.alerium.tags.utils.NMSUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TagGroup {
    
    private final UUID owner;
    private final List<TagEntity> entities = new ArrayList<>();

    /**
     * This method spawns the TagGroup
     * @param texts The text to display
     */
    public void spawnTag(List<String> texts) {
        if (!entities.isEmpty())
            despawn(getWorld());

        Player player = getPlayer();
        Location location = player.getLocation();
        double y = location.getY();
        List<AbstractPacket> packets = new ArrayList<>();
        for (String text : texts) {
            TagEntity entity = new TagEntity(NMSUtil.getNextEntityID());
            packets.add(entity.spawnEntity(location.getX(), y, location.getZ()));
            packets.add(entity.updateEntityName(TagsPlugin.getInstance().getPlaceholderHook().parsePlaceholders(player, text)));
            
            entities.add(entity);
            y += 0.25;
        }
        
        sendPackets(player.getWorld(), packets.toArray(new AbstractPacket[0]));
    }

    /**
     * This method spawns the Tag for a Player
     * @param player The Player
     */
    public void spawnTag(Player player) {
        List<AbstractPacket> packets = new ArrayList<>();
        for (TagEntity entity : entities) {
            packets.add(entity.spawnEntity(entity.getX(), entity.getY(), entity.getZ()));
            packets.add(entity.updateEntityName(entity.getName()));
        }
        
        packets.forEach(packet -> packet.sendPacket(player));
    }

    /**
     * This method updates the text on the TagGroup
     * @param texts The text to display
     */
    public void updateTag(List<String> texts) {
        if (entities.isEmpty())
            return;
        
        Player player = getPlayer();
        List<AbstractPacket> packets = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            TagEntity entity = entities.get(i);
            String text = texts.get(i);

            packets.add(entity.updateEntityName(TagsPlugin.getInstance().getPlaceholderHook().parsePlaceholders(player, text)));
        }
        
        sendPackets(player.getWorld(), packets.toArray(new AbstractPacket[0]));
    }

    /**
     * This method moves the TagGroup to a specific position
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     */
    public void moveEntity(double x, double y, double z) {
        if (entities.isEmpty())
            return;
        
        List<AbstractPacket> packets = new ArrayList<>();
        for (TagEntity entity : entities) {
            AbstractPacket packet = entity.teleportEntity(x, y, z);
            if (packet != null)
                packets.add(packet);

            y += 0.25;
        }
        
        if (!packets.isEmpty())
            sendPackets(getWorld(), packets.toArray(new AbstractPacket[0]));
    }

    /**
     * This method despawns the TagGroup
     */
    public void despawn() {
        despawn(getWorld());
    }

    /**
     * This method despawns the TagGroup in a specific World
     * @param world The World
     */
    public void despawn(World world) {
        if (entities.isEmpty())
            return;
        
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntityIds(entities.stream().mapToInt(TagEntity::getId).toArray());
        
        entities.clear();
        sendPackets(world, packet);
    }
    
    private World getWorld() {
        return getPlayer().getWorld();
    }
    
    private Player getPlayer() {
        return Bukkit.getPlayer(owner);
    }
    
    private void sendPackets(World world, AbstractPacket... packets) {
        world.getPlayers().forEach(player -> {
            if (player.getUniqueId().equals(owner))
                return;

            for (AbstractPacket packet : packets)
                packet.sendPacket(player);
        });
    }
    
}
