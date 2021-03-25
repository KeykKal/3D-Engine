package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Entity;
import entity.Light;
import entity.Player;
import fontRendering.TextMaster;
import models.RawModel;
import models.TexturedModel;
import particles.ParticleMaster;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import skybox.SkyboxRenderer;
import terrain.Terrain;
import textures.ModelTexture;

public class SkyboxTestingMain {
	
	private static final float SIZE = 6f;
	
	public static final float[] VERTICES = {        
		    -SIZE,  SIZE, -SIZE,
		    -SIZE, -SIZE, -SIZE,
		    SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		    -SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE
		};
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		
		List<Light> lights = new ArrayList<Light>();
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		
		lights.add(new Light(new Vector3f(0,10000,100), new Vector3f(.4f,.4f,.4f)));
		
		RawModel playerModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel playerTextureModel = new TexturedModel(playerModel, 
				new ModelTexture(loader.loadTexture("IPuked T")));
		
		Player player = new Player(playerTextureModel, new Vector3f(100f, 5f, 150f),
				0, 180, 0, 1);
		
		Camera camera = new Camera(player);
		
		
		TexturedModel model = new TexturedModel(OBJLoader.loadObjModel("dragon", loader), new ModelTexture(loader.loadTexture("blank")));
		entities.add(new Entity(model,new Vector3f(0,0,0),0,0,0,1));
		
		while(!Display.isCloseRequested()) {
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, 1));
			camera.move();
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
}
