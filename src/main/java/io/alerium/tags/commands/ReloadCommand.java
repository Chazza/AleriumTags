package io.alerium.tags.commands;

import io.alerium.tags.TagsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    
    private final TagsPlugin plugin = TagsPlugin.getInstance();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tags.reload")) {
            sender.sendMessage(plugin.getMessage("noPermission"));
            return true;
        }
        
        plugin.reloadConfig();
        plugin.getTagManager().reload();
        sender.sendMessage(plugin.getMessage("reloaded"));
        return true;
    }
    
}
