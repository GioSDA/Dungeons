package io.github.giosda.dungeons;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import io.github.giosda.dungeons.listeners.ChunkLoadListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public final class Dungeons extends JavaPlugin {
	public static ArrayList<File> schematicPaths;

	public static WorldEditPlugin worldEditPlugin;
	public static FileConfiguration config;
	public static Logger logger;

	public static int min;
	public static int max;
	public static double percentage;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();

		worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		config = this.getConfig();
		logger = this.getLogger();

		min = this.getConfig().getInt("min-y");
		max = this.getConfig().getInt("max-y");

		percentage = this.getConfig().getDouble("percentage-chance");

		this.getLogger().info(Color.GREEN + "Enabling dungeon spawning.");

		this.getServer().getPluginManager().registerEvents(new ChunkLoadListener(), this);
	}

	@Override
	public void onDisable() {
		this.getLogger().severe(Color.RED + "Disabling dungeon spawning.");
	}
}
