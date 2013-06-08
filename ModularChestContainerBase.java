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
		
		Slot[] slots = ModularChestGuiMaker.addSlots(te.getSizeInventory(), tileEntity, inventoryPlayer);
		for(Slot slot : slots)
			addSlotToContainer(slot);
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
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