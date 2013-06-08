package vovapolu.modularchests;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class ModularChestTileEntityBase extends TileEntity implements IInventory {

	private ArrayList<ItemStack> inv;
	private byte facing;

	public ModularChestTileEntityBase(int size) {
		inv = new ArrayList<ItemStack>();
		for (int i = 0; i < size; i++)	
			inv.add(null);
	}
	
	public ModularChestTileEntityBase()
	{
		this(1);
	}

	@Override
	public int getSizeInventory() {
		return inv.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {		
		return inv.get(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv.set(slot, stack);
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
				&& player.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory");
		NBTTagCompound tagSize = (NBTTagCompound) tagList.tagAt(0);
		int newSize = tagSize.getByte("Size");
		System.out.println("wNBT" + newSize);
		inv = new ArrayList<ItemStack>(newSize);
		for (int i = 0; i < newSize; i++)
			inv.add(null);
		for (int i = 1; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.size()) {
				inv.set(slot, ItemStack.loadItemStackFromNBT(tag));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();
		NBTTagCompound tagSize = new NBTTagCompound();
		tagSize.setByte("Size", (byte)inv.size());
		itemList.appendTag(tagSize);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.get(i);
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}

	@Override
	public String getInvName() {
		return "vovapolu.modularchestinventory";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	public void setFacing(byte chestFacing) {
		facing = chestFacing;
	}
	
	public byte getFacing()	{
		return facing;
	}
	
	public void addSlot() {
		System.out.println("tile entity size:" + getSizeInventory());
		inv.add(null);
		System.out.println("new tile entity size:" + getSizeInventory());
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		this.writeToNBT(var1);
		return new Packet132TileEntityData(this.xCoord, this.yCoord,
				this.zCoord, 2, var1);
	}
	        
	@Override
	public void onDataPacket(INetworkManager netManager, Packet132TileEntityData packet)
	{
		readFromNBT(packet.customParam1);
	}

}