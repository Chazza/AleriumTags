package io.alerium.tags.utils;

import io.alerium.tags.TagsPlugin;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public final class NMSUtil {

    private NMSUtil() {
    }

    private static Class entityClass;
    private static Field entityCountField;

    /**
     * This method setups the NMSUtil
     * @return True if the setup was done correctly
     */
    public static boolean setup() {
        try {
            entityClass = Class.forName(getNMSPackage() + ".Entity");
            entityCountField = entityClass.getDeclaredField("entityCount");
            return true;
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            TagsPlugin.getInstance().getLogger().log(Level.SEVERE, "An error occurred while loading NMSUtil (probably this version is not supported)", e);
            return false;
        }
    }
    
    /**
     * This method gets the next entity id
     * @return the next entity id
     */
    public static int getNextEntityID() {
        try {
            entityCountField.setAccessible(true);
            AtomicInteger ids = (AtomicInteger) entityCountField.get(null);
            entityCountField.setAccessible(false);
            
            return ids.incrementAndGet();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This method gets the NMS package name
     * @return The NMS package name
     */
    private static String getNMSPackage() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return "net.minecraft.server." + version;
    }
    
}
