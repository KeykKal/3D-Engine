			package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Entity;
import entity.Light;
import entity.Player;
import fontRendering.TextMaster;
import guis.GuiRenderer;
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
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class WaterScene {

	public static void main(String[] args) {
		//some important stuff
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		//Lists and Arrays
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		Light sun = new Light(new Vector3f(0,10000,-7000), new Vector3f(.4f,.4f,.4f));
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<WaterTile> waters = new ArrayList<WaterTile>();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();

		
		//setup player
		RawModel playerModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel playerTextureModel = new TexturedModel(playerModel, 
				new ModelTexture(loader.loadTexture("IPuked T")));
		
		//initialize player and camera
		Player player = new Player(playerTextureModel, new Vector3f(100f, 5f, 150f),
				0, 180, 0, 1);
		Camera cam = new Camera(player);
		
		//Lights setup
		List<Light> lights = new ArrayList<Light>();
		lights.add(sun);
	
		//Textures for the terrain
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		//setup Terrains
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//add the terrain
		Terrain[][] currentTerrain;
		int terrainX = 1;
		int terrainZ = 1;
		currentTerrain = new Terrain[terrainX][terrainZ];
		for(int tx = 0; tx < terrainX; tx++) {
			for(int tz = 0; tz < terrainZ; tz++) {
				terrains.add(new Terrain(tx, tz, loader, texturePack, blendMap, "heightmap", false));
				for (var t:terrains) {
					currentTerrain[tx][tz] = t;//new Terrain(tx,tz, loader, texturePack, blendMap, "heightmap");					
				}
			}
		}
		
		//Water rendering and stuff
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		var water = new WaterTile(85, 85, 0);
		waters.add(water);
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		
		
		//GUI
		GuiTexture refraction= new GuiTexture(fbos.getRefractionTexture(), new Vector2f(-.8f,.8f), new Vector2f(.2f,.2f));
		GuiTexture reflection = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-.8f,.25f), new Vector2f(.2f,.2f));
		guis.add(refraction);
		guis.add(reflection);
		//while loop with the Renderer and other things
		while(!Display.isCloseRequested()) {
			

			if(!cam.useCameraControlle) {
				int gridX = (int) (player.getPosition().x / Terrain.SIZE);
				int gridZ = (int) (player.getPosition().z / Terrain.SIZE);
				player.Move(currentTerrain[gridX][gridZ]);
			}
			cam.move();
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			//refraction Texture
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, cam, new Vector4f(0, -1, 0, water.getHeight()));
			
			//reflection texture
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (cam.getPosition().y - water.getHeight());
			cam.getPosition().y -= distance;
			cam.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, cam, new Vector4f(0, 1, 0, -water.getHeight()));
			cam.getPosition().y += distance;
			cam.invertPitch();
			
			//normal renderer to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();
			renderer.processEntity(player);
			renderer.renderScene(entities, normalMapEntities, terrains, lights, cam, new Vector4f(0, -1, 0, 100000));
			waterRenderer.render(waters, cam);
			
//			renderer.proccessEntity(wallEntity);
			//guiRenderer.render(guis);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();

		}
		fbos.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
}
