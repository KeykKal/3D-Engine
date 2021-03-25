package entity;


import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

import models.TexturedModel;
import renderEngine.DisplayManager;
import toolBox.Maths;


public class Camera {

	private static final float SPEED = 100;

	private float currentSpeed = 0;

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	
	private Vector3f position = new Vector3f(50,50,0);
	private float pitch = 0;
	private float yaw = 0;
	private float roll;
	
	public boolean useCameraControlle = false;
	
	Player player;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public Camera() {
		
	}
	
	public void move(float delta, Entity entity) {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= 0.02f * delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += 0.02f * delta;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.02f * delta;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 0.02f * delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y -= 0.02f * delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			position.y += 0.02f * delta;
		}
		
		position.z -= (Mouse.getDWheel() * .06f); 
		
		
		if(Mouse.isButtonDown(0)) {
			entity.increaseRotation(0, (Mouse.getDX() * .06f * delta), 0);
			entity.increaseRotation(-(Mouse.getDY() * .06f * delta), 0, 0);
			//entity.setRotY();
		}
		
		if(Mouse.isButtonDown(2)) {
			//yaw += (Mouse.getDX() * .06f);
			pitch -= (Mouse.getDY() * .06f);
		}
		
	}

	float uX = 0;
	int uY;
	private boolean OKeyUp;
	public void move(float delta) {		
		checkInputs();
		//increaseRotation(0, 0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds());
		//float right = (float) (Math.sin(Math.toRadians(yaw - 3.141f/2.0f)));
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(yaw)));
		float dz = (float) (distance * Math.cos(Math.toRadians(yaw)));
		float dy = (float) (distance * Math.sin(Math.toRadians(pitch)));
		float constDistance= SPEED * DisplayManager.getFrameTimeSeconds();
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			dx += (float) (constDistance*Math.sin(Math.toRadians(yaw-90)));
			dz += (float) (constDistance*Math.cos(Math.toRadians(yaw-90)));
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			dx += (float) (constDistance*Math.sin(Math.toRadians(yaw+90)));
			dz += (float) (constDistance*Math.cos(Math.toRadians(yaw+90)));
		}
		increasePosition(dx, -dy, -dz);
	}
	
	
	//"Second person" Camera is still W.I.P
	public void secondPersonMove() {
		//calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraRotation(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getPosition().x + angleAroundPlayer);
		//this.pitch = 95 - ((player.getPosition().z + player.getPosition().y)+ angleAroundPlayer);
		
		
		
		//this.pitch = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	//third person Camera
	public void move() {
		checkInputs();
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(yaw)));
		float dz = (float) (distance * Math.cos(Math.toRadians(yaw)));
		float dy = (float) (distance * Math.sin(Math.toRadians(pitch)));
		float constDistance= SPEED * DisplayManager.getFrameTimeSeconds();
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			dx += (float) (Math.sin(Math.toRadians(yaw-90)));
			dz += (float) (Math.cos(Math.toRadians(yaw-90)));
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			dx += (float) (constDistance*Math.sin(Math.toRadians(yaw+90)));
			dz += (float) (constDistance*Math.cos(Math.toRadians(yaw+90)));
		}
		increasePosition(dx, -dy, -dz);
	}
	
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.roll += dx;
		this.pitch += dy;
		this.yaw += dz;
	}
	
	private void checkInputs() {
		
		if(useCameraControlle) {
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				this.currentSpeed = SPEED;
			} 
			else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				this.currentSpeed = -SPEED;
			} else {
				this.currentSpeed = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				position.y -= 1;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
				position.y += 1;
			}
			
			if(Mouse.isButtonDown(0)) {
				yaw += Mouse.getDX()*0.06f;
				pitch -= Mouse.getDY()*0.06f;
			}

		} else {
			calculatePitch();
			calculateAngleAroundPlayer();
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			calculateCameraPosition(horizontalDistance, verticalDistance);
			this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		}
		
		calculateZoom();
		
		
		if(Mouse.isButtonDown(0)) {
			//yaw += (Mouse.getDX() * .06f);
			//pitch -= (Mouse.getDY() * .06f);
		}
		
		//toggle the Camera controle
		if(Keyboard.isKeyDown(Keyboard.KEY_O)) {
		    if(OKeyUp==true) {
			      OKeyUp = false;
			      useCameraControlle = !useCameraControlle;
			}
		    
		} else if (OKeyUp == false && !Keyboard.isKeyDown(Keyboard.KEY_O))
	        OKeyUp = true;	
		
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ; 
		position.y = player.getPosition().y + verticDistance;
	}
	
	//for the second Camera
	private void calculateCameraRotation(float horizDistance, float verticDistance) {
		this.pitch = (position.y - player.getPosition().y) -(player.getPosition().z + player.getPosition().y);
	}
	
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(0)) {
			float pitchChanged = Mouse.getDY() * 0.1f;
			pitch -= pitchChanged;
		}	
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChanged = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChanged;
		}
	}
	
}
