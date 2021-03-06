package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entity.Camera;
import renderEngine.DisplayManager;
import toolBox.Physic;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLenght;
	private float rotation;
	private float scale;
	
	private ParticleTexture texture;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	
	private float elapsedTime = 0;
	private float distance;
	
	private Vector3f reusableChange = new Vector3f();
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLenght, float rotation,
			float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLenght = lifeLenght;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}
	
	
	public float getDistance() {
		return distance;
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}


	public Vector2f getTexOffset2() {
		return texOffset2;
	}


	public float getBlend() {
		return blend;
	}


	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	protected boolean update(Camera camera) {
		velocity.y += Physic.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		reusableChange.set(velocity);
		updateTextureCoordInfo();
		reusableChange.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(reusableChange, position, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLenght;
	}
	
	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime/lifeLenght;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1; 
		this.blend = atlasProgression%1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
		
	}
	
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float)column / texture.getNumberOfRows();
		offset.y = (float)row / texture.getNumberOfRows();
	}
	
}
