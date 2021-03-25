package guis;

import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import renderEngine.DisplayManager;

public class ButtonUi extends GuiTexture{

	private int xPos;
	private int yPos;
	
	private int scaleX;
	private int scaleY;
	
	private FontType font;
	private GUIText text;
	
	boolean down = false;
	
	///*Render a simple button without text*///
	public ButtonUi(int texture, Vector2f position, Vector2f scale) {
		super(texture, position, scale);
		calculateValues(position, scale);
	}
	
	///*Render a Button with Simple Text*///
	public ButtonUi(int texture, Vector2f position, Vector2f scale, FontType font, String text) {
		super(texture, position, scale);
		calculateValues(position, scale);
		//Text
		this.text = new GUIText(text, 1, font, new Vector2f((.5f), (position.y*2)), scale.x, false);
		this.text.setColour(1, 0, 0);
	}
	
	///*Returns true if the Button is pressed (just once)*///
	public boolean isButtonDown(int button) {
		if(Mouse.isButtonDown(button) && !down){
			down = true;
			if(Mouse.getX() > xPos &&
					Mouse.getX() < xPos + scaleX) {
				if(Mouse.getY() > yPos &&
						Mouse.getY() < yPos + scaleY) {
					return true;		
				}
			}
		}
		down = Mouse.isButtonDown(0);
		return false;
	}
	
	///*Returns true as long the button is pressed*///
	public boolean asLongButtonDown(int button) {
		if(Mouse.isButtonDown(button)){
			if(Mouse.getX() > xPos &&
					Mouse.getX() < xPos + scaleX) {
				if(Mouse.getY() > yPos &&
						Mouse.getY() < yPos + scaleY) {
					return true;		
				}
			}
		}
		return false;
	}
	
	public void setScale(Vector2f scale) {
		super.setScale(scale);
		calculateValues(getPosition(), scale);
	}

	
	private void calculateValues(Vector2f position, Vector2f scale) {
		//Scale
		scaleX = (int) (scale.x * DisplayManager.WIDTH);
		scaleY = (int) (scale.y * DisplayManager.HEIGHT);
		//Position
		xPos = ((int) ((DisplayManager.WIDTH/2) * position.x + (position.x + (DisplayManager.WIDTH/2)))) - (scaleX/2);
		yPos = ((int) ((DisplayManager.HEIGHT/2) * position.y + (position.y + (DisplayManager.HEIGHT/2)))) - (scaleY/2);
	}
}
