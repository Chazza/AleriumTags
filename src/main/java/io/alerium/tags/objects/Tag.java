package io.alerium.tags.objects;

import io.alerium.tags.TagsPlugin;
import io.alerium.tags.api.events.TagUpdateEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

@RequiredArgsConstructor
public class Tag {
    
    @Getter private final String id;
    @Getter private final int priority;
    private final int refreshInterval;
    
    @Getter private final List<String> texts;
    
    private BukkitTask updateTask;

    /**
     * This method starts the update task of this Tag
     */
    public void startTask() {
        if (updateTask != null)
            stopTask();
        
        updateTask = Bukkit.getScheduler().runTaskTimer(TagsPlugin.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(new TagUpdateEvent(this));

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (TagsPlugin.getInstance().getTagManager().getTag(player) != this)
                    continue;
                
                TagGroup group = TagsPlugin.getInstance().getTagManager().getTagGroup(player.getUniqueId());
                group.updateTag(texts);
            }
        }, refreshInterval, refreshInterval);
    }
    
    /**
     * This method stops the update task of this Tag
     */
    public void stopTask() {
        if (updateTask == null)
            return;
        
        updateTask.cancel();
        updateTask = null;
    }
    
}
