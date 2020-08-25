package io.alerium.tags.hooks;

import org.bukkit.entity.Player;

public class NoPlaceholderHook implements PlaceholderHook {
    
    @Override
    public String parsePlaceholders(Player player, String text) {
        return text.replaceAll("%player_name%", player.getName());
    }
    
}
