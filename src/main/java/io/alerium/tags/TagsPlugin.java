package io.alerium.tags;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.haroldstudios.hexitextlib.HexResolver;
import io.alerium.tags.commands.ReloadCommand;
import io.alerium.tags.hooks.NoPlaceholderHook;
import io.alerium.tags.hooks.PAPIHook;
import io.alerium.tags.hooks.PlaceholderHook;
import io.alerium.tags.listeners.PlayerListener;
import io.alerium.tags.managers.TagManager;
import io.alerium.tags.objects.TagGroup;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TagsPlugin extends JavaPlugin {

    @Getter private static TagsPlugin instance;

    @Getter private PlaceholderHook placeholderHook;
    
    @Getter private TagManager tagManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        setupDepends();
        registerInstances();
        registerListeners();
        registerCommands();

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer container = event.getPacket();
                double x = container.getDoubles().read(0);
                double y = container.getDoubles().read(1);
                double z = container.getDoubles().read(2);

                tagManager.getTagGroup(event.getPlayer().getUniqueId()).moveEntity(x, y, z);
            }
        });
        
        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        tagManager.getTagGroups().values().forEach(TagGroup::despawn);
    }

    /**
     * This method gets a message from the config file
     * @param path The path of the message
     * @return The String with the message
     */
    public String getMessage(String path) {
        return HexResolver.parseHexString(getConfig().getString("messages." + path));
    }
    
    /**
     * This method setups all the dependencies
     */
    private void setupDepends() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            placeholderHook = new PAPIHook();
        else 
            placeholderHook = new NoPlaceholderHook();
    }

    /**
     * This method registers all the instances
     */
    private void registerInstances() {
        tagManager = new TagManager();
        tagManager.enable();
    }

    /**
     * This method registers all the Listener(s)
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    /**
     * This method registers all the commands
     */
    private void registerCommands() {
        getCommand("tagsreload").setExecutor(new ReloadCommand());
    }
    
}
