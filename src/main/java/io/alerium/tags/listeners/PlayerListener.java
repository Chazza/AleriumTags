package io.alerium.tags.listeners;

import io.alerium.tags.TagsPlugin;
import io.alerium.tags.objects.TagGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final TagsPlugin plugin = TagsPlugin.getInstance();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getTagManager().spawnTag(player), 1);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        plugin.getTagManager().removePlayerTag(player.getUniqueId());
        plugin.getTagManager().getTagGroup(player.getUniqueId()).despawn();
    }
    
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        TagGroup group = plugin.getTagManager().getTagGroup(player.getUniqueId());
        
        group.despawn(event.getFrom());
        plugin.getTagManager().spawnTag(player);
    }
    
}
