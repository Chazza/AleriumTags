package io.alerium.tags.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PAPIHook implements PlaceholderHook {
    
    @Override
    public String parsePlaceholders(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }
    
}
