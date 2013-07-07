package vovapolu.modularchests;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.model.AdvancedModelLoader;

import scala.util.control.Exception;
import vovapolu.modularchests.items.ModularChestUpgradesStorage;

public class ModularChestTextureMaker {
	private BufferedImage mainTexture;
	private BufferedImage sides[] = new BufferedImage[6];
	private ArrayList<BufferedImage> globalTextures = new ArrayList<BufferedImage>();
	private BufferedImage resImage;
	
	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	/**
	 * coordinates of first side on texture
	 */
	public static final int ySide = 33, xSide = 0;
	public static final int sideWidth = 14, sideHeight = 10;
	
	
	private ModularChestUpgradesStorage storage;
	
	private static BufferedImage loadImageByName(String name)
	{		
		if (images.containsKey(name))
		{
			return images.get(name);
		}
		else 
		{			
			try {		
				InputStream istream = ModularChestTextureMaker.class.getResourceAsStream("/mods/ModularChests/textures/model/" + name);
				BufferedImage image = ImageIO.read(istream);				
				images.put(name, image);
				return image;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
		
	public void loadTextures()
	{
		if (storage == null)
			return;
		for (int side = 0; side < 6; side++)
			if (storage.getSideItem(side) != null)
				sides[side] = loadImageByName(storage.getSideItem(side).getTextureName());
			else 
				sides[side] = null;
		
		if (globalTextures.size() != storage.getGlobalItemsCount())
			globalTextures.clear();
		for (int i = 0; i < storage.getGlobalItemsCount(); i++)
		{
			BufferedImage newImage = loadImageByName(storage.getGlobalItem(i).getTextureName());
			if (globalTextures.size() <= i)
				globalTextures.add(newImage);
			else 
				globalTextures.set(i, newImage);
		}
		
		mainTexture = loadImageByName("stoneChest.png");	
	}
	
	public ModularChestTextureMaker(ModularChestUpgradesStorage aStorage) {
		storage = aStorage;	
	}
	
	void drawCentered(Graphics2D g, int x, int y, int width, int height, BufferedImage img)
	{
		if (img == null)
			return;
		int posX = (width - img.getWidth()) / 2 + x;
		int posY = (height - img.getHeight()) / 2 + y;
		g.drawImage(img, posX, posY, null);
	}
	
	public void setStorage(ModularChestUpgradesStorage aStorage)
	{
		storage = aStorage;
	}
	
	public BufferedImage getTexture()
	{
		if (storage == null)
			return null;
		loadTextures();			
		if (resImage == null)
			resImage = new BufferedImage(mainTexture.getWidth(), mainTexture.getHeight(), mainTexture.getType());
				
		Graphics2D g = resImage.createGraphics();
		g.drawImage(mainTexture, 0, 0, null);
		
		//draw textures on sides
		int nowX = xSide, nowY = ySide;
		for (int i = 0; i < 4; i++)
			drawCentered(g, (i ^ 1) * sideWidth + nowX, nowY, 
					sideWidth, sideHeight, sides[i]); // "i ^ 1" some kind of magic, but it works		
		
		//TODO up and down textures
		
		//draw textures of global upgrades
		for (int i = 0; i < globalTextures.size(); i++)
		{
			g.drawImage(globalTextures.get(i), 0, 0, null);			
		}
		
		g.dispose();
		
		return resImage;
	}
}
