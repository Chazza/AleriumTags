package io.alerium.tags.objects;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import io.alerium.tags.packetwrapper.AbstractPacket;
import io.alerium.tags.packetwrapper.WrapperPlayServerEntityMetadata;
import io.alerium.tags.packetwrapper.WrapperPlayServerRelEntityMove;
import io.alerium.tags.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class TagEntity {
    
    @Getter private final int id;
    
    private double x;
    private double y;
    private double z;

    /**
     * This method constructs the AbstractPacket to spawn the TagEntity
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return The AbstractPacket
     */
    public AbstractPacket spawnEntity(double x, double y, double z) {
        WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving();
        packet.setUniqueId(UUID.randomUUID());
        packet.setEntityID(id);
        packet.setType(1);

        packet.setX(x);
        packet.setY(y);
        packet.setZ(z);
        packet.setHeadPitch(0);
        packet.setPitch(0);
        packet.setYaw(0);
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        return packet;
    }

    /**
     * This method constructs the AbstractPacket to change the name to the TagEntity
     * @param name The name of the TagEntity
     * @return The AbstractPacket
     */
    public AbstractPacket updateEntityName(String name) {
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
        packet.setEntityID(id);
        
        List<WrappedWatchableObject> meta = packet.getMetadata();
        meta.add(new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20));
        meta.add(new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), Optional.of(WrappedChatComponent.fromChatMessage(name)[0].getHandle())));
        meta.add(new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true));
        meta.add(new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true));
        packet.setMetadata(meta);
        
        return packet;
    }

    /**
     * This method constructs the AbstractPacket to move the TagEntity in a specific position
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return The AbstractPacket
     */
    public AbstractPacket teleportEntity(double x, double y, double z) {
        if (this.x == x && this.y == y && this.z == z)
            return null;
        
        WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove();
        packet.setEntityID(id);
        packet.setDx(x - this.x);
        packet.setDy(y - this.y);
        packet.setDz(z - this.z);
        packet.setYaw(0);
        packet.setPitch(0);
        packet.setOnGround(false);
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        return packet;
    }
    
}
