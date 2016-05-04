package oneder.terminal.system;

import java.awt.image.BufferedImage;

public class Texture {
	
	private BufferedImage image;
	
	public void SetTexture(BufferedImage image){
		this.image = image;
	}
	
	public BufferedImage GetTexture(){
		return this.image;
	}
	
}
