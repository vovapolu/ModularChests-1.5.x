package vovapolu.modularchests;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ModularChestAddItem extends Item{

	public ModularChestAddItem(int id) {
		super(id);
		setMaxStackSize(16);
		setUnlocalizedName("AddItem");
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("ModularChests:addItem");
    }
	
	@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) return false;
        System.out.println("Item used");
        TileEntity te = world.getBlockTileEntity(X, Y, Z);
        ModularChestTileEntityBase newChest;
        if (te != null && te instanceof ModularChestTileEntityBase)
        {
            ModularChestTileEntityBase modularChest = (ModularChestTileEntityBase) te;
            modularChest.addSlot();                            
            world.setBlockTileEntity(X, Y, Z, modularChest);
            PacketDispatcher.sendPacketToPlayer(modularChest.getDescriptionPacket(), (Player)player);
            stack.stackSize--;            
        }
        else
        {
            return false;
        }
        //world.markBlockForUpdate(X, Y, Z);                
        return true;
    }
	
}
