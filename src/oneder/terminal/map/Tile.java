package oneder.terminal.map;

import java.awt.Graphics;

import oneder.terminal.system.Sprite;
import oneder.terminal.system.Vector2i;

public class Tile {
	
	private Sprite tile;
	
	private int tileSize;
	private int layerID;
	private int tileID;
	
	private Vector2i position;
	
	public Tile(int tileSize, int layerID, int tileID){
		this.tileSize = tileSize;
		this.layerID = layerID;
		this.tileID = tileID;
	}
	
	public void Draw(Graphics g){
		g.drawImage(tile.GetSpriteTexture(), position.GetX(), position.GetY(), null);
	}
	
	// Set Data
	public void SetSprite(Sprite tile){
		this.tile = tile;
	}
	public void SetTileSize(int tileSize){
		this.tileSize = tileSize;
	}
	public void SetLayerID(int layerID){
		this.layerID = layerID;
	}
	public void SetTileID(int tileID){
		this.tileID = tileID;
	}
	public void SetPosition(Vector2i position){
		this.position = position;
	}
	
	// Get Data
	public Sprite GetSprite(){
		return this.tile;
	}
	public int GetTileSize(){
		return this.tileSize;
	}
	public int GetLayerID(){
		return this.layerID;
	}
	public int GetTileID(){
		return this.tileID;
	}
	public Vector2i GetPosition(){
		return this.position;
	}

}
