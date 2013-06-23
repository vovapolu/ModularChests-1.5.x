package vovapolu.modularchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ScrollContainer extends Container {

	protected ModularChestTileEntityBase tileEntity;
	private InventoryPlayer inventoryPlayer;
	public static final int slotsWidth = 9;
	public static final int slotsHeight = 5;
	private int prevBeginSlot = -1;
	private int prevSlotsCount = -1;
	private int prevValidSlotsCount = -1;

	public ScrollContainer(InventoryPlayer aInventoryPlayer,
			ModularChestTileEntityBase te) {
		inventoryPlayer = aInventoryPlayer;
		tileEntity = te;
		te.openChest();

		for (int row = 0; row < slotsHeight; row++)
			for (int column = 0; column < slotsWidth; column++)
				addSlotToContainer(new ScrollChestSlot(tileEntity, row * slotsWidth
						+ column, 9 + 18 * column, 18 + 18 * row));

		for (int column = 0; column < slotsWidth; column++)
			addSlotToContainer(new ScrollChestSlot(inventoryPlayer, column,
					9 + 18 * column, 170));

		for (int row = 0; row < 3; row++)
			for (int column = 0; column < slotsWidth; column++)
				addSlotToContainer(new ScrollChestSlot(inventoryPlayer, 9 + row
						* slotsWidth + column, 9 + 18 * column, 112 + 18 * row));
		scrollTo(0.0F);				
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		super.onCraftGuiClosed(entityplayer);
		tileEntity.closeChest();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if (slot < tileEntity.getSizeInventory()) {
				if (!this.mergeItemStack(stackInSlot,
						tileEntity.getSizeInventory(),
						tileEntity.getSizeInventory() + 36, true)) {
					return null;
				}
			} else if (!tileEntity.mergeItemStack(stackInSlot)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		//updateSlots();
		return stack;
	}

	public boolean scrollTo(float val) {
		int slotsCount = tileEntity.getRealSizeInventory();
		int scrollSlotsHeight = (slotsCount + slotsWidth - 1) / slotsWidth
				- slotsHeight;
		int beginslot = (int) (scrollSlotsHeight * val) * slotsWidth;
		if (beginslot < 0)
			beginslot = 0;
		if (beginslot == prevBeginSlot)
			return false;
		prevBeginSlot = beginslot;
		tileEntity.shiftItems(beginslot);
		hideSlots();
		return true;
	}

	public void updateSlots() {
		for (int i = 0; i < slotsWidth * slotsHeight; i++) {
			Slot slot = (Slot) inventorySlots.get(i);
			slot.putStack(tileEntity.getStackInSlot(i));
			slot.onSlotChanged();			
		}
	}
	
	public void hideSlots()
	{
		for (int i = 0; i < slotsHeight * slotsWidth; i++)
			((ScrollChestSlot)inventorySlots.get(i)).setActive(tileEntity.isValidSlot(i));
	}
	
	@Override
	public void detectAndSendChanges() {
		if (tileEntity.getRealSizeInventory() != prevSlotsCount)
		{
			scrollTo(0.0F);
			prevSlotsCount = tileEntity.getRealSizeInventory();
		}
		
		if (tileEntity.getValidSlots() != prevValidSlotsCount)
		{
			hideSlots();
			prevValidSlotsCount = tileEntity.getValidSlots();
		}
		super.detectAndSendChanges();
	}
}
