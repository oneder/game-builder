package oneder.terminal.system;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Sprite {
	
	private Texture sourceImage;
	private BufferedImage image;
	
	private Rectangle subImage;
	
	private Vector2i position;
	
	private int spriteID;
	
	public Sprite(Texture sourceImage){
		this.sourceImage = sourceImage;
		
		subImage = new Rectangle(0, 0, this.sourceImage.GetTexture().getWidth(), this.sourceImage.GetTexture().getHeight());
	}
	
	public boolean SpriteContainsMouse(int mouseX, int mouseY){
		if(mouseX >= position.GetX() && mouseX <= position.GetX() + subImage.getWidth() &&
		   mouseY >= position.GetY() && mouseY <= position.GetY() + subImage.getHeight())
			return true;
		
		return false;
	}
	
	// Set Data
	public void SetSubImageDimensions(Rectangle subImage){
		this.subImage = subImage;
	}
	public void SetSubImageDimensions(int x, int y, int w, int h){
		subImage = new Rectangle(x, y, w, h);
	}
	public void SetSpriteTexture(Rectangle subImage){
		this.image = sourceImage.GetTexture().getSubimage(subImage.x, subImage.y, subImage.width, subImage.height);
	}
	public void SetPosition(Vector2i position){
		this.position = position;
	}
	public void SetPosition(int x, int y){
		this.position.SetX(x);
		this.position.SetY(y);
	}
	public void SetSpriteID(int spriteID){
		this.spriteID = spriteID;
	}
	
	// Get Data
	public BufferedImage GetSpriteTexture(){
		return image;
	}
	public Vector2i GetPosition(){
		return this.position != null ? this.position : null;
	}
	public Rectangle GetSubImageDimensions(){
		return subImage;
	}
	public Texture GetSourceImage(){
		return sourceImage;
	}
	public int GetSpriteID(){
		return spriteID;
	}

}
