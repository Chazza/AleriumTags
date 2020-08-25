package io.alerium.tags.api.events;

import io.alerium.tags.objects.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor @Getter
public class TagUpdateEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();

    private final Tag tag;
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
}
