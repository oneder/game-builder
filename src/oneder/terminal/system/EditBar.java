package oneder.terminal.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import oneder.terminal.engine.GraphicsManager;
import oneder.terminal.engine.InputManager;
import oneder.terminal.engine.MapManager;
import oneder.terminal.main.TMain;
import oneder.terminal.map.Tile;
import oneder.terminal.map.TileMap;
import oneder.terminal.state.GameState;

public class EditBar {
	
	private GraphicsManager gm;
	private MapManager mm;
	private GameState currentState;
	private View view;
	
	// UI
	private Sprite loadMap;
	private Sprite newMap;
	private Sprite save;
	private Sprite upLayer, downLayer;
	private Sprite viewTiles;
	private Sprite visible;
	
	private Sprite selectedTile = null;
	private Sprite[] tiles;
	private Tile[][][] map;
	
	private Rectangle tileDisplay;
	
	private int mouseX, mouseY, tileSize, currentLayer;
	
	private boolean[] layerVisibility;

	private boolean showGrid = true;
	private boolean showTiles = false;
	private boolean changed = false;
	
	public EditBar(GraphicsManager gm, MapManager mm, GameState currentState){
		this.gm = gm;
		this.mm = mm;
		this.currentState = currentState;
		
		Setup();
	}
	
	public void Setup(){
		tileSize = currentState.GetMap().GetTileSize();
		currentLayer = 0;
		
		// Initialize visibility array
		layerVisibility = new boolean[currentState.GetMap().GetLayerAmount()];
		for(int i = 0; i < layerVisibility.length; i++){
			layerVisibility[i] = true;
		}
		
		// Set the current view
		view = new View(currentState.GetView().GetPosition().GetX(), currentState.GetView().GetPosition().GetY(), 16);
		
		// Load Current Map
		map = new Tile[currentState.GetMap().GetDimensions().GetX()][currentState.GetMap().GetDimensions().GetY()][currentState.GetMap().GetLayerAmount()];
		for(int layer = 0; layer < currentState.GetMap().GetLayerAmount(); layer++){
			for(int r = 0; r < currentState.GetMap().GetDimensions().GetY(); r++){
				for(int c = 0; c < currentState.GetMap().GetDimensions().GetX(); c++){
					map[c][r][layer] = currentState.GetMap().GetTileMap()[c][r][layer];
				}
			}
		}
		
		// Load UI
		loadMap = new Sprite(gm.GetUI(0));
		loadMap.SetSubImageDimensions(0, 0, tileSize, tileSize);
		loadMap.SetSpriteTexture(loadMap.GetSubImageDimensions());
		loadMap.SetPosition(new Vector2i((int)((view.GetPosition().GetX() * -1) + 88), (int)((view.GetPosition().GetY() * -1) + 8)));
		
		newMap = new Sprite(gm.GetUI(1));
		newMap.SetSubImageDimensions(0, 0, tileSize, tileSize);
		newMap.SetSpriteTexture(newMap.GetSubImageDimensions());
		newMap.SetPosition(new Vector2i((int)((view.GetPosition().GetX() * -1) + 48), (int)((view.GetPosition().GetY() * -1) + 8)));
		
		save = new Sprite(gm.GetUI(2));
		save.SetSubImageDimensions(0, 0, tileSize, tileSize);
		save.SetSpriteTexture(save.GetSubImageDimensions());
		save.SetPosition(new Vector2i((int)((view.GetPosition().GetX() * -1) + 128), (int)((view.GetPosition().GetY() * -1) + 8)));
		
		upLayer = new Sprite(gm.GetUI(3));
		upLayer.SetSubImageDimensions(0, 0, tileSize, tileSize);
		upLayer.SetSpriteTexture(upLayer.GetSubImageDimensions());
		upLayer.SetPosition(new Vector2i((int)((view.GetPosition().GetX() * -1) + 8), (int)((view.GetPosition().GetY() * -1) + (TMain.HEIGHT - ((tileSize * 2) + 8)))));
		
		downLayer = new Sprite(gm.GetUI(3));
		downLayer.SetSubImageDimensions(32, 0, tileSize, tileSize);
		downLayer.SetSpriteTexture(downLayer.GetSubImageDimensions());
		downLayer.SetPosition(new Vector2i((int)((view.GetPosition().GetX() * -1) + 48), (int)((view.GetPosition().GetY() * -1) + (TMain.HEIGHT - ((tileSize * 2) + 8)))));
		
		viewTiles = new Sprite(gm.GetUI(4));
		viewTiles.SetSubImageDimensions(new Rectangle(0, 0, tileSize, tileSize));
		viewTiles.SetSpriteTexture(viewTiles.GetSubImageDimensions());
		viewTiles.SetPosition(new Vector2i((int)((view.GetPosition().GetX() * -1) + 8), (int)((view.GetPosition().GetY() * -1) + 8)));
		
		visible = new Sprite(gm.GetUI(5));
		visible.SetSubImageDimensions(0, 0, tileSize, tileSize / 2);
		visible.SetSpriteTexture(visible.GetSubImageDimensions());
		visible.SetPosition(new Vector2i((int)((view.GetPosition().GetX() * -1) + 176), (int)((view.GetPosition().GetY() * -1) + (TMain.HEIGHT - ((tileSize * 2))))));
		
		// Load Tiles
		tiles = new Sprite[gm.GetTileAmount()];
		
		Texture[] tileset = gm.GetTilesets();
		int index = 0;
		
		for(int set = 0; set < tileset.length; set++){
			int row = tileset[set].GetTexture().getHeight() / tileSize;
			int col = tileset[set].GetTexture().getWidth() / tileSize;
			
			for(int r = 0; r < row; r++){
				for(int c = 0; c < col; c++){
					tiles[index] = new Sprite(tileset[set]);
					
					tiles[index].SetSubImageDimensions(new Rectangle(c * tileSize, r * tileSize, tileSize, tileSize));
					tiles[index].SetSpriteTexture(tiles[index].GetSubImageDimensions());
					
					tiles[index].SetSpriteID(++index);
				}
			}
		}
	}
	
