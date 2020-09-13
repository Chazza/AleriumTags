package io.alerium.tags.listeners;

import io.alerium.tags.TagsPlugin;
import io.alerium.tags.objects.TagGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerListener implements Listener {
    
    private final TagsPlugin plugin = TagsPlugin.getInstance();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setupScoreboard(player);
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getTagManager().spawnTag(player);
            plugin.getTagManager().restoreTags(player);
        }, 1);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        plugin.getTagManager().removePlayerTag(player.getUniqueId());
        plugin.getTagManager().removeTag(player);
    }
    
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        TagGroup group = plugin.getTagManager().getTagGroup(player.getUniqueId());
        
        group.despawn(event.getFrom());
        plugin.getTagManager().spawnTag(player);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        TagGroup group = plugin.getTagManager().getTagGroup(player.getUniqueId());

        group.despawn();
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        plugin.getTagManager().spawnTag(player);
    }
    
    private void setupScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("AleriumTags");
        if (team == null) {
            team = scoreboard.registerNewTeam("AleriumTags");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

            for (Player p : Bukkit.getOnlinePlayers())
                team.addEntry(p.getName());
            
        }

        team.addEntry(player.getName());
    }
    
}
