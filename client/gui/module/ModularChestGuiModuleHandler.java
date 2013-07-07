package vovapolu.modularchests.client.gui.module;

import java.util.ArrayList;

import vovapolu.modularchests.client.gui.GuiModuleTabs;
import vovapolu.modularchests.items.GUIUpgradeItem;
import vovapolu.modularchests.items.ModularChestUpgradesStorage;

public class ModularChestGuiModuleHandler {
	
	ModularChestUpgradesStorage storage;
	ArrayList<IGuiModule> modules = new ArrayList<IGuiModule>();
	GuiModuleTabs tabs = new GuiModuleTabs();
	
	public ModularChestGuiModuleHandler(ModularChestUpgradesStorage storage) { 
		this.storage = storage;
		ArrayList<GUIUpgradeItem> items = this.storage.getGuiModuleItems();
		for (GUIUpgradeItem item: items)
			modules.add(item.getNewGuiModule());
	}
	
	
	
	
}
