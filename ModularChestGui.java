package vovapolu.modularchests;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class ModularChestGui extends GuiContainer {

	public ModularChestGui(InventoryPlayer inventoryPlayer, ModularChestTileEntityBase tileEntity) {
		super(new ModularChestContainerBase(inventoryPlayer, tileEntity));
		xSize = 184;
		ySize = 256;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		fontRenderer.drawString("Tiny", 8, 6, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8,
				40, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/ModularChests/textures/gui/container.png");
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}