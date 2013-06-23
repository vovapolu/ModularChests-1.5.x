package vovapolu.modularchests.client.gui;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.network.PacketDispatcher;
import vovapolu.modularchests.ModularChestTileEntityBase;
import vovapolu.modularchests.ModularChests;
import vovapolu.modularchests.PacketHandler;
import vovapolu.modularchests.ScrollContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiScrollChest extends GuiContainer {
	
	private float scrollVal = 0.0F;
	private int xGui;
	private int yGui;
	private final int barX = 175, barY = 18, barWidth = 12, barHeight = 85;
	private final int pWidth = 12, pHeight = 15;
	
	ModularChestTileEntityBase tileEntity;
	private boolean wasClicking;
	private boolean isScrolling;
	private int ticksAfterUpdate = 0;
	
	public GuiScrollChest(InventoryPlayer inventory,
			ModularChestTileEntityBase te) {
		super(new ScrollContainer(inventory, te));	
		tileEntity = te;
	}

	@Override
	public void initGui() {
		xSize = 195;
		ySize = 194;
		xGui = (width - xSize) / 2;
		yGui = (height - ySize) / 2;
		super.initGui();
	}
	
	private boolean isOverScrollBar(int mouseX, int mouseY)
	{
		return mouseX >= xGui + barX && mouseX <= xGui + barX + barWidth 
				&& mouseY >= yGui + barY && mouseY <= yGui +barY + barHeight;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float particleTick) {
		boolean isClick = Mouse.isButtonDown(0);
        int xGui = this.guiLeft;
        int yGui = this.guiTop;
        int realBarX = xGui + barX;
        int realBarY = yGui + barY;

        if (!this.wasClicking && isClick && isOverScrollBar(mouseX, mouseY))
        {
            this.isScrolling = true;
        }

        if (!isClick)
        {
            this.isScrolling = false;
        }

        this.wasClicking = isClick;

        if (this.isScrolling)
        {
            this.scrollVal = ((float)(mouseY - realBarY) - (float)pHeight / 2) / 
            		((float)(barHeight) - (float)pHeight);

            if (this.scrollVal < 0.0F)
            {
                this.scrollVal = 0.0F;
            }

            if (this.scrollVal > 1.0F)
            {
                this.scrollVal = 1.0F;                
            }

            if (ticksAfterUpdate > ModularChests.scrollChestOptimalTicks && ((ScrollContainer)this.inventorySlots).scrollTo(this.scrollVal))
            {
            	ticksAfterUpdate = 0;           	
            	PacketDispatcher.sendPacketToServer(PacketHandler.createSBPacket(tileEntity));
            }
            ticksAfterUpdate++;
        }

        super.drawScreen(mouseX, mouseY, particleTick);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float particleTick, int mouseX, int mouseY) {
		this.mc.renderEngine.bindTexture("/mods/ModularChests/textures/gui/scroll_container.png");
		
		this.drawTexturedModalRect(xGui, yGui, 0, 0, xSize, ySize);
		this.drawTexturedModalRect(xGui + barX, yGui + barY + 
				(int)((float)(barHeight - pHeight) * this.scrollVal), 
				195, 0, pWidth, pHeight);

		for (int row = 0; row < ScrollContainer.slotsHeight; row++)
			for (int column = 0; column < ScrollContainer.slotsWidth; column++)
				if (!tileEntity.isValidSlot(row * ScrollContainer.slotsWidth + column))	
					this.drawTexturedModalRect(xGui - 1 + 9 + 18 * column, yGui - 1 + 18 + 18 * row, 207, 0, 18, 18);
	}
}
