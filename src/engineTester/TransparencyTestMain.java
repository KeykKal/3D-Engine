package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Entity;
import entity.EntityTypes;
import entity.Light;
import entity.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;
import particles.Particle;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import terrain.TerrainTexture;
import terrain.TerrainTexturePack;
import textures.ModelTexture;
import toolBox.MousePicker;

public class TransparencyTestMain {
	static Loader loader = new Loader();
	static int fps;
	static long lastFPS;
	static FontType font;
	static GUIText fpsCounter;
	static MasterRenderer renderer;
	static GuiRenderer guiRenderer;
	static GUIText text;
	static List<Terrain> terrains = new ArrayList<Terrain>();
	static List<Light> lights = new ArrayList<Light>();
	static List<Entity> entities = new ArrayList<Entity>();
	static List<Entity> normalMapEntities = new ArrayList<Entity>();
	static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	static Terrain[][] currentTerrain;
	
	static Camera camera;
	static Player player;
	
	
	static ParticleSystem particleSystem;
	
	static MousePicker picker;

	public static void loadScene() {

		start();
		
		text = new GUIText("YOU CAN TAKE MY HEART MAKE IT STOP MAKE IT STOP", 3f, font,
				new Vector2f(.0f,.5f), .5f, true);
    	
    	
    	
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		  
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		

		int terrainX = 2;
		int terrainZ = 2;
		currentTerrain = new Terrain[terrainX][terrainZ];
	
		for(int tx = 0; tx < terrainX; tx++) {
			for(int tz = 0; tz < terrainZ; tz++) {
				terrains.add(new Terrain(tx, tz, loader, texturePack, blendMap, "heightmap", false));
				for (var t:terrains) {
					currentTerrain[tx][tz] = t;//new Terrain(tx,tz, loader, texturePack, blendMap, "heightmap");					
				}
			}
		}
		
		Light sun = new Light(new Vector3f(0,1000000,-7000000), new Vector3f(.4f,.4f,.4f));

		lights.add(sun);
		
		//lights.add(new Light(new Vector3f(-200, 10, -200), new Vector3f(10,0,0), new Vector3f(1,0.01f,0.002f)));
		//lights.add(new Light(new Vector3f(200, 10, 200), new Vector3f(0,0,10), new Vector3f(1,0.01f,0.002f)));
		
		
		Random random = new Random();
		for(int i = 0; i < 500; i++)
		{	
			if (i % 20 == 0)
			{			  
			  float x = random.nextFloat() * 800 -400;
			  float z = random.nextFloat() * -600;
			  Terrain t = currentTerrain[(int) ((x/Terrain.SIZE))][(int) ((z/Terrain.SIZE))];
			  float y	= t.getHeightOfTerrain(x, z);
			  entities.add(new Entity(grass, 
					  new Vector3f(x, y, z),
					  0, 0, 0, 3));
			  entities.add(new Entity(tree, 
					  new Vector3f(x, y, z),
					  0, 0, 0, 1f));
			}
		}
		
		TexturedModel sword = new TexturedModel(OBJLoader.loadObjModel("testSword", loader), 
				new ModelTexture(loader.loadTexture("blank")));
		
		entities.add(new Entity(sword, new Vector3f(100, 10, 150), 0, 0, 0, 2));
		
		TexturedModel barrel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		normalMapEntities.add(new Entity(barrel, new Vector3f(100,20,100), 0, 0, 0, 1.5f));
		//Terrain terrain = new Terrain(0, 0, loader, 
			//	new ModelTexture(loader.loadTexture("grass")));
		
		RawModel playerModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel playerTextureModel = new TexturedModel(playerModel, 
				new ModelTexture(loader.loadTexture("IPuked T")));
		
		player = new Player(playerTextureModel, new Vector3f(100f, 5f, 150f),
				0, 180, 0, 1);
		
		camera = new Camera(player);
		
		

		GuiTexture gui = new GuiTexture(loader.loadTexture("IPuked T"), new Vector2f(.0f,.0f), 
				new Vector2f(.1f,.1f));
		guis.add(gui);
		
		
		EntityTypes type = new EntityTypes();
		Terrain t = currentTerrain[(int) ((100/Terrain.SIZE))][(int) ((100/Terrain.SIZE))];
		EntityTypes.Lamp lämp = type.new Lamp(loader,new Vector3f(100,t.getHeightOfTerrain(100, 100),100),
				1f, 15f, new Vector3f(2f,2f,0f), new Vector3f(1f,0.01f,0.003f));
		Entity test = new Entity(grass, new Vector3f(100,lämp.getLight().getPosition().y,100), 0, 0, 0, 1);
		entities.add(test);
		entities.add(lämp.getEntity());
		lights.add(lämp.getLight());
		
		
		
		picker = new MousePicker(camera, renderer.getProjectionMatrix(), currentTerrain);
		
		Light light = new Light(new Vector3f(player.getPosition()), new Vector3f(10,20,20), new Vector3f(1f,0.023f,0.023f));
		lights.add(light);
		
		ParticleTexture pTexture = new ParticleTexture(loader.loadTexture("fire"), 8, true);
		
		particleSystem = new ParticleSystem(pTexture, 900, 1, -0.1f, 4, 8);
		particleSystem.randomizeRotation();
		particleSystem.setDirection(new Vector3f(0,1,0), .01f);
		particleSystem.setLifeError(.1f);
		particleSystem.setSpeedError(.7f);
		particleSystem.setScaleError(.1f);
		
		lastFPS = DisplayManager.getCurrentTime();
	
		update();
	}
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		loadScene();
	}
	
	private static void start() {
		initValues();
	}
	
	private static void initValues() {
		loader = new Loader();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		
		TextMaster.init(loader);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());	
		
		font = new FontType(loader.loadTexture("candara"), "candara");
		
		//Text for the FPS counter
		fpsCounter = new GUIText("FPS: " + fps, 3f, font, new Vector2f(0,0), .5f, false);
    	fpsCounter.setColour(1f, 0, 0);
    	
	}
	
	private static void update() {
		while(!Display.isCloseRequested()) {
			updateFPS();

			if(!camera.useCameraControlle) {
				int gridX = (int) (player.getPosition().x / Terrain.SIZE);
				int gridZ = (int) (player.getPosition().z / Terrain.SIZE);
				player.Move(currentTerrain[gridX][gridZ]);
			}
			if(!Keyboard.isKeyDown(Keyboard.KEY_G))
				camera.move(1);
			
			
//			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
//				MainMenu.;
			
			picker.update2D();
			
			Vector3f point = picker.getCurrentRay();
			if(point != null) {

			}
			
			//add Input here
			//picker.update();
			//TODO: Fix this shit
			//Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			//if(terrainPoint != null) {
				//lämp.setPosition(terrainPoint);
				//if(Mouse.isButtonDown(1))
					//light.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y +5 , terrainPoint.z));
					
			//}
			
			
			particleSystem.generateParticles(new Vector3f(150f, 15f, 150f));
			
			ParticleMaster.update(camera);
			
			renderer.processEntity(player);
			
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, 1));
			
			ParticleMaster.renderParticles(camera);
			
			TextMaster.render();

//			renderer.proccessEntity(wallEntity);
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
		}
		cleanUp();
	}

	private static void updateFPS() {
	    if (DisplayManager.getCurrentTime() - lastFPS > 1000) {
	    	fpsCounter.updateText("FPS: " + fps);
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fps++;
	}

	private static void cleanUp() {
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
