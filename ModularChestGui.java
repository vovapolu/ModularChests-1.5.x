package vovapolu.modularchests;

import java.awt.image.BufferedImage;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class ModularChestGui extends GuiContainer {	
	private ModularChestTileEntityBase tileEntity;

	public ModularChestGui(InventoryPlayer inventoryPlayer, ModularChestTileEntityBase te) {
		super(new ModularChestContainerBase(inventoryPlayer, te));
		tileEntity = te;
		int slotsCount = tileEntity.getSizeInventory();
		xSize = ModularChestGuiMaker.getGuiWidth(slotsCount);
		ySize = ModularChestGuiMaker.getGuiHeight(slotsCount);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		ModularChestGuiMaker.drawText(tileEntity.getSizeInventory(), fontRenderer);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		int slotsCount = tileEntity.getSizeInventory();				
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		BufferedImage texture = ModularChestGuiMaker.makeGui(slotsCount);
		this.mc.renderEngine.resetBoundTexture();
		int id = this.mc.renderEngine.allocateAndSetupTexture(texture);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}