package io.alerium.tags.objects;

import io.alerium.tags.TagsPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MultilineTagLine implements TagController.TagLine {
    
    @Getter private final String text;
    
    @Override
    public String getText(Entity target, Player viewer) {
        if (!(target instanceof Player))
            return "";
        
        return TagsPlugin.getInstance().getPlaceholderHook().parsePlaceholders((Player) target, text);
    }

    @Override
    public boolean keepSpaceWhenNull(Entity target) {
        return true;
    }

}
