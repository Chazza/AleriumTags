package io.alerium.tags.utils;

import net.minecraft.server.v1_16_R1.Entity;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public final class NMSUtil {

    private NMSUtil() {
    }

    /**
     * This method gets the next entity id
     * @return the next entity id
     */
    public static int getNextEntityID() {
        try {
            Field field = Entity.class.getDeclaredField("entityCount");
            field.setAccessible(true);
            AtomicInteger ids = (AtomicInteger) field.get(null);
            
            return ids.incrementAndGet();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
}
