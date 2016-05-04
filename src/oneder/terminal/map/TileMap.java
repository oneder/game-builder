package oneder.terminal.map;

import java.awt.Graphics;

import oneder.terminal.engine.GraphicsManager;
import oneder.terminal.system.Vector2i;

public class TileMap {
	
	private String name;
	
	private Vector2i mapDimensions;
	private int tileSize;
	private int layers;
	
	public Tile[][][] tiles;
	
	public TileMap(){
		name = "Untitled";
		mapDimensions = new Vector2i();
		tileSize = 0;
		layers = 0;
	}
	
	public void InitiateMap(){
		tiles = new Tile[mapDimensions.GetX()][mapDimensions.GetY()][layers];
	}
	
	public void LoadLayer(GraphicsManager gm, String[] data, int layer){
		if(layer > layers){
			System.out.println("\t\tInvaild Layer Amount '" + layer + "' - '" + name + "' only has " + layers + " layers.");
			return;
		}
		
		int dataIndex = 0;
		for(int r = 0; r < mapDimensions.GetY(); r++){
			for(int c = 0; c < mapDimensions.GetX(); c++){
				if(dataIndex > data.length)
					break;
				
				int nextID = Integer.parseInt(data[dataIndex]);
				if(nextID > 0){
					tiles[c][r][layer] = new Tile(tileSize, layer, nextID);
					tiles[c][r][layer].SetSprite(gm.SetTileSprite(tileSize, nextID));
					tiles[c][r][layer].SetPosition(new Vector2i(c * tileSize, r * tileSize));
				}
				
				dataIndex++;
			}
		}
	}
	
	public void Draw(Graphics g){
		for(int layer = 0; layer < layers; layer++){
			for(int row = 0; row < mapDimensions.GetY(); row++){
				for(int col = 0; col < mapDimensions.GetX(); col++){
					if(tiles[col][row][layer] != null)
						tiles[col][row][layer].Draw(g);
				}
			}
		}
	}
	
	// Set Data
	public void SetName(String name){
		this.name = name;
	}
	public void SetDimensions(Vector2i mapDimensions){
		this.mapDimensions = mapDimensions;
	}
	public void SetTileSize(int tileSize){
		this.tileSize = tileSize;
	}
	public void SetLayerAmount(int layers){
		this.layers = layers;
	}
	public void SetTileMap(Tile[][][] map){
		tiles = map;
	}
	
	// Get Data
	public String GetName(){
		return this.name;
	}
	public Vector2i GetDimensions(){
		return this.mapDimensions;
	}
	public int GetTileSize(){
		return this.tileSize;
	}
	public int GetLayerAmount(){
		return this.layers;
	}
	public Tile[][][] GetTileMap(){
		return tiles;
	}
	

}
