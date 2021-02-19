package com.github.burgerguy.hudtweaks.hud;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.github.burgerguy.hudtweaks.gui.HTOptionsScreen;
import com.github.burgerguy.hudtweaks.hud.element.HTIdentifier;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class MatrixCache {
	private final Map<HTIdentifier, Matrix4f> matrixMap = new HashMap<>();
	private final Map<HTIdentifier, Boolean> appliedMatrixMap = new HashMap<>();
	
	public Matrix4f getMatrix(HTIdentifier identifier) {
		return matrixMap.get(identifier);
	}
	
	public void putMatrix(HTIdentifier identifier, Matrix4f matrix) {
		matrixMap.put(identifier, matrix);
	}
	
	public void tryPushMatrix(HTIdentifier identifier, @Nullable MatrixStack matrixStack) {
		Matrix4f matrix = getMatrix(identifier);
		if (matrix != null) {
			appliedMatrixMap.put(identifier, true);
			if (matrixStack != null) {
				matrixStack.push();
				matrixStack.peek().getModel().multiply(matrix);
			} else {
				RenderSystem.pushMatrix();
				RenderSystem.multMatrix(matrix);
			}
			
			if (HTOptionsScreen.isOpen()) HudContainer.getElementRegistry().getElement(identifier).startDrawTest(); // we only care about visibility when HudElementWidgets have to be displayed
		}
	}
	
	public void tryPopMatrix(HTIdentifier identifier, @Nullable MatrixStack matrixStack) {
		if (appliedMatrixMap.get(identifier)) {
			if (matrixStack != null) {
				matrixStack.pop();
			} else {
				RenderSystem.popMatrix();
			}
			appliedMatrixMap.put(identifier, false);
			
			HudContainer.getElementRegistry().getElement(identifier).endDrawTest(); // this won't error out if the draw test isn't active, so it's ok
		}
	}
}
