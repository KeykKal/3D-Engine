package engineTester;

import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.*;

import models.TexturedModel;
import models.RawModel;
import objConverter.*;
import renderEngine.*;
import terrain.Terrain;
import textures.ModelTexture;


public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
	
		Loader loader = new Loader();

		ModelData dragonModelData = OBJFileLoader.loadOBJ("dragon");
		
		RawModel model = loader.loadToVAO(dragonModelData.getVertices(), 
				dragonModelData.getTextureCoords(), 
				dragonModelData.getNormals(), 
				dragonModelData.getIndices());
		
		RawModel wmodel = OBJLoader.loadObjModel("stall", loader);		
		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("texture")));
		ModelTexture texture = 	staticModel.getTexture();
		texture.setShineDamper(200);
		texture.setReflectivity(1f);
		
		TexturedModel staticWall = new TexturedModel(wmodel, new ModelTexture(loader.loadTexture("blank")));
		
		Entity entity = new Entity(staticModel, new Vector3f(0,-5,-20), 0, 180, 0, 1);
		Entity wallEntity = new Entity(staticWall, new Vector3f(0, -10, -60), 0, 0, 0, 10);
		Light light = new Light(new Vector3f(300,200,300), new Vector3f(1f,1f,1f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		//Terrain terrain = new Terrain(-1,0,loader,new ModelTexture(loader.loadTexture("blank")));
		
		
		Camera camera = new Camera();
				
		
		MasterRenderer renderer = new MasterRenderer(loader);
		while(!Display.isCloseRequested()) {

			
		//	entity.increaseRotation(0, .1f, 0);

			//camera.move(DisplayManager.getDelta(), entity);
			
			//renderer.proccessTerrain(terrain);
			renderer.processEntity(entity);
			
//			renderer.proccessEntity(wallEntity);
			renderer.render(lights, camera,new Vector4f(0, -1, 0, 1));
			DisplayManager.updateDisplay();
			
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	
	}

}
