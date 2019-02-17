package moe.kira.autoclose;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.google.common.collect.Lists;

public class AutoCloses implements Listener {
    private static final AutoCloses singleton = new AutoCloses();
    
    public static final AutoCloses getInstance() {
        return singleton;
    }
    
    private final Collection<Map<Player, ?>> autoCloseableInstanceKeyedMaps = Lists.newArrayList();
    private final Collection<Map<String, ?>> autoCloseableNameKeyedMaps = Lists.newArrayList();
    private final Collection<Map<UUID, ?>> autoCloseableUniqueIdKeyedMaps = Lists.newArrayList();
    
    public static <V> Map<Player, V> asInstance(Map<Player, V> instanceKeyedMap) {
        getInstance().autoCloseableInstanceKeyedMaps.add(instanceKeyedMap);
        return instanceKeyedMap;
    }
    
    public static <V> Map<String, V> asName(Map<String, V> nameKeyedMap) {
        getInstance().autoCloseableNameKeyedMaps.add(nameKeyedMap);
        return nameKeyedMap;
    }
    
    public static <V> Map<UUID, V> asUniqueId(Map<UUID, V> uniqueIdKeyedMap) {
        getInstance().autoCloseableUniqueIdKeyedMaps.add(uniqueIdKeyedMap);
        return uniqueIdKeyedMap;
    }
    
    public static <V> Set<Player> asInstance(Set<Player> instanceKeyedSet) {
        getInstance().autoCloseableInstanceKeyedSets.add(instanceKeyedSet);
        return instanceKeyedSet;
    }
    
    public static <V> Set<String> asName(Set<String> nameKeyedSet) {
        getInstance().autoCloseableNameKeyedSets.add(nameKeyedSet);
        return nameKeyedSet;
    }
    
    public static <V> Set<UUID> asUniqueId(Set<UUID> uniqueIdKeyedSet) {
        getInstance().autoCloseableUniqueIdKeyedSets.add(uniqueIdKeyedSet);
        return uniqueIdKeyedSet;
    }
    
    private final Collection<Set<Player>> autoCloseableInstanceKeyedSets = Lists.newArrayList();
    private final Collection<Set<String>> autoCloseableNameKeyedSets = Lists.newArrayList();
    private final Collection<Set<UUID>> autoCloseableUniqueIdKeyedSets = Lists.newArrayList();
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player playerInstance = event.getPlayer();
        autoCloseableInstanceKeyedMaps.forEach(c -> c.remove(playerInstance));
        autoCloseableInstanceKeyedSets.forEach(c -> c.remove(playerInstance));
        
        String playerName = playerInstance.getName();
        autoCloseableNameKeyedMaps.forEach(c -> c.remove(playerName));
        autoCloseableNameKeyedSets.forEach(c -> c.remove(playerName));
        
        UUID uniqueId = playerInstance.getUniqueId();
        autoCloseableUniqueIdKeyedMaps.forEach(c -> c.remove(uniqueId));
        autoCloseableUniqueIdKeyedSets.forEach(c -> c.remove(uniqueId));
    }
}
