package moe.kira.autoclose.misc;

import java.util.function.Consumer;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerConsumer extends Consumer<Player> {
}