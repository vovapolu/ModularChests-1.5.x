package vovapolu.modularchests;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import scala.util.control.Exception;
import vovapolu.modularchests.items.ModularChestUpgradesStorage;

public class ModularChestTextureMaker {
	private BufferedImage mainTexture;
	private BufferedImage sides[] = new BufferedImage[6];
	private ArrayList<BufferedImage> globalTextures = new ArrayList<BufferedImage>();
	
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
				BufferedImage image = ImageIO.read(new File(
						"mods/ModularChests/textures/model/" + name));
				return images.put(name, image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
		
	public void loadTextures()
	{
		for (int side = 0; side < 6; side++)
			if (storage.getSideItem(side) != null)
				sides[side] = loadImageByName(storage.getSideItem(side).getTextureName());
			else 
				sides[side] = null;
		
		globalTextures.clear();
		for (int i = 0; i < storage.getGlobalItemCount(); i++)
		{
			globalTextures.add(loadImageByName(storage.getGlobalItem(i).getTextureName()));
		}
	}
	
	public ModularChestTextureMaker(ModularChestUpgradesStorage aStorage) {
		storage = aStorage;
		loadTextures();
		mainTexture = loadImageByName("stoneChest.png");		
	}
	
	void drawCentered(Graphics2D g, int x, int y, int width, int height, BufferedImage img)
	{
		if (img == null)
			return;
		int posX = (width - img.getWidth()) / 2 + x;
		int posY = (height - img.getHeight()) / 2 + y;
		g.drawImage(img, posX, posY, null);
	}
	
	public BufferedImage getTexture()
	{
		loadTextures();		 		
		BufferedImage res = null;
		
		//FIXME copy of buffered image
		try {
			res = ImageIO.read(new File("mods/ModularChests/textures/model/stoneChest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//mainTexture.getSubimage(0, 0, mainTexture.getWidth(), mainTexture.getHeight());
		
		
		Graphics2D g = res.createGraphics();
		
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
		
		return res;
	}
}
