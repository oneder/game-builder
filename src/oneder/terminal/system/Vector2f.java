package oneder.terminal.system;

/*
 *	This class will be used to hold a 2D vector of float values
 *		- Used specifically for position, velocity etc.
 */

public class Vector2f {
	
	private float x;
	private float y;
	
	public Vector2f(){
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void AddVector(Vector2f other){
		this.x += other.GetX();
		this.y += other.GetY();
	}
	public void AddComponents(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	// Set Data
	public void SetX(float x){
		this.x = x;
	}
	public void SetY(float y){
		this.y = y;
	}
	
	// Get Data
	public float GetX(){
		return this.x;
	}
	public float GetY(){
		return this.y;
	}

}
