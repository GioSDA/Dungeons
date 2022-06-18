package io.github.giosda.dungeons.listeners;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import io.github.giosda.dungeons.Dungeons;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class ChunkLoadListener implements Listener {

	Random r = new Random();

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		if (!e.isNewChunk()) return;
		if (!e.getWorld().getName().endsWith("world")) return;

		double random = r.nextDouble();
		if (random < Dungeons.percentage / 100d) {
			Location l = new Location(e.getWorld(), e.getChunk().getX()*16 + r.nextInt(16), r.nextInt(Dungeons.max - Dungeons.min) + Dungeons.min, e.getChunk().getZ()*16 + r.nextInt(16));
			loadSchematic(l);
		}
	}

	private void loadSchematic(Location location) {
		File path = new File(Dungeons.schematicPaths.get(r.nextInt(Dungeons.schematicPaths.size())));
		EditSession session = Dungeons.worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);

		try {
			ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(path));

			Clipboard clipboard = reader.read(((World) new BukkitWorld(location.getWorld())).getWorldData());
			ClipboardHolder holder = new ClipboardHolder(clipboard, ((World) new BukkitWorld(location.getWorld())).getWorldData());

			Operation operation = holder
					.createPaste(session, ((World) new BukkitWorld(location.getWorld())).getWorldData())
					.to(new Vector(location.getX(), location.getY(), location.getZ()))
					.ignoreAirBlocks(false)
					.build();

			Operations.completeLegacy(operation);

		} catch (MaxChangedBlocksException  | IOException e) {
			e.printStackTrace();
		}
	}

}
