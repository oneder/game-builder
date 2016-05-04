package oneder.terminal.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import oneder.terminal.main.GameWindow;
import oneder.terminal.main.TMain;

public class Engine implements Runnable{
	
	private GameWindow window;
	
	private InputManager im;
	private GraphicsManager gm;
	private MapManager mm;
	private GameStateManager gsm;
	
	private Thread thread;
	private boolean running = false;
	private int FPS = 0;
	
	public Engine(GameWindow window){
		this.window = window;
	}
	
	public synchronized void StartEngine(){
		System.out.println("\nCreating game...");
		
		// Set up the Input Manager
		im = new InputManager();
		window.addKeyListener(im);
		window.addMouseListener(im);
		window.addMouseMotionListener(im);
		
		// Set up the Graphics Manager
		gm = new GraphicsManager();
		
		// Set up the Map Manager
		mm = new MapManager();
		
		// Set up the Game State Manager
		gsm = new GameStateManager(gm, mm);
		gsm.LoadStates();
		gsm.SetCurrentState(1);
		
		System.out.println("\nGame created.");
		
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void StopEngine(){
		try{
			running = false;
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		window.requestFocus();
		
		long lastTime = System.nanoTime();
		
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(running){
			long currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / ns;
			lastTime = currentTime;
			
			while(delta > 0){
				Input();
				im.Clear();
				
				Update();
				delta--;
			}
			
			if(running)
				Draw();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				FPS = frames;
				frames = 0;
			}
		}
		StopEngine();
	}
	
	public void Input(){
		// Master Controls
		if(im.keyUp[KeyEvent.VK_END])
			running = false;
		if(im.control && im.shift && im.keyUp[KeyEvent.VK_E])
			gsm.ToggleEditMode();
		
		gsm.Input(im);
	}
	
	public void Update(){
		// Update Current State
		gsm.Update();
	}
	
	public void Draw(){
		BufferStrategy bs = window.getBufferStrategy();
		
		if(bs == null){
			window.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, TMain.WIDTH, TMain.HEIGHT);
		
		// Draw Current State
		gsm.Draw(g);
		
		if(gsm.EditModeEnable()){
			// Display FPS - TODO: Move to HUD class
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
			g.drawString("FPS: " + FPS, TMain.WIDTH - 72, 16);
		}
		
		g.dispose();
		bs.show();
	}
	
	public boolean IsRunning(){
		return running;
	}

}
