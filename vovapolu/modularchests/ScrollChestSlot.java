package vovapolu.modularchests;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ScrollChestSlot extends Slot {

	boolean isActive;
	
	public ScrollChestSlot(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);	
		isActive = true;
	}
	
	public ScrollChestSlot(IInventory inventory, int id, int x, int y, boolean active) {
		super(inventory, id, x, y);	
		isActive = active;
	}

	public void setActive(boolean newVal)
	{
		isActive = newVal;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) { 
		return isActive && super.isItemValid(itemStack);
	}
}
