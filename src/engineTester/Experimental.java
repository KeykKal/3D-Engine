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
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import terrain.TerrainTexture;
import terrain.TerrainTexturePack;
import textures.ModelTexture;

public class Experimental {
	static Loader loader = new Loader();
	static MasterRenderer renderer;
	static Camera cam = new Camera();
	static Player player;
	static List<Terrain> terrains = new ArrayList<Terrain>();
	static List<Light> lights = new ArrayList<Light>();
	static List<Entity> entities = new ArrayList<Entity>();
	static List<Entity> normalMapEntities = new ArrayList<Entity>();
	static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		loadScene();
	}
	
	private static void loadScene() {
		renderer = new MasterRenderer(loader);
		player = new Player(new TexturedModel(OBJLoader.loadObjModel("dragon", loader), 
				new ModelTexture(loader.loadTexture("barrel"))),
				new Vector3f(), 0, 0, 0, 0);
		
		cam = new Camera(player);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(new TerrainTexture(loader.loadTexture("grass")),
				new TerrainTexture(loader.loadTexture("grassy")), new TerrainTexture(loader.loadTexture("grassy2")), 
				new TerrainTexture(loader.loadTexture("path")));
		terrains.add(new Terrain(0, 0, loader, texturePack , blendMap, true));
		lights.add(new Light(new Vector3f(2000,20000,2000), new Vector3f(.4f,.4f,.4f)));
		update();
	}
	
	private static void update() {
		while(!Display.isCloseRequested()) {
			player.Move(terrains.get(0));
			cam.move();
			renderer.processEntity(player);
			renderer.renderScene(entities, normalMapEntities, terrains, lights, cam, new Vector4f(0, 1, 0, 1));			
			DisplayManager.updateDisplay();
		}
		cleanUp();
	}
	
	private static void cleanUp() {
		renderer.cleanUp();
		DisplayManager.closeDisplay();
	}
	
}
