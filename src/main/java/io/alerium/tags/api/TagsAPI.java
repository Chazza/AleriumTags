package io.alerium.tags.api;

import io.alerium.tags.TagsPlugin;
import io.alerium.tags.objects.MultilineTagLine;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class TagsAPI {

    private static final TagsPlugin plugin = TagsPlugin.getInstance();
    
    private TagsAPI() {
    }

    /**
     * This method sets a personalized tag for a Player
     * @param player The Player
     * @param tags The personalized tag
     */
    public static void setTag(Player player, List<String> tags) {
        plugin.getTagManager().setPlayerTag(player.getUniqueId(), tags);
    }

    /**
     * This method resets the tag of a Player
     * @param player The Player
     */
    public static void resetTag(Player player) {
        plugin.getTagManager().removePlayerTag(player.getUniqueId());
    }

    /**
     * This method gets the personalized tag of a Player
     * @param player The Player
     * @return The List of String(s) with the tag, null if not found
     */
    public static List<String> getPlayerTag(Player player) {
        List<TagController.TagLine> lines = plugin.getTagManager().getPlayerTag(player.getUniqueId());
        if (lines == null)
            return null;
        
        return lines.stream()
                .map(line -> ((MultilineTagLine) line).getText())
                .collect(Collectors.toList());
    }
    
}
