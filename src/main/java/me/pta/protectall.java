package me.pta;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author UROR
 */
public final class protectall extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        this.getServer().getPluginManager().registerEvents(new ProtectionListener(config), this);
        this.getLogger().info("PTA has been enabled. Comprehensive world protection is active!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("PTA has been disabled!");
    }
}
