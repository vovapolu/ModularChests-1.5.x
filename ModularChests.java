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
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;

@Mod(modid = "ModularChests", name = "Modular Chests", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, 
	packetHandler = PacketHandler.class, channels = {"TE", "SB", "UTE"})
public class ModularChests {
	@Instance("ModularChests")
	public static ModularChests instance;

	@SidedProxy(clientSide = "vovapolu.modularchests.client.ClientProxy", serverSide = "vovapolu.modularchests.CommonProxy")
	public static CommonProxy proxy;

	public static Block modularChestBlock;
	public static Item addItem;
	
	// Cfg
	public static int modularBlockId;
	public static int addItemId;
	public static int scrollChestOptimalTicks;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(
				event.getSuggestedConfigurationFile());
		cfg.load();
		modularBlockId = cfg.getBlock("ModularChest", 500).getInt();
		addItemId = cfg.getItem("AddItem", 5001).getInt();
		Property scotProperty = cfg.get(Configuration.CATEGORY_GENERAL, "ScrollChestOptimalTicks", 3);
        scotProperty.comment = "This value doesn't affect gameplay, but it affects some performance of Scroll Chest.\n" +
        		"For example: if this value equals 0 then in Scroll Chest if you move slider too fast you will see some " +
        		"duplication of items\n" +
        		"Also this can happen at large values. The higher the value the less the risk that it will happen.";
        
        scrollChestOptimalTicks = scotProperty.getInt();
		cfg.save();
	}

	@Init
	public void load(FMLInitializationEvent event) {
		modularChestBlock = new ModularChestBaseBlock(modularBlockId,
				Material.ground);
		addItem = new ModularChestAddItem(addItemId);

		GameRegistry.registerBlock(modularChestBlock, "ModularChest");
		GameRegistry.registerItem(addItem, "AddItem");
		LanguageRegistry.addName(modularChestBlock, "Modular Chest");
		LanguageRegistry.addName(addItem, "Add Item");
		GameRegistry.registerTileEntity(ModularChestTileEntityBase.class,
				"ModularChestTileEntity");
		ClientRegistry.bindTileEntitySpecialRenderer(
				ModularChestTileEntityBase.class, new ModularChestRenderer());
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		proxy.registerRenderInformation();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
}