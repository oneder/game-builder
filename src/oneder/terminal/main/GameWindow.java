package oneder.terminal.main;

import java.awt.Canvas;

import oneder.terminal.engine.Engine;

public class GameWindow extends Canvas{

	private static final long serialVersionUID = -8743187702556060493L;
	
	private static Engine engine;
	
	public GameWindow(){
		engine = new Engine(this);
	}
	
	public static void Start(){
		engine.StartEngine();
	}
	
	public static void Stop(){
		engine.StopEngine();
	}

}
