package vovapolu.modularchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ScrollContainer extends Container {

	protected ModularChestTileEntity tileEntity;
	private InventoryPlayer inventoryPlayer;
	public static final int slotsWidth = 9;
	public static final int slotsHeight = 5;
	private int prevBeginSlot = -1;
	private int prevSlotsCount = -1;
	private int prevValidSlotsCount = -1;
	private int shiftVal = 0;

	public ScrollContainer(InventoryPlayer aInventoryPlayer,
			ModularChestTileEntity te) {
		inventoryPlayer = aInventoryPlayer;
		tileEntity = te;
		te.openChest();

		for (int column = 0; column < slotsWidth; column++)
			addSlotToContainer(new ScrollChestSlot(inventoryPlayer, column,
					9 + 18 * column, 170));

		for (int row = 0; row < 3; row++)
			for (int column = 0; column < slotsWidth; column++)
				addSlotToContainer(new ScrollChestSlot(inventoryPlayer, 9 + row
						* slotsWidth + column, 9 + 18 * column, 112 + 18 * row));

		for (int i = 0; i < te.getRealSizeInventory(); i++)
			addSlotToContainer(new ScrollChestSlot(tileEntity, i, -100, -100));

		scrollTo(0.0F);
	}

	void shiftSlots(int beginSlot) {
		shiftVal = beginSlot;
		beginSlot += 36;
		for (int i = 36; i < inventorySlots.size(); i++) {
			if (i >= beginSlot && i < beginSlot + slotsWidth * slotsHeight) {
				int column = (i - beginSlot) % slotsWidth;
				int row = (i - beginSlot) / slotsWidth;
				((Slot) inventorySlots.get(i)).xDisplayPosition = 9 + 18 * column;
				((Slot) inventorySlots.get(i)).yDisplayPosition = 18 + 18 * row;
			} else {
				((Slot) inventorySlots.get(i)).xDisplayPosition = -100;
				((Slot) inventorySlots.get(i)).yDisplayPosition = -100;
			}
		}
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

			if (slot > 36) {
				if (!this.mergeItemStack(stackInSlot, 0, 36, true)) {
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
		// updateSlots();
		return stack;
	}
	
	public int getShiftSlot(float shiftVal)
	{
		int slotsCount = tileEntity.getRealSizeInventory();
		int scrollSlotsHeight = (slotsCount + slotsWidth - 1) / slotsWidth
				- slotsHeight;
		return (int) (scrollSlotsHeight * shiftVal) * slotsWidth;
	}
	
	public int getShiftRow(float shiftVal)
	{
		return getShiftSlot(shiftVal) / slotsWidth;
	}	
	
	public float getShiftHeightOfRow()
	{
		int slotsCount = tileEntity.getRealSizeInventory();
		int scrollSlotsHeight = (slotsCount + slotsWidth - 1) / slotsWidth
				- slotsHeight;
		return 1.0F / scrollSlotsHeight;
	}

	public boolean scrollTo(float val) {
		int beginslot = getShiftSlot(val);
		if (beginslot < 0)
			beginslot = 0;
		if (beginslot == prevBeginSlot)
			return false;
		prevBeginSlot = beginslot;
		shiftSlots(beginslot);
		return true;
	}

	public void updateSlots() {
		for (int i = 0; i < slotsWidth * slotsHeight; i++) {
			Slot slot = (Slot) inventorySlots.get(i);
			slot.putStack(tileEntity.getStackInSlot(i));
			slot.onSlotChanged();
		}
	}

	@Override
	public void detectAndSendChanges() {
		/*if (tileEntity.getRealSizeInventory() != prevSlotsCount) {
			scrollTo(0.0F);
			prevSlotsCount = tileEntity.getRealSizeInventory();
		}*/

		super.detectAndSendChanges();
	}
	
	public boolean isValidSlot(int slot)
	{
		return shiftVal + slot < tileEntity.getRealSizeInventory();
	}
	
	public int getValidSlots()
	{
		return tileEntity.getRealSizeInventory() - shiftVal;
	}
}
