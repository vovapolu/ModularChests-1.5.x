package vovapolu.modularchests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ModularChestContainerBase extends Container {

	protected ModularChestTileEntityBase tileEntity;

	public ModularChestContainerBase(InventoryPlayer inventoryPlayer, ModularChestTileEntityBase te) {

		tileEntity = te;
		int chestSize = tileEntity.getSizeInventory();
		for (int row = 0; row < (chestSize + 8) / 9; row++)
			for (int column = 0; column < Math.min(chestSize - row * 9, 9); column++)
				addSlotToContainer(new Slot(tileEntity, row * 9 + column, 8 + column * 18, 18 + row * 18));											
		int chestSlotsHeight = ((chestSize + 8) / 9) * 18 + 18;
		
		bindPlayerInventory(inventoryPlayer, chestSlotsHeight + 14);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int beginHeight) {
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				addSlotToContainer(new Slot(inventoryPlayer, column + row * 9 + 9, 8 + column * 18, 
						beginHeight + row * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 58 + beginHeight));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if (slot < tileEntity.getSizeInventory()) {
				if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 
						tileEntity.getSizeInventory() + 36, true)) {
					return null;
				}
			}
			else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) {
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
		return stack;
	}
}