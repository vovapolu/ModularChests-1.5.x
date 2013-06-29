package vovapolu.modularchests.items;

import vovapolu.modularchests.ModularChests;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public enum StorageAddItemType {	
	WOOD("woodAddItem", "woodAddItem", "woodSide.png", "Wood Storage Module", 9, "www", "wcw", "www"),
	STONE("stoneAddItem", "stoneAddItem", "stoneSide.png", "Stone Storage Module", 12, "sss", "scs", "sss"),
	IRON("ironAddItem", "ironAddItem", "ironSide.png", "Iron Storage Module", 18, " i ", "ici", " i "),
	GOLD("goldAddItem", "goldAddItem", "goldSide.png", "Gold Storage Module", 36, " g ", "gcg", " g "),
	DIAMOND("diamondAddItem", "diamondAddItem", "diamondSide.png", "Diamond Storage Module", 72, "dcd");
	
	private String name;
	private String iconName;
	private String sideName;
	private int size;
	private String fullName;
	private String[] recipes;
	
	StorageAddItemType(String aName, String aIconName, String aSideName, String aFullName, int aSize, String... aRecipes)
	{
		name = aName;
		iconName = aIconName;
		size = aSize;
		fullName = aFullName;
		recipes = aRecipes;
		sideName = aSideName;
	}
	
	public void registerItem(int id)
	{
		StorageAddItem item = new StorageAddItem(id, name, iconName, sideName, size);			
		
		GameRegistry.registerItem(item, name);
		LanguageRegistry.addName(item, fullName);
		
		GameRegistry.addRecipe(new ItemStack(item), recipes,
		        'w', new ItemStack(Block.wood), 's', new ItemStack(Block.stone),
		        'i', new ItemStack(Item.ingotIron), 'g', new ItemStack(Item.ingotGold),
		        'd', new ItemStack(Item.diamond), 'c', new ItemStack(ModularChests.coreAddItem));
	}
	
	public static void registreItems()
	{
		int i = 0;
		for (StorageAddItemType type: values())
		{
			type.registerItem(ModularChests.addItemId + i);
			i++;
		}
	}
}
