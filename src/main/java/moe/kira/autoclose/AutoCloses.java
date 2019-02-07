package moe.kira.autoclose;

import java.util.Collection;
import java.util.Map;
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
    
    private final Collection<Map<Player, ?>> autoCloseableInstanceKeyedMaps = Lists.newArrayList();
    private final Collection<Map<String, ?>> autoCloseableNameKeyedMaps = Lists.newArrayList();
    private final Collection<Map<UUID, ?>> autoCloseableUniqueIdKeyedMaps = Lists.newArrayList();
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player playerInstance = event.getPlayer();
        
        for (Map<Player, ?> instanceKeyedMap : autoCloseableInstanceKeyedMaps)
            instanceKeyedMap.remove(playerInstance);
        
        String playerName = playerInstance.getName();
        for (Map<String, ?> nameKeyedMap : autoCloseableNameKeyedMaps)
            nameKeyedMap.remove(playerName);
        
        UUID uniqueId = playerInstance.getUniqueId();
        for (Map<UUID, ?> uniqueIdKeyedMap : autoCloseableUniqueIdKeyedMaps)
            uniqueIdKeyedMap.remove(uniqueId);
    }
}
