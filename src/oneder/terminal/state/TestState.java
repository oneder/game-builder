package oneder.terminal.state;

import java.awt.Graphics;

import oneder.terminal.engine.GameStateManager;
import oneder.terminal.engine.GraphicsManager;
import oneder.terminal.engine.InputManager;
import oneder.terminal.system.EditBar;
import oneder.terminal.system.View;

public class TestState extends GameState{
	
	public TestState(GameStateManager gsm, GraphicsManager gm, int id, String mapFile){
		super(gsm, gm, id);
		
		this.mapFile = mapFile;
		
		// Set View
		view = new View();
	}
	
	public void Input(InputManager im){
	}
	
	public void Update(){
		view.Update();
	}
	
	public void Draw(Graphics g){
		map.Draw(g);
	}

}
