package oneder.terminal.engine;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import oneder.terminal.system.Sprite;
import oneder.terminal.system.Texture;

public class GraphicsManager {
	
	// Holds all Graphic arrays
	private ArrayList<Texture[]> graphics = new ArrayList<Texture[]>();
	
	// Holds each category of graphic
	private Texture[] entity;
	private Texture[] object;
	private Texture[] tileset;
	private Texture[] ui;
	
	private int[] tileAmounts;
	
	// Graphic directory paths
	private String entityPath = "res/graphic/entity/";
	private String objectPath = "res/graphic/object/";
	private String tilesetPath = "res/graphic/tileset/";
	private String uiPath = "res/graphic/ui/";
	
	// Graphic amount in each directory
	private int entityAmount = 0, objectAmount = 0, tilesetAmount = 0, uiAmount = 0, tileAmount = 0;
	private boolean tilesLoaded = false;
	
	// Directory Information
	private String[] paths = {entityPath, objectPath, tilesetPath, uiPath};
	private int[] amounts = {entityAmount, objectAmount, tilesetAmount, uiAmount};
	
	public GraphicsManager(){
		System.out.println("\n\tGraphics Manager created.");
		
		System.out.println("\t\tLoading graphics...");
		for(int i = 0; i < paths.length; i++){
			switch(i){
				case 0:
					entity = LoadGraphics(paths[i], i);
					entityAmount = amounts[i];
					graphics.add(entity);
					break;
				case 1:
					object = LoadGraphics(paths[i], i);
					objectAmount = amounts[i];
					graphics.add(object);
					break;
				case 2:
					tileset = LoadGraphics(paths[i], i);
					tilesetAmount = amounts[i];
					graphics.add(tileset);
					break;
				case 3:
					ui = LoadGraphics(paths[i], i);
					uiAmount = amounts[i];
					graphics.add(ui);
					break;
			}
			
			System.out.println("\t\t\t" + paths[i] + ": " + graphics.get(i).length);
		}
		
		System.out.println("\t\tDone.");
	}
	
	public Texture[] LoadGraphics(String path, int index){
		// Array of all files in the directory (determined by 'path')
		File[] dir = new File(path).listFiles();
		
		int dirAmount = dir.length;
		amounts[index] = dirAmount;
		int finalAmount = amounts[index];
		
		for(int i = 0; i < amounts[index]; i++){
			try{
				BufferedImage tempImage = ImageIO.read(new File(path + dir[i].getName()));
			}catch(Exception e){
				finalAmount--;
				continue;
			}
		}
		
		amounts[index] = finalAmount;
		Texture[] loadedGraphics = new Texture[amounts[index]];
		int currentIndex = 0;
		
		for(int i = 0; i < dirAmount; i++){
			Texture temp = new Texture();
			
			try{
				BufferedImage tempImage = ImageIO.read(new File(path + dir[i].getName()));
				temp.SetTexture(tempImage);
				
				loadedGraphics[currentIndex] = temp;
				
				currentIndex++;
			}catch(IOException e){
				System.out.println("\t\t\tFailed to load '" + dir[i].getName() + "' from: " + path);
				continue;
			}
		}
		
		return loadedGraphics;
	}
	
	public void LoadTiles(int tileSize){
		System.out.println("\n\t\tLoading tiles...");
		tileAmounts = new int[tileset.length];
		
		for(int i = 0; i < tileAmounts.length; i++){
			Texture temp = tileset[i];
			
			int height = temp.GetTexture().getHeight() / tileSize;
			tileAmounts[i] = (temp.GetTexture().getWidth() / tileSize) * height;
			tileAmount += tileAmounts[i];
			
			System.out.println("\t\t\tTileset " + i + ": " + tileAmounts[i] + " tiles");
		}

		tilesLoaded = true;
		System.out.println("\t\tTotal tiles: " + tileAmount);
	}
	
	public Sprite SetTileSprite(int tileSize, int tileID){
		int currentMax = 0;
		int previousMax = 0;
		
		Texture requiredSet = null;
		
		for(int i = 0; i < tileAmounts.length; i++){
			if(i > 0)
				previousMax = currentMax;
			currentMax += tileAmounts[i];
			
			if(tileID <= currentMax){
				requiredSet = tileset[i];
				break;
			}
		}
		
		int row = requiredSet.GetTexture().getHeight() / tileSize;
		int col = requiredSet.GetTexture().getWidth() / tileSize;
		
		int tileNumber = (tileID - previousMax);
		int tileIndex = 1;
		
		for(int r = 0; r < row; r++){
			for(int c = 0; c < col; c++){
				if(tileNumber == tileIndex){
					Sprite tileSprite = new Sprite(requiredSet);
					
					tileSprite.SetSubImageDimensions(new Rectangle(c * tileSize, r * tileSize, tileSize, tileSize));
					tileSprite.SetSpriteTexture(tileSprite.GetSubImageDimensions());
					tileSprite.SetSpriteID(tileIndex);
					
					return tileSprite;
				}
				
				tileIndex++;
			}
		}
		
		return null;
	}
	
	// Grab Textures
	public Texture GetEntity(int index){
		return index < entity.length ? entity[index] : null;
	}
	public Texture GetObject(int index){
		return index < object.length ? object[index] : null;
	}
	public Texture GetTileset(int index){
		return index < tileset.length ? tileset[index] : null;
	}
	public Texture[] GetTilesets(){
		return tileset;
	}
	public Texture GetUI(int index){
		return index < ui.length ? ui[index] : null;
	}
	
	// Get Data
	public int GetEntityAmount(){
		return entityAmount;
	}
	public int GetObjectAmount(){
		return objectAmount;
	}
	public int GetTilesetAmount(){
		return tilesetAmount;
	}
	public int GetUIAmount(){
		return uiAmount;
	}
	public int GetTileAmount(){
		return tileAmount;
	}
	public boolean TilesLoaded(){
		return tilesLoaded;
	}
	
}
