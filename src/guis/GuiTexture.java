package guis;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class GuiTexture {
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
	public Vector2f getScreenPos() {
		return new Vector2f(getPosition().x * DisplayManager.WIDTH, getPosition().y * DisplayManager.HEIGHT);
	}
	
	public Vector2f getScreenSize() {
		return new Vector2f(getScale().x * DisplayManager.WIDTH, getScale().y * DisplayManager.HEIGHT);
	}
	
	public float getX() {
		return ((int) ((getScreenSize().x/2) * position.x + (position.x + (getScreenSize().x/2)))) - (getScreenSize().x/2);
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
}
