package com.github.burgerguy.hudtweaks.config;

import com.github.burgerguy.hudtweaks.hud.HudContainer;
import com.github.burgerguy.hudtweaks.util.Util;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigHelper {
	private ConfigHelper() {
		// no instantiation, all contents static
	}

	public static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("hudtweaks.json");

	/**
	 * Tries to parse the configuration file.
	 */
	public static void tryLoadConfig() {
		if (Files.exists(configFile)) {
			Util.LOGGER.info("Loading config file...");
			try (BufferedReader reader = new BufferedReader(new FileReader(configFile.toFile()))) {
				HudContainer.getElementRegistry().updateFromJson(Util.JSON_PARSER.parse(reader));
			} catch (JsonIOException e) {
				Util.LOGGER.error("Unable to read config file", e);
			} catch (JsonParseException e) {
				Util.LOGGER.error("Config file invalid", e);
			} catch (IOException e) {
				Util.LOGGER.error("Error loading config file", e);
			}
		} else {
			Util.LOGGER.info("Config file not found");
		}
	}

	/**
	 * Saves the current HUD state as a json file for later reloading.
	 * This doesn't save all the information that would be in the class
	 * tree, just the things that would make sense in a config file.
	 */
	public static void trySaveConfig() {
		Util.LOGGER.info("Saving config file...");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile.toFile()))) {
			Util.GSON.toJson(HudContainer.getElementRegistry(), writer);
		} catch (JsonIOException e) {
			Util.LOGGER.error("Unable to write to config file", e);
		} catch (IOException e) {
			Util.LOGGER.error("Error saving config file", e);
		}
	}

}
