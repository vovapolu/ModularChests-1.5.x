package vovapolu.modularchests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class ModularChestTileEntityBase extends TileEntity implements
		IInventory {

	private ArrayList<ItemStack> inv;
	private byte facing;
	private int numUsingPlayers;
	private int ticksSinceSync;
	public float lidAngle;
	public float prevLidAngle;

	public ModularChestTileEntityBase(int size) {
		inv = new ArrayList<ItemStack>();
		for (int i = 0; i < size; i++)
			inv.add(null);
	}

	public ModularChestTileEntityBase() {
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
		onInventoryChanged();
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
			onInventoryChanged();
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
		System.out.println("Chest opened");
		if (numUsingPlayers < 0) {
			numUsingPlayers = 0;
		}

		++numUsingPlayers;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID,
				1, numUsingPlayers);
	}

	@Override
	public void closeChest() {
		--numUsingPlayers;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID,
				1, numUsingPlayers);
	}

	@Override
	public boolean receiveClientEvent(int id, int val) {
		if (id == 1) {
			numUsingPlayers = val;
			return true;
		} else {
			return super.receiveClientEvent(id, val);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		int newSize = tagCompound.getInteger("Size");
		facing = tagCompound.getByte("Facing");
		NBTTagList tagList = tagCompound.getTagList("Inventory");
		inv = new ArrayList<ItemStack>(newSize);
		for (int i = 0; i < newSize; i++)
			inv.add(null);
		for (int i = 0; i < tagList.tagCount(); i++) {
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

		tagCompound.setInteger("Size", inv.size());
		tagCompound.setByte("Facing", facing);
		NBTTagList itemList = new NBTTagList();
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

	public byte getFacing() {
		return facing;
	}

	public void addSlot() {
		System.out.println("tile entity size:" + getSizeInventory());
		inv.add(null);
		System.out.println("new tile entity size:" + getSizeInventory());
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		writeToNBT(data);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 2, data);
	}

	@Override
	public void onDataPacket(INetworkManager netManager,
			Packet132TileEntityData packet) {
		readFromNBT(packet.customParam1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		++ticksSinceSync;
		float padding;

		if (!worldObj.isRemote && numUsingPlayers != 0
				&& (ticksSinceSync + xCoord + yCoord + zCoord) % 200 == 0) {
			numUsingPlayers = 0;
			padding = 5.0F;
			List list = worldObj.getEntitiesWithinAABB(
					EntityPlayer.class,
					AxisAlignedBB.getAABBPool().getAABB(
							(double) ((float) xCoord - padding),
							(double) ((float) yCoord - padding),
							(double) ((float) zCoord - padding),
							(double) ((float) (xCoord + 1) + padding),
							(double) ((float) (yCoord + 1) + padding),
							(double) ((float) (zCoord + 1) + padding)));
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityPlayer entityplayer = (EntityPlayer) iterator.next();

				if (entityplayer.openContainer instanceof ModularChestContainerBase)
					++numUsingPlayers;
			}
		}

		prevLidAngle = lidAngle;
		padding = 0.1F;
		double d0;			

		if (numUsingPlayers > 0 && lidAngle == 0.0F) {
			worldObj.playSoundEffect((double) xCoord + 0.5,
					(double) yCoord + 0.5D, (double) zCoord + 0.5D,
					"random.chestopen", 0.5F,
					worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
			float copyLidAngle = lidAngle;

			if (numUsingPlayers > 0) {
				lidAngle += padding;
			} else {
				lidAngle -= padding;
			}

			if (lidAngle > 1.0F) {
				lidAngle = 1.0F;
			}

			float f2 = 0.5F;

			if (lidAngle < 0.5F && copyLidAngle >= 0.5F) {
				worldObj.playSoundEffect((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D,
						"random.chestclosed", 0.5F,
						worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (lidAngle < 0.0F) {
				lidAngle = 0.0F;
			}
		}
	}

}