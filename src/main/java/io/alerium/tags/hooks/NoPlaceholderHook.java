package io.alerium.tags.hooks;

import com.haroldstudios.hexitextlib.HexResolver;
import org.bukkit.entity.Player;

public class NoPlaceholderHook implements PlaceholderHook {
    
    @Override
    public String parsePlaceholders(Player player, String text) {
        return HexResolver.parseHexString(text.replaceAll("%player_name%", player.getName()));
    }
    
}
