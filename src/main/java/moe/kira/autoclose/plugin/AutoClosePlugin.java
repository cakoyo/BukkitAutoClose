package moe.kira.autoclose.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import moe.kira.autoclose.AutoCloses;

public class AutoClosePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(AutoCloses.getInstance(), this);
    }
}
