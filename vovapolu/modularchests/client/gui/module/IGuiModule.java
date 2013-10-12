package vovapolu.modularchests.client.gui.module;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public interface IGuiModule {
	public BufferedImage getIcon();
	public BufferedImage getGuiImage();
	public void placeSlots(ArrayList<Slot> slots);
	public IInventory getInventory();
}