	public void Input(InputManager im){
		// Set mouse coordinates
		mouseX = im.mouseX + (int)(view.GetPosition().GetX() * -1);
		mouseY = im.mouseY + (int)(view.GetPosition().GetY() * -1);
		view.SetScroll(im.mouseInside);
		
		// Mouse Input
		if(im.mouseUp[MouseEvent.BUTTON1]){
			if(showTiles){
				// Check if user selected a tile from the tile display
				for(int i = 0; i < tiles.length; i++){
					if(tiles[i].SpriteContainsMouse(mouseX, mouseY)){
						selectedTile = null;
						
						// Set the selected tile to 'selectedTile'
						Texture tempTexture = tiles[i].GetSourceImage();
						Rectangle tempRect = tiles[i].GetSubImageDimensions();
						
						selectedTile = new Sprite(tempTexture);
						selectedTile.SetSubImageDimensions(tempRect);
						selectedTile.SetSpriteTexture(selectedTile.GetSubImageDimensions());
						selectedTile.SetPosition(new Vector2i(SnapToGrid(mouseX), SnapToGrid(mouseY)));
						selectedTile.SetSpriteID(tiles[i].GetSpriteID());
						
						break;
					}
				}
				
				ToggleTiles();
			}
			else if(loadMap.SpriteContainsMouse(mouseX, mouseY)){
				boolean result = false;
				JTextField mapName = new JTextField();
				
				Object[] fields = {
					"Map Name: ", mapName
				};
				
				if(JOptionPane.showConfirmDialog(null, fields, "Load existing map", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION){
					String newMapName = mapName.getText();
					
					if(!newMapName.equals("")){
						String[] mapNames = mm.GetMapNames();
						
						for(int i = 0; i < mapNames.length; i++){
							if(newMapName.equals(mapNames[i])){
								currentState.SetMapFile(newMapName);
								result = currentState.LoadMap(mm);
								
								// Load Current Map
								map = new Tile[currentState.GetMap().GetDimensions().GetX()][currentState.GetMap().GetDimensions().GetY()][currentState.GetMap().GetLayerAmount()];
								for(int layer = 0; layer < currentState.GetMap().GetLayerAmount(); layer++){
									for(int r = 0; r < currentState.GetMap().GetDimensions().GetY(); r++){
										for(int c = 0; c < currentState.GetMap().GetDimensions().GetX(); c++){
											map[c][r][layer] = currentState.GetMap().GetTileMap()[c][r][layer];
										}
									}
								}
								
								tileSize = currentState.GetMap().GetTileSize();
								currentLayer = 0;
								
								// Re-Initialize visibility array
								layerVisibility = new boolean[currentState.GetMap().GetLayerAmount()];
								for(int layer = 0; layer < layerVisibility.length; layer++){
									layerVisibility[layer] = true;
								}
								
								view.SetPosition(0, 0);
								
								if(!showGrid)
									ToggleGrid();
								
								break;
							}
						}
					}
				}
			}
			else if(newMap.SpriteContainsMouse(mouseX, mouseY)){
				TileMap temp = mm.CreateBlankMap();
				
				if(temp != null){
					currentState.SetMap(temp);
					currentState.SetMapFile(currentState.GetMap().GetName());
					
					map = currentState.GetMap().GetTileMap();
					
					tileSize = currentState.GetMap().GetTileSize();
					currentLayer = 0;
					
					// Re-Initialize visibility array
					layerVisibility = new boolean[currentState.GetMap().GetLayerAmount()];
					for(int i = 0; i < layerVisibility.length; i++){
						layerVisibility[i] = true;
					}
					
					view.SetPosition(0, 0);
					
					if(!showGrid)
						ToggleGrid();
				}
			}
			else if(save.SpriteContainsMouse(mouseX, mouseY)){
				// User saves current map
				if(changed && selectedTile == null){
					System.out.print("\tSaving map... ");
					
					currentState.GetMap().SetTileMap(map);
					ToggleSaveState();
					
					System.out.println("Done.");
				}
			}
			else if(upLayer.SpriteContainsMouse(mouseX, mouseY)){
				// User goes up a layer
				if(currentLayer < currentState.GetMap().GetLayerAmount() - 1){
					currentLayer++;
					
					if(layerVisibility[currentLayer])
						visible.SetSubImageDimensions(0, 0, tileSize, tileSize / 2);
					else
						visible.SetSubImageDimensions(32, 0, tileSize, tileSize / 2);
					
					visible.SetSpriteTexture(visible.GetSubImageDimensions());
					System.out.println("\tCurrent Layer: " + currentLayer);
				}
			}
			else if(downLayer.SpriteContainsMouse(mouseX, mouseY)){
				// User goes down a layer
				if(currentLayer > 0){
					currentLayer--;
				
					if(layerVisibility[currentLayer])
						visible.SetSubImageDimensions(0, 0, tileSize, tileSize / 2);
					else
						visible.SetSubImageDimensions(32, 0, tileSize, tileSize / 2);
					
					visible.SetSpriteTexture(visible.GetSubImageDimensions());
					System.out.println("\tCurrent Layer: " + currentLayer);
				}
			}
			else if(viewTiles.SpriteContainsMouse(mouseX, mouseY)){
				// User clicked on the tile icon
				ToggleTiles();
			}
			else if(visible.SpriteContainsMouse(mouseX, mouseY)){
				// User clicks on layer visibility
				// Changes current layer's visibility
				ToggleVisibility();
			}
			else{
				if(selectedTile != null){
					try{
						// User placed a tile
						if(map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer] != null)
							map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer] = null;
							
						Tile temp = new Tile(tileSize, currentLayer, selectedTile.GetSpriteID());
						System.out.println("\t" + selectedTile.GetSpriteID());
						
						Texture tempTexture = selectedTile.GetSourceImage();
						Rectangle tempRect = selectedTile.GetSubImageDimensions();
						
						Sprite tempSprite = new Sprite(tempTexture);
						tempSprite.SetSubImageDimensions(tempRect);
						tempSprite.SetSpriteTexture(tempSprite.GetSubImageDimensions());
						tempSprite.SetPosition(new Vector2i(SnapToGrid(mouseX), SnapToGrid(mouseY)));
						
						temp.SetSprite(tempSprite);
						temp.SetPosition(tempSprite.GetPosition());
						
						map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer] = temp;
						
						if(!changed)
							ToggleSaveState();
					}catch(Exception e){
						System.out.println("CANNOT PLACE TILE: OUT OF BOUNDS");
					}
				}
			}
		}
		if(im.mouseUp[MouseEvent.BUTTON2]){
			try{
				// Tile Quick Select
				selectedTile = null;
				
				if(map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer] != null){
					Texture tempTexture = map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer].GetSprite().GetSourceImage();
					Rectangle tempRect = map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer].GetSprite().GetSubImageDimensions();
				
					selectedTile = new Sprite(tempTexture);
					selectedTile.SetSubImageDimensions(tempRect);
					selectedTile.SetSpriteTexture(selectedTile.GetSubImageDimensions());
					selectedTile.SetSpriteID(map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer].GetTileID());
					
					selectedTile.SetPosition(new Vector2i(SnapToGrid(mouseX), SnapToGrid(mouseY)));
				}
			}catch(Exception e){
				System.out.println("CANNOT SELECT TILE: OUT OF BOUNDS");
			}
		}
		if(im.mouseUp[MouseEvent.BUTTON3]){
			try{
				// User removes a tile
				if(selectedTile == null && map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer] != null){
					map[SnapToGrid(mouseX) / tileSize][SnapToGrid(mouseY) / tileSize][currentLayer] = null;
					
					if(!changed)
						ToggleSaveState();
				}
			}catch(Exception e){
				System.out.println("CANNOT DELETE TILE: OUT OF BOUNDS");
			}
		}
		
		// Keyboard Input
		if(im.keyUp[KeyEvent.VK_T]){
			// Toggles the tile display
			ToggleTiles();
		}
		if(im.keyUp[KeyEvent.VK_PERIOD]){
			// User goes up a layer
			if(currentLayer < currentState.GetMap().GetLayerAmount() - 1){
				currentLayer++;
				
				if(layerVisibility[currentLayer])
					visible.SetSubImageDimensions(0, 0, tileSize, tileSize / 2);
				else
					visible.SetSubImageDimensions(32, 0, tileSize, tileSize / 2);
				
				visible.SetSpriteTexture(visible.GetSubImageDimensions());
				System.out.println("\tCurrent Layer: " + currentLayer);
			}
		}
		if(im.keyUp[KeyEvent.VK_COMMA]){
			// User goes down a layer
			if(currentLayer > 0){
				currentLayer--;
				
				if(layerVisibility[currentLayer])
					visible.SetSubImageDimensions(0, 0, tileSize, tileSize / 2);
				else
					visible.SetSubImageDimensions(32, 0, tileSize, tileSize / 2);
				
				visible.SetSpriteTexture(visible.GetSubImageDimensions());
				System.out.println("\tCurrent Layer: " + currentLayer);
			}
		}
		if(im.keyUp[KeyEvent.VK_V]){
			// User clicks on layer visibility
			// Changes current layer's visibility
			ToggleVisibility();
		}
		if(im.control && im.keyUp[KeyEvent.VK_S]){
			// User saves current map
			if(changed){
				System.out.print("\tSaving map... ");
				
				currentState.GetMap().SetTileMap(map);
				ToggleSaveState();
				
				System.out.println("Done.");
			}
		}
		if(im.keyUp[KeyEvent.VK_SPACE]){
			// Drop current selection
			if(selectedTile != null)
				selectedTile = null;
		}
		if(im.keyUp[KeyEvent.VK_G]){
			// Toggle Grid
			ToggleGrid();
		}
	}
	
	public void Update(){
		view.Update();
		
		if(selectedTile != null){
			selectedTile.SetPosition(SnapToGrid(mouseX), SnapToGrid(mouseY));
		}
		
		if(!showTiles){
			// Mouse Scroll - X Axis
			if(mouseX < (view.GetPosition().GetX() * -1) + 16)
				view.SetVelocity(view.GetScrollSpeed(), view.GetVelocity().GetY());
			else if(mouseX > (view.GetPosition().GetX() * -1) + (TMain.WIDTH - 16))
				view.SetVelocity(-view.GetScrollSpeed(), view.GetVelocity().GetY());
			else
				view.SetVelocity(0, view.GetVelocity().GetY());
			
			// Mouse Scroll - Y Axis
			if(mouseY < (view.GetPosition().GetY() * -1) + 16)
				view.SetVelocity(view.GetVelocity().GetX(), view.GetScrollSpeed());
			else if(mouseY > (view.GetPosition().GetY() * -1) + (TMain.HEIGHT - 48))
				view.SetVelocity(view.GetVelocity().GetX(), -view.GetScrollSpeed());
			else
				view.SetVelocity(view.GetVelocity().GetX(), 0);
		}
		
		UpdateIcons();
	}
	
	public void UpdateIcons(){
		loadMap.SetPosition((int)((view.GetPosition().GetX() * -1) + 88), (int)((view.GetPosition().GetY() * -1) + 8));
		newMap.SetPosition((int)((view.GetPosition().GetX() * -1) + 48), (int)((view.GetPosition().GetY() * -1) + 8));
		save.SetPosition((int)((view.GetPosition().GetX() * -1) + 128), (int)((view.GetPosition().GetY() * -1) + 8));
		upLayer.SetPosition((int)((view.GetPosition().GetX() * -1) + 8), (int)((view.GetPosition().GetY() * -1) + (TMain.HEIGHT - ((tileSize * 2) + 8))));
		downLayer.SetPosition((int)((view.GetPosition().GetX() * -1) + 48), (int)((view.GetPosition().GetY() * -1) + (TMain.HEIGHT - ((tileSize * 2) + 8))));
		viewTiles.SetPosition((int)((view.GetPosition().GetX() * -1) + 8), (int)((view.GetPosition().GetY() * -1) + 8));
		visible.SetPosition((int)((view.GetPosition().GetX() * -1) + 176), (int)((view.GetPosition().GetY() * -1) + (TMain.HEIGHT - ((tileSize * 2)))));
	}
	
	public void Draw(Graphics g){
		// Draw Current Map
		for(int layer = 0; layer < currentState.GetMap().GetLayerAmount(); layer++){
			if(!layerVisibility[layer]){
				continue;
			}
		
			for(int r = 0; r < currentState.GetMap().GetDimensions().GetY(); r++){
				for(int c = 0; c < currentState.GetMap().GetDimensions().GetX(); c++){
					if(map[c][r][layer] != null)
						map[c][r][layer].Draw(g);
				}
			}
		}
		g.setColor(Color.RED);
		g.drawRect(0, 0, currentState.GetMap().GetDimensions().GetX() * tileSize, currentState.GetMap().GetDimensions().GetY() * tileSize);
		
		if(showGrid){
			int row = currentState.GetMap().GetDimensions().GetY();
			int col = currentState.GetMap().GetDimensions().GetX();
			
			g.setColor(new Color(255, 255, 255, 65));
			
			for(int r = 0; r < row; r++){
				g.drawLine(0, r * tileSize, col * tileSize, r * tileSize);
			}
			for(int c = 0; c < col; c++){
				g.drawLine(c * tileSize, 0, c * tileSize, row * tileSize);
			}
		}
		
		if(showTiles){
			g.setColor(new Color(158, 158, 158, 65));
			g.fillRect(tileDisplay.x, tileDisplay.y, tileDisplay.width, tileDisplay.height);
			g.setColor(Color.WHITE);
			g.drawRect(tileDisplay.x, tileDisplay.y, tileDisplay.width, tileDisplay.height);
			
			for(int i = 0; i < tiles.length; i++){
				g.drawImage(tiles[i].GetSpriteTexture(), tiles[i].GetPosition().GetX(), tiles[i].GetPosition().GetY(), null);
			}
		}
		
		if(selectedTile != null)
			g.drawImage(selectedTile.GetSpriteTexture(), selectedTile.GetPosition().GetX(), selectedTile.GetPosition().GetY(), null);
		
		g.drawImage(loadMap.GetSpriteTexture(), loadMap.GetPosition().GetX(), loadMap.GetPosition().GetY(), null);
		g.drawImage(newMap.GetSpriteTexture(), newMap.GetPosition().GetX(), newMap.GetPosition().GetY(), null);
		g.drawImage(save.GetSpriteTexture(), save.GetPosition().GetX(), save.GetPosition().GetY(), null);
		g.drawImage(upLayer.GetSpriteTexture(), upLayer.GetPosition().GetX(), upLayer.GetPosition().GetY(), null);
		g.drawImage(downLayer.GetSpriteTexture(), downLayer.GetPosition().GetX(), downLayer.GetPosition().GetY(), null);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString("Layer: " + currentLayer, downLayer.GetPosition().GetX() + 48, downLayer.GetPosition().GetY() + 24);
		
		g.drawImage(viewTiles.GetSpriteTexture(), viewTiles.GetPosition().GetX(), viewTiles.GetPosition().GetY(), null);
		g.drawImage(visible.GetSpriteTexture(), visible.GetPosition().GetX(), visible.GetPosition().GetY(), null);
	}
	
	// Snap to Grid
	public int SnapToGrid(int position){
		double currentPosition = Math.floor(position / tileSize);
		
		int fixed = (int)currentPosition * tileSize;
		
		return fixed;
	}
	
	// Manage Tile display
	public void CanViewTiles(boolean showTiles){
		this.showTiles = showTiles;
	}
	public boolean CanViewTiles(){
		return showTiles;
	}
	public void ToggleTiles(){
		if(showTiles){
			showTiles = false;
			viewTiles.SetSubImageDimensions(0, 0, tileSize, tileSize);
		}
		else{
			showTiles = true;
			SetTileDisplay();
			viewTiles.SetSubImageDimensions(32, 0, tileSize, tileSize);
		}
		
		viewTiles.SetSpriteTexture(viewTiles.GetSubImageDimensions());
	}
	public void SetTileDisplay(){
		// Set Tile display Rectangle
		tileDisplay = new Rectangle(viewTiles.GetPosition().GetX() + 56, viewTiles.GetPosition().GetY() + 56, TMain.WIDTH - 144, TMain.HEIGHT - 144);
		System.out.println(tiles.length);
		// Sets Tile Position within the display
		int row = (tileDisplay.height - 48) / tileSize;
		int col = (tileDisplay.width - 48) / tileSize;
		int tileIndex = 0;
		
		for(int r = 0; r < row; r++){
			for(int c = 0; c < col; c++){
				if(tileIndex < tiles.length)
					tiles[tileIndex].SetPosition(new Vector2i((tileDisplay.x + 24) + (c * tileSize), (tileDisplay.y + 24) + (r * tileSize)));
				else
					break;
				
				tileIndex++;
			}
		}
	}
	
	// Toggle Save State
	public void ToggleSaveState(){
		if(changed){
			changed = false;
			mm.SaveMapFile(currentState.GetMap());
			save.SetSubImageDimensions(0, 0, tileSize, tileSize);
		}
		else{
			changed = true;
			save.SetSubImageDimensions(32, 0, tileSize, tileSize);
		}
		
		save.SetSpriteTexture(save.GetSubImageDimensions());
	}
	
	// Toggle Current Layer's Visibility
	public void ToggleVisibility(){
		if(layerVisibility[currentLayer]){
			layerVisibility[currentLayer] = false;
			visible.SetSubImageDimensions(32, 0, tileSize, tileSize / 2);
		}
		else{
			layerVisibility[currentLayer] = true;
			visible.SetSubImageDimensions(0, 0, tileSize, tileSize / 2);
		}
		
		visible.SetSpriteTexture(visible.GetSubImageDimensions());
	}
	
	// Toggle Grid
	public void ToggleGrid(){
		if(showGrid)
			showGrid = false;
		else
			showGrid = true;
	}
	
	// Get Current View
	public View GetView(){
		return view;
	}
}
