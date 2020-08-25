package io.alerium.tags.hooks;

import org.bukkit.entity.Player;

public interface PlaceholderHook {

    /**
     * This method parses all the placeholders
     * @param player The Player
     * @param text The String of text to parse
     * @return The String with all the placeholders
     */
    String parsePlaceholders(Player player, String text);
    
}
