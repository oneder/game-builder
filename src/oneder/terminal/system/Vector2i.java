package oneder.terminal.system;

/*
 * 	This class will be used to hold a 2D vector of integer values
 * 		- Used specifically for size dimensions etc.
 */

public class Vector2i {
	
	private int x;
	private int y;
	
	public Vector2i(){
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2i(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void AddVector(Vector2i other){
		this.x += other.GetX();
		this.y += other.GetY();
	}
	public void AddComponents(int x, int y){
		this.x += x;
		this.y += y;
	}
	
	// Set Data
	public void SetX(int x){
		this.x = x;
	}
	public void SetY(int y){
		this.y = y;
	}
	
	// Get Data
	public int GetX(){
		return this.x;
	}
	public int GetY(){
		return this.y;
	}
	
}
