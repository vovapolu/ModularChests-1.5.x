package vovapolu.modularchests.items;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;

import vovapolu.modularchests.ModularChestTileEntity;

import cpw.mods.fml.common.registry.GameData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ModularChestUpgradesStorage {
	private HashSet<Integer> itemsId = new HashSet<Integer>();
	private ModularChestUpgradeItem upgrades[] = new ModularChestUpgradeItem[6];
	private ArrayList<ModularChestUpgradeItem> globalUpgrades = new ArrayList<ModularChestUpgradeItem>();
	private ModularChestTileEntity tileEntity;

	public ModularChestUpgradesStorage(ModularChestTileEntity aTileEntity) {
		for (int i = 0; i < 6; i++)
			upgrades[i] = null;
		tileEntity = aTileEntity;
	}

	public ModularChestUpgradesStorage(ModularChestTileEntity aTileEntity, ModularChestUpgradeItem[] newUpgrades) {
		for (int i = 0; i < 6; i++)
			if (newUpgrades.length > i)
				upgrades[i] = newUpgrades[i];
		tileEntity = aTileEntity;
	}

	public ModularChestUpgradeItem getSideItem(int side) {
		return upgrades[side];
	}

	public boolean setSideItem(int side, ModularChestUpgradeItem newItem, EntityPlayer player) {
		if (newItem.isUniqueItem() && itemsId.contains(newItem.itemID))
			return false;
		if (!newItem.isGlobalItem() && newItem.isValidSide(side))
		{
			if (upgrades[side] != null)
			{
				itemsId.remove(upgrades[side].itemID);
				upgrades[side].onRemoveItem(tileEntity, player, tileEntity.worldObj, side);
			}
			upgrades[side] = newItem;
			itemsId.add(newItem.itemID);
			upgrades[side].onUseItem(tileEntity, player, tileEntity.worldObj, side);
			return true;
		}
		else 
			return false;
	}
	
	public boolean addGlobalItem(ModularChestUpgradeItem newItem, EntityPlayer player)
	{
		if (newItem.isUniqueItem() && itemsId.contains(newItem.itemID))
			return false;
		else 
		{
			globalUpgrades.add(newItem);
			itemsId.add(newItem.itemID);
			newItem.onUseItem(tileEntity, player, tileEntity.worldObj);
			return true;
		}
	}
	
	public boolean removeGlobalItem(ModularChestUpgradeItem item, EntityPlayer player)
	{
		if (item == null)
			return true;
		itemsId.remove(item.itemID);
		int index = globalUpgrades.indexOf(item);
		if (index >= 0 && index < globalUpgrades.size())
		{
			globalUpgrades.get(index).onRemoveItem(tileEntity, player, tileEntity.worldObj);
			globalUpgrades.remove(index);
			return true;
		}
		else 
			return false;
	}
	
	public int getGlobalItemCount()
	{
		return globalUpgrades.size();
	}
	
	public ModularChestUpgradeItem getGlobalItem(int num)
	{
		return globalUpgrades.get(num);
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < 6; i++) {
			if (upgrades[i] != null) {
				int itemId = upgrades[i].itemID;
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				tag.setInteger("Id", itemId);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Upgrades", itemList);

		tagCompound.setInteger("GlobalUpgradesSize", globalUpgrades.size());

		NBTTagList globalItemList = new NBTTagList();
		for (int i = 0; i < globalUpgrades.size(); i++) {
			if (globalUpgrades.get(i) != null) {
				int itemId = globalUpgrades.get(i).itemID;
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("Id", itemId);
				globalItemList.appendTag(tag);
			}
		}
		tagCompound.setTag("GlobalUpgrades", globalItemList);
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		itemsId.clear();
		NBTTagList tagList = tagCompound.getTagList("Upgrades");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < 6) {
				int id = tag.getInteger("Id");
				upgrades[slot] = (ModularChestUpgradeItem) Item.itemsList[id];
				itemsId.add(id);
			}
		}

		int size = tagCompound.getInteger("GlobalUpgradesSize");
		globalUpgrades.clear();

		tagList = tagCompound.getTagList("GlobalUpgrades");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			
			int id = tag.getInteger("Id");
			globalUpgrades.add((ModularChestUpgradeItem) Item.itemsList[id]);
			itemsId.add(id);
		}
	}
}
