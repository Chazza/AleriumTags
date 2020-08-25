package io.alerium.tags.objects;

import io.alerium.tags.TagsPlugin;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class MultilineTagController implements TagController {
    
    private final TagsPlugin plugin = TagsPlugin.getInstance();
    
    @Override
    public List<TagLine> getFor(Entity target) {
        if (!(target instanceof Player))
            return Collections.emptyList();
        
        Player player = (Player) target;
        List<TagLine> playerTags = plugin.getTagManager().getPlayerTag(player.getUniqueId());
        if (playerTags != null)
            return playerTags;
        
        Tag tag = plugin.getTagManager().getTag(player);
        if (tag == null)
            return Collections.emptyList();
        
        return tag.getLines();
    }

    @Override
    public EntityType[] getAutoApplyFor() {
        return new EntityType[] {
                EntityType.PLAYER
        };
    }

    @Override
    public JavaPlugin getPlugin() {
        return TagsPlugin.getInstance();
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getNamePriority() {
        return 0;
    }

    @Override
    public String getName(Entity target, Player viewer, String previous) {
        return null;
    }
}
