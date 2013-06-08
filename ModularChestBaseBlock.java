package vovapolu.modularchests;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ModularChestBaseBlock extends BlockContainer {
	public static final int blockId = 500;

	public ModularChestBaseBlock(int id, Material material) {
		super(id, material);
		setHardness(0.5F);
		setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
		setUnlocalizedName("modularChest");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		iconRegister.registerIcon("Test:test");
	}	

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);			
		if (tileEntity == null || player.isSneaking()) {			
			return true;
		}
		player.openGui(ModularChests.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	private void dropItems(World world, int x, int y, int z) {
		Random rand = new Random();

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z
						+ rz, new ItemStack(item.itemID, item.stackSize,
						item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound(
							(NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new ModularChestTileEntityBase(20);
	}
	
	
	@Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemStack)
    {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (facing == 0)
        {
            chestFacing = 2;
        }
        if (facing == 1)
        {
            chestFacing = 5;
        }
        if (facing == 2)
        {
            chestFacing = 3;
        }
        if (facing == 3)
        {
            chestFacing = 4;
        }
        TileEntity te = world.getBlockTileEntity(i, j, k);
        if (te != null && te instanceof ModularChestTileEntityBase)
        {
            ((ModularChestTileEntityBase) te).setFacing(chestFacing);
            world.markBlockForUpdate(i, j, k);
        }
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 22;
    }

}