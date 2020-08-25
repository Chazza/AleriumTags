package io.alerium.tags;

import com.haroldstudios.hexitextlib.HexResolver;
import io.alerium.tags.commands.ReloadCommand;
import io.alerium.tags.hooks.NoPlaceholderHook;
import io.alerium.tags.hooks.PAPIHook;
import io.alerium.tags.hooks.PlaceholderHook;
import io.alerium.tags.listeners.PlayerListener;
import io.alerium.tags.managers.TagManager;
import lombok.Getter;
import net.iso2013.mlapi.api.MultiLineAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TagsPlugin extends JavaPlugin {

    @Getter private static TagsPlugin instance;

    @Getter private MultiLineAPI multiLineAPI;
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
    
        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        
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
        multiLineAPI = (MultiLineAPI) Bukkit.getPluginManager().getPlugin("MultiLineAPI");
        
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
