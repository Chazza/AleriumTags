package io.alerium.tags.objects;

import com.haroldstudios.hexitextlib.HexResolver;
import io.alerium.tags.TagsPlugin;
import io.alerium.tags.api.events.TagUpdateEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Tag {
    
    @Getter private final String id;
    @Getter private final int priority;
    private final int refreshInterval;
    
    @Getter private final List<String> texts;
    private List<TagController.TagLine> lines;
    
    private BukkitTask updateTask;

    /**
     * This method starts the update task of this Tag
     */
    public void startTask() {
        if (updateTask != null)
            stopTask();
        
        updateTask = Bukkit.getScheduler().runTaskTimer(TagsPlugin.getInstance(), () -> {
            if (lines == null)
                return;

            Bukkit.getPluginManager().callEvent(new TagUpdateEvent(this));
            
            for (TagController.TagLine line : lines)
                TagsPlugin.getInstance().getMultiLineAPI().update(line);
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

    /**
     * This method gets the TagLine(s) of this Tag
     * @return A List of TagLine(s)
     */
    public List<TagController.TagLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
            texts.forEach(s -> lines.add(new MultilineTagLine(HexResolver.parseHexString(s))));
        }
        
        return lines;
    }
    
}
