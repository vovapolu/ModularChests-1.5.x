package vovapolu.modularchests.items;

import vovapolu.modularchests.ModularChestTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class StorageAddItem extends ModularChestUpgradeItem {
	
	private int addSize;

	public StorageAddItem(int id, String name, String aIconName, String aTextureName, int newAddSize) {
		super(id, name, aIconName, aTextureName);	
		addSize = newAddSize;
	}

	@Override
	public void onUseItem(ModularChestTileEntity tileEnity,
			EntityPlayer player, World world, int side) {	
		tileEnity.addStorageSlots(addSize);
	}
	
	public boolean isValidSide(int side)
	{
		return side < 4;
	}

	@Override
	public void onRemoveItem(ModularChestTileEntity tileEnity,
			EntityPlayer player, World world, int side) {
		tileEnity.removeStorageSlots(addSize);
	}

	@Override
	public boolean applyItemToStorage(ModularChestUpgradesStorage storage, EntityPlayer player, int side) {	
		return storage.setSideItem(side, this, player);
	}

	@Override
	public boolean isGlobalItem() {
		return false;
	}

	@Override
	public boolean isUniqueItem() {
		return false;
	}
}
