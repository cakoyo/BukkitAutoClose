package moe.kira.autoclose;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.concurrent.NotThreadSafe;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Lists;

import moe.kira.autoclose.misc.CollectionWithPlayerConsumer;
import moe.kira.autoclose.misc.PlayerConsumer;

@NotThreadSafe
public class AutoCloses implements Listener {
    private static final AutoCloses singleton = new AutoCloses();
    
    public static final AutoCloses getInstance() {
        return singleton;
    }
    
    private boolean hasRegistered;
    
    public synchronized AutoCloses ensuresRegistry(Plugin plugin) {
        if (!hasRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
            hasRegistered = true;
        }
        return this;
    }
    
    private final Collection<CollectionWithPlayerConsumer<Map<Player, ?>>> autoCloseableInstanceKeyedMaps = Lists.newArrayList();
    private final Collection<CollectionWithPlayerConsumer<Map<String, ?>>> autoCloseableNameKeyedMaps = Lists.newArrayList();
    private final Collection<CollectionWithPlayerConsumer<Map<UUID, ?>>> autoCloseableUniqueIdKeyedMaps = Lists.newArrayList();
    
    public static <V> Map<Player, V> asInstance(Map<Player, V> instanceKeyedMap, PlayerConsumer... playerConsumer) {
        getInstance().autoCloseableInstanceKeyedMaps.add(CollectionWithPlayerConsumer.of(instanceKeyedMap, playerConsumer));
        return instanceKeyedMap;
    }
    
    public static <V> Map<String, V> asName(Map<String, V> nameKeyedMap, PlayerConsumer... playerConsumer) {
        getInstance().autoCloseableNameKeyedMaps.add(CollectionWithPlayerConsumer.of(nameKeyedMap, playerConsumer));
        return nameKeyedMap;
    }
    
    public static <V> Map<UUID, V> asUniqueId(Map<UUID, V> uniqueIdKeyedMap, PlayerConsumer... playerConsumer) {
        getInstance().autoCloseableUniqueIdKeyedMaps.add(CollectionWithPlayerConsumer.of(uniqueIdKeyedMap, playerConsumer));
        return uniqueIdKeyedMap;
    }
    
    private final Collection<CollectionWithPlayerConsumer<Set<Player>>> autoCloseableInstanceKeyedSets = Lists.newArrayList();
    private final Collection<CollectionWithPlayerConsumer<Set<String>>> autoCloseableNameKeyedSets = Lists.newArrayList();
    private final Collection<CollectionWithPlayerConsumer<Set<UUID>>> autoCloseableUniqueIdKeyedSets = Lists.newArrayList();
    
    public static <V> Set<Player> asInstance(Set<Player> instanceKeyedSet, PlayerConsumer... playerConsumer) {
        getInstance().autoCloseableInstanceKeyedSets.add(CollectionWithPlayerConsumer.of(instanceKeyedSet, playerConsumer));
        return instanceKeyedSet;
    }
    
    public static <V> Set<String> asName(Set<String> nameKeyedSet, PlayerConsumer... playerConsumer) {
        getInstance().autoCloseableNameKeyedSets.add(CollectionWithPlayerConsumer.of(nameKeyedSet, playerConsumer));
        return nameKeyedSet;
    }
    
    public static <V> Set<UUID> asUniqueId(Set<UUID> uniqueIdKeyedSet, PlayerConsumer... playerConsumer) {
        getInstance().autoCloseableUniqueIdKeyedSets.add(CollectionWithPlayerConsumer.of(uniqueIdKeyedSet, playerConsumer));
        return uniqueIdKeyedSet;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player playerInstance = event.getPlayer();
        autoCloseableInstanceKeyedMaps.forEach(c -> c.collection().remove(playerInstance));
        autoCloseableInstanceKeyedSets.forEach(c -> c.collection().remove(playerInstance));
        
        String playerName = playerInstance.getName();
        autoCloseableNameKeyedMaps.forEach(c -> c.collection().remove(playerName));
        autoCloseableNameKeyedSets.forEach(c -> c.collection().remove(playerName));
        
        UUID uniqueId = playerInstance.getUniqueId();
        autoCloseableUniqueIdKeyedMaps.forEach(c -> c.collection().remove(uniqueId));
        autoCloseableUniqueIdKeyedSets.forEach(c -> c.collection().remove(uniqueId));
    }
}
