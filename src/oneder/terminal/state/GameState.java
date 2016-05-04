package oneder.terminal.state;

import java.awt.Graphics;

import oneder.terminal.engine.GameStateManager;
import oneder.terminal.engine.GraphicsManager;
import oneder.terminal.engine.InputManager;
import oneder.terminal.engine.MapManager;
import oneder.terminal.map.TileMap;
import oneder.terminal.system.View;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected GraphicsManager gm;
	
	protected View view;
	
	protected TileMap map;
	protected String mapFile;
	
	protected int id;
	
	public GameState(GameStateManager gsm, GraphicsManager gm, int id){
		this.gsm = gsm;
		this.gm = gm;
		this.id = id;
	}
	
	public GameState(GameStateManager gsm, GraphicsManager gm, int id, String mapFile){
		this.gsm = gsm;
		this.gm = gm;
		this.id = id;
		this.mapFile = mapFile;
	}
	
	
	// Loads the Game State map
	public boolean LoadMap(MapManager mm){
		System.out.println("\n\tLoading map...");
		boolean result = false;
		
		if(mm.LoadMapFile(mapFile)){
			map = mm.ReadMapFile(gm);
			result = true;
		}
		else{
			map = mm.CreateBlankMap();
			result = false;
		}
		
		return result;
	}
	
	// Abstract Methods
	public abstract void Input(InputManager im);
	public abstract void Update();
	public abstract void Draw(Graphics g);
	
	// Set Data
	public void SetID(int id){
		this.id = id;
	}
	public void SetMap(TileMap map){
		this.map = map;
	}
	public void SetMapFile(String mapFile){
		this.mapFile = mapFile;
	}
	public void SetView(View view){
		this.view = view;
	}
	
	// Get Data
	public int GetID(){
		return this.id;
	}
	public String GetMapFile(){
		return this.mapFile;
	}
	public TileMap GetMap(){
		return this.map;
	}
	public View GetView(){
		return this.view;
	}
	
}
