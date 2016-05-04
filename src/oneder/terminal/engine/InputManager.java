package oneder.terminal.engine;

import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

public class InputManager implements KeyListener, MouseInputListener{
	
	// Mouse Data
	public int mouseX, mouseY, mouseDownCount, mouseUpCount;
	public boolean[] mouseDown = new boolean[MouseInfo.getNumberOfButtons()];
	public boolean[] mouseUp = new boolean[MouseInfo.getNumberOfButtons()];
	public boolean mouseInside = false;
	public boolean mouseDragging = false;
	public boolean mouseMoving = false;
	
	// Keyboard Data
	public int keyDownCount, keyUpCount;
	public boolean[] keyDown = new boolean[256];
	public boolean[] keyUp = new boolean[256];
	public boolean control = false, alt = false, shift = false;
	
	// Clears input data
	public void Clear(){
		// Clear Keyboard Data
		keyDownCount = 0;
		keyUpCount = 0;
		
		for(int i = 0; i < keyUp.length; i++)
			keyUp[i] = false;
		
		// Clear Mouse Data
		mouseDownCount = 0;
		mouseUpCount = 0;
		mouseDragging = false;
		mouseMoving = false;
		
		for(int i = 0; i < mouseUp.length; i++)
			mouseUp[i] = false;
	}
	
	// Manage Mouse Input
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {
		mouseInside = true;
	}
	@Override
	public void mouseExited(MouseEvent e) {
		mouseInside = false;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown[e.getButton()] = true;
		mouseDownCount++;
		
		if(mouseUpCount > 0)
			mouseUpCount--;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseUp[e.getButton()] = true;
		mouseUpCount++;
		
		mouseDown[e.getButton()] = false;
		if(mouseDownCount > 0)
			mouseDownCount--;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDragging = true;
		mouseMoving = true;
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseDragging = false;
		mouseMoving = true;
		
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	// Manage Keyboard Input
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			control = true;
		else if(e.getKeyCode() == KeyEvent.VK_ALT)
			alt = true;
		else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			shift = true;
		
		if(!keyDown[e.getKeyCode()]){
			keyDown[e.getKeyCode()] = true;
			keyDownCount++;
			
			if(keyUpCount > 0)
				keyUpCount--;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			control = false;
		else if(e.getKeyCode() == KeyEvent.VK_ALT)
			alt = false;
		else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			shift = false;
		
		if(!keyUp[e.getKeyCode()]){
			keyUp[e.getKeyCode()] = true;
			keyUpCount++;
			
			keyDown[e.getKeyCode()] = false;
			if(keyDownCount > 0)
				keyDownCount--;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	// Manage Gamepad Input

}
