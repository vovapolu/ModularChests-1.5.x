package vovapolu.modularchests.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vovapolu.modularchests.ModularChestTileEntity;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BreakableUpgradeItem extends ModularChestUpgradeItem {

	public BreakableUpgradeItem(int id, String name, String aIconName,
			String aTextureName) {
		super(id, name, aIconName, aTextureName);
	}

	@Override
	public void onUseItem(ModularChestTileEntity tileEnity,
			EntityPlayer player, World world, int side) {
		tileEnity.isBreakable = true;
		tileEnity.isLocked = true;
	}

	@Override
	public void onRemoveItem(ModularChestTileEntity tileEnity,
			EntityPlayer player, World world, int side) {
		tileEnity.isBreakable = false;
		tileEnity.isLocked = false;
	}

	@Override
	public boolean applyItemToStorage(ModularChestUpgradesStorage storage, EntityPlayer player, int side) {
		return storage.addGlobalItem(this, player);
	}

	@Override
	public boolean isValidSide(int side) {
		return true;
	}

	@Override
	public boolean isGlobalItem() {
		return true;
	}

	@Override
	public boolean isUniqueItem() {
		return true;
	}

}
