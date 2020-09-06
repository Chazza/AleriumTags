package io.alerium.tags.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.alerium.tags.TagsPlugin;
import io.alerium.tags.objects.Tag;
import io.alerium.tags.objects.TagGroup;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TagManager {
    
    private final TagsPlugin plugin = TagsPlugin.getInstance();
    
    @Getter private final Map<String, Tag> tags = new HashMap<>();
    private final Cache<UUID, String> tagCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    @Getter private final Map<UUID, TagGroup> tagGroups = new HashMap<>();
    private final Map<UUID, List<String>> playerTags = new HashMap<>();
    
    /**
     * This method enables the TagManager
     */
    public void enable() {
        loadTags();
        Bukkit.getOnlinePlayers().forEach(this::spawnTag);
    }

    /**
     * This method reloads all the Tag(s)
     */
    public void reload() {
        tags.forEach((id, tag) -> tag.stopTask());
        tags.clear();
        tagCache.invalidateAll();
        
        loadTags();
        Bukkit.getOnlinePlayers().forEach(this::spawnTag);
    }
    
    /**
     * This method loads all the tags from the config file
     */
    private void loadTags() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("groups");
        for (String id : section.getKeys(false)) {
            tags.put(id, new Tag(
                    id,
                    section.getInt(id + ".priority"),
                    section.getInt(id + ".refreshInterval"),
                    section.getStringList(id + ".lines")
            ));
        }
        
        plugin.getLogger().info("Loaded " + tags.size() + " tags.");
    }

    /**
     * This method spawns a Tag of a Player
     * @param player The Player
     */
    public void spawnTag(Player player) {
        TagGroup group = tagGroups.get(player.getUniqueId());
        if (group == null) {
            group = new TagGroup(player.getUniqueId());
            tagGroups.put(player.getUniqueId(), group);
        }
        
        if (playerTags.containsKey(player.getUniqueId())) {
            group.spawnTag(playerTags.get(player.getUniqueId()));
            return;
        }
        
        Tag tag = plugin.getTagManager().getTag(player);
        if (tag != null)
            group.spawnTag(tag.getTexts());
    }

    /**
     * This method gets the TagGroup of a Player
     * @param uuid The UUID of the Player
     * @return The TagGroup
     */
    public TagGroup getTagGroup(UUID uuid) {
        return tagGroups.get(uuid);
    }
    
    /**
     * This method sets a personalized Tag for a Player
     * @param player The UUID of the Player
     * @param tags A List of String(s)
     */
    public void setPlayerTag(UUID player, List<String> tags) {
        playerTags.put(player, tags);
        
        TagGroup group = tagGroups.get(player);
        group.spawnTag(tags);
    }

    /**
     * This method removes the personalized Tag of a Player
     * @param player The UUID of the Player
     */
    public void removePlayerTag(UUID player) {
        playerTags.remove(player);
    }

    /**
     * This method gets the personalized Tag of a Player
     * @param player The UUID of the Player
     * @return A List of String(s)
     */
    public List<String> getPlayerTag(UUID player) {
        return playerTags.get(player);
    }

    /**
     * This method gets the Tag of a Player
     * @param player The Player
     * @return The Tag of this Player
     */
        public Tag getTag(Player player) {
        String id = tagCache.getIfPresent(player.getUniqueId());
        if (id == null) {
            id = getTagOf(player);
            if (id != null)
                tagCache.put(player.getUniqueId(), id);
        }
        
        return tags.get(id);
    }

    /**
     * This method checks the Tag that the Player should have
     * @param player The Player
     * @return The Tag of the Player
     */
    private String getTagOf(Player player) {
        Tag playerTag = null;
        for (Map.Entry<String, Tag> entry : tags.entrySet()) {
            if (!player.hasPermission("tags.group." + entry.getKey()))
                continue;

            if (playerTag != null && playerTag.getPriority() > entry.getValue().getPriority())
                continue;

            playerTag = entry.getValue();
        }
        
        if (playerTag == null)
            return null;
        
        return playerTag.getId();
    }
    
}
