package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;
import toolBox.Physic;

public class Player extends Entity{
	
	private static final float RUN_SPEED = 90;
	private static final float TURN_SPEED = 160;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);

	}

	public void Move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += Physic.GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = -3.001f;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	
	private void jump() {
		if(!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} 
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} 
		else {
			this.currentSpeed = 0;
		}
		
		boolean once = true;
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
		
			this.currentTurnSpeed = TURN_SPEED;
		}
		else {
			this.currentTurnSpeed = 0;
		}
		
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {

		}
		
		
		if(Mouse.isButtonDown(0)) {
			//yaw += (Mouse.getDX() * .06f);
			//pitch -= (Mouse.getDY() * .06f);
		}
	}
	
}
