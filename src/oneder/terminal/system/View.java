package oneder.terminal.system;

public class View {
	
	float scrollSpeed;
	
	boolean canScroll = true;
	
	private Vector2f position;
	private Vector2f velocity;
	
	public View(){
		scrollSpeed = 0;
		
		position = new Vector2f();
		velocity = new Vector2f();
	}
	public View(float x, float y, float scrollSpeed){
		this.scrollSpeed = scrollSpeed;
		
		position = new Vector2f(x, y);
		velocity = new Vector2f();
	}
	public View(Vector2f position, float scrollSpeed){
		this.scrollSpeed = scrollSpeed;
		this.position = position;
	}
	
	public void Update(){
		if(canScroll)
			position.AddVector(velocity);
	}
	
	// Set Data
	public void SetPosition(float x, float y){
		this.position.SetX(x);
		this.position.SetY(y);
	}
	public void SetPosition(Vector2f position){
		this.position = position;
	}
	public void SetVelocity(float x, float y){
		this.velocity.SetX(x);
		this.velocity.SetY(y);
	}
	public void SetVelocity(Vector2f velocity){
		this.velocity = velocity;
	}
	public void SetScrollSpeed(float scrollSpeed){
		this.scrollSpeed = scrollSpeed;
	}
	public void SetScroll(boolean canScroll){
		this.canScroll = canScroll;
	}
	
	// Get Data
	public Vector2f GetPosition(){
		return this.position;
	}
	public Vector2f GetVelocity(){
		return this.velocity;
	}
	public float GetScrollSpeed(){
		return this.scrollSpeed;
	}
	public boolean CanScroll(){
		return this.canScroll;
	}

}
