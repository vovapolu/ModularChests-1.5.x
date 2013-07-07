package vovapolu.modularchests.client.gui.module;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class CraftModule implements IGuiModule {
	
	public CraftModule() {
	}

	@Override
	public BufferedImage getIcon() {
		return null;
	}

	@Override
	public BufferedImage getGuiImage() {
		return null;
	}

	@Override
	public void placeSlots(ArrayList<Slot> slots) {
	}

	@Override
	public IInventory getInventory() {
		return null;
	}
}
