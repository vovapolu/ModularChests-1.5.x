package vovapolu.modularchests;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "ModularChests", name = "Modular Chests", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModularChests {

	@Instance("ModularChests")
	public static ModularChests instance;

	@SidedProxy(clientSide = "vovapolu.modularchests.client.ClientProxy", serverSide = "vovapolu.modularchests.CommonProxy")
	public static CommonProxy proxy;

	public static Block modularChestBlock = new ModularChestBaseBlock(500,
			Material.ground);
	public static Item addItem = new ModularChestAddItem(5001);

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Init
	public void load(FMLInitializationEvent event) {
		GameRegistry.registerBlock(modularChestBlock, "ModularChest");
		GameRegistry.registerItem(addItem, "AddItem");
		LanguageRegistry.addName(modularChestBlock, "Modular Chest");
		LanguageRegistry.addName(addItem, "Add Item");
		GameRegistry.registerTileEntity(ModularChestTileEntityBase.class, "ModularChestTileEntity");
		ClientRegistry.bindTileEntitySpecialRenderer(
				ModularChestTileEntityBase.class, new ModularChestRenderer());
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		proxy.registerRenderInformation();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
}