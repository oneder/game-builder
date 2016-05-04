package oneder.terminal.engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import oneder.terminal.state.*;
import oneder.terminal.system.EditBar;
import oneder.terminal.system.View;

public class GameStateManager {
	
	private ArrayList<GameState> state;
	private GameState currentState;
	
	private GraphicsManager gm;
	private MapManager mm;
	
	private View currentView;
	
	private EditBar editBar;
	private boolean editMode = false;
	
	public GameStateManager(GraphicsManager gm, MapManager mm){
		System.out.println("\n\tGame State Manager created.");
		
		this.gm = gm;
		this.mm = mm;
		
		state = new ArrayList<GameState>();
	}
	
	public void LoadStates(){
		System.out.print("\t\tLoading States... ");

		state.add(new TestState(this, gm, 1, "testMap"));
		
		System.out.println("Done.");
		System.out.println("\t\tNumber of States: " + state.size());
	}
	
	public void Input(InputManager im){
		if(editMode)
			editBar.Input(im);
		else
			currentState.Input(im);
	}
	
	public void Update(){
		if(editMode)
			editBar.Update();
		else
			currentState.Update();
	}
	
	public void Draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.translate(currentView.GetPosition().GetX(), currentView.GetPosition().GetY());
		
		if(editMode)
			editBar.Draw(g);
		else
			currentState.Draw(g);
		
		g2d.translate(-currentView.GetPosition().GetX(), -currentView.GetPosition().GetY());
	}
	
	// Set Data
	public void SetCurrentState(int id){
		// TODO: Save current state
		currentState = null;
		
		for(int i = 0; i < state.size(); i++){
			GameState temp = state.get(i);
			
			if(temp.GetID() == id){
				currentState = temp;
				break;
			}
		}
		
		if(currentState != null){
			System.out.println("\t\tCurrent State: " + currentState.GetID());
			currentView = currentState.GetView();
			
			if(currentState.LoadMap(mm)){
				System.out.println("\tMap Loaded Successfully!");
			}
			else{
				System.out.println("\tMap File '" + currentState.GetMapFile() + "' does not exist. (Blank map created)");
			}
		}
	}
	public void ToggleEditMode(){
		if(editMode){
			editMode = false;
			editBar = null;
			currentView = currentState.GetView();
			System.out.println("Edit Mode Disabled.");
		}
		else{
			editMode = true;
			editBar = new EditBar(gm, mm, currentState);
			currentView = editBar.GetView();
			System.out.println("Edit Mode Enabled.");
		}
	}
	
	// Get Data
	public GameState GetCurrentState(){
		return currentState;
	}
	public boolean EditModeEnable(){
		return this.editMode;
	}
	
}
