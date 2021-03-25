package entity;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class EntityTypes {

	
	
	
	public class CustomLamp {
		TexturedModel texturedModel;
		Vector3f position = new Vector3f(0,0,0);;
		float rotX = 0;
		float rotY = 0;
		float rotZ = 0;
		float scale = 1;
		
		float h = 3;
		Vector3f color = new Vector3f(0,0,0);
		Vector3f attenuantion = new Vector3f(0,0,0);
		
		public CustomLamp(TexturedModel texturedModel, Vector3f position, float scale, 
				float lightHeight, Vector3f color, Vector3f attenuantion) {
			this.texturedModel = texturedModel;
			this.position = position;
			this.scale = scale;
			
			this.h = lightHeight;
			this.color = color;
			this.attenuantion = attenuantion;
		}
		
		public CustomLamp() {
			
		}
		
		

		public Entity getEntity() {
			return new Entity(texturedModel, position, rotX, rotY, rotZ, scale);
		}

		public Light getLight() {
			return new Light(new Vector3f(this.position.x, this.position.y + h, this.position.z),
					new Vector3f(0,0,10), new Vector3f(1,0.01f,0.002f));
		}
	}
	
	public class Lamp {
		
		Loader loader;
		
		
		Vector3f position = new Vector3f(0,0,0);
		float rotX = 0;
		float rotY = 0;
		float rotZ = 0;
		float scale = 1;
		
		float h = 3;
		Vector3f color = new Vector3f(0,0,0);
		Vector3f attenuantion = new Vector3f(0,0,0);
		Light light;
		Entity entity;
		public Lamp(Loader loader, Vector3f position, float scale, 
				float lightHeight, Vector3f color, Vector3f attenuantion) {
			this.loader = loader;
			this.position = position;
			this.scale = scale;
			
			this.h = lightHeight;
			this.color = color;
			this.attenuantion = attenuantion;
			
			TexturedModel texturedModel = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
					new ModelTexture(loader.loadTexture("lamp")));
			entity = new Entity(texturedModel, position, rotX, rotY, rotZ, scale);
			
			light = new Light(new Vector3f(this.getEntity().getPosition().x,
					this.getEntity().getPosition().y + h,
					this.getEntity().getPosition().z),
					color, attenuantion);
		}

		//with rotations
		public Lamp(Loader loader, Vector3f position,float rotX, float rotY, float rotZ, float scale, 
				float lightHeight, Vector3f color, Vector3f attenuantion) {
			this.loader = loader;
			this.position = position;
			this.rotX = rotX;
			this.rotY = rotY;
			this.rotZ = rotZ;
			this.scale = scale;
			
			this.h = lightHeight;
			this.color = color;
			this.attenuantion = attenuantion;
			
			
			TexturedModel texturedModel = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
					new ModelTexture(loader.loadTexture("lamp")));
			entity = new Entity(texturedModel, position, rotX, rotY, rotZ, scale);
			
			light = new Light(new Vector3f(this.getEntity().getPosition().x,
					this.getEntity().getPosition().y + h,
					this.getEntity().getPosition().z),
					color, attenuantion);
		}
		
		public void setPosition(Vector3f position) {
			this.position = position;
			getEntity().setPosition(position);
			getLight().setPosition(new Vector3f(this.getEntity().getPosition().x,
					this.getEntity().getPosition().y + h,
					this.getEntity().getPosition().z));
			
		}

		public Entity getEntity() {
			return entity;
		}

		public Light getLight() {
			return light;
		}
	
		
	}
	
}
