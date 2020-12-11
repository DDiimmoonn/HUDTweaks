package com.github.burgerguy.hudtweaks.gui;

import net.minecraft.client.MinecraftClient;

public interface RelativeElementSupplier {
	public String getIdentifier();
	public int getPosition(MinecraftClient client);
	public int getDimension(MinecraftClient client);	
}
