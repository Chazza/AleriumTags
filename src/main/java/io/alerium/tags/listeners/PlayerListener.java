package io.alerium.tags.listeners;

import io.alerium.tags.TagsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final TagsPlugin plugin = TagsPlugin.getInstance();
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getTagManager().removePlayerTag(event.getPlayer().getUniqueId());
    }
    
}
