package engineTester;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Entity;
import entity.Light;
import entity.Player;
import experimental.UI;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.ButtonUi;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import particles.ParticleMaster;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;
import toolBox.MouseClick;

public class MainMenu {
	static Loader loader = new Loader();
	static int fps;
	static long lastFPS;
	static FontType font;
	static GUIText fpsCounter;
	static MasterRenderer renderer;
	static GuiRenderer guiRenderer;
	static GUIText text;
	static GuiTexture gui;
	static ButtonUi buttonToTTM;
	static List<Terrain> terrains = new ArrayList<Terrain>();
	static List<Light> lights = new ArrayList<Light>();
	static List<Entity> entities = new ArrayList<Entity>();
	static List<Entity> normalMapEntities = new ArrayList<Entity>();
	static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	static Camera camera;
	static Player player;
	
	static MouseClick picker;
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		start();
		
		text = new GUIText("YOU CAN TAKE MY HEART MAKE IT STOP MAKE IT STOP", 2f, font,
				new Vector2f(.5f,.6f), .5f, false);
    	
		var Tes = new GUIText("I", 3f, font,
				new Vector2f(.5f,.5f), .5f, true);
		
		gui = new GuiTexture(loader.loadTexture("IPuked T"), new Vector2f(0f,0), 
				new Vector2f(.1f, .1f));
		//guis.add(gui);
		
		buttonToTTM = new ButtonUi(loader.loadTexture("blank"), new Vector2f(.1f, .1f), new Vector2f(.2f,.2f));
		//guis.add(buttonToTTM);
		Light sun = new Light(new Vector3f(0,1000000,-7000000), new Vector3f(.5f,.4f,.4f));
		lights.add(sun);
		
		RawModel playerModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel playerTextureModel = new TexturedModel(playerModel,
				new ModelTexture(loader.loadTexture("IPuked T")));

		
		player = new Player(playerTextureModel, new Vector3f(100f, 5f, 150f),
				0, 180, 0, 1);
		
		camera = new Camera(player);
	
		picker = new MouseClick(camera, renderer.getProjectionMatrix());

		lastFPS = DisplayManager.getCurrentTime();
		
		update();

	}

	private static void start() {
		initValues();
	}
	
	private static void initValues() {
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		
		TextMaster.init(loader);
		
		font = new FontType(loader.loadTexture("candara"), "candara");
		
		//Text for the FPS counter
		fpsCounter = new GUIText("FPS: " + fps, 3f, font, new Vector2f(0,0), .5f, false);
    	fpsCounter.setColour(1f, 0, 0);
    	
	}
	static boolean openMenu = false;
	private static void update() {
		while(!Display.isCloseRequested()) {
			updateFPS();

			camera.move(1);	
			
			if(buttonToTTM.isButtonDown(0)) {
				cleanUp(false);
				TransparencyTestMain.loadScene();
				return;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
				openMenu = !openMenu;
			}
			
			UI.openMenu(openMenu, guis, loader);
			
			
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, 1));
			
			TextMaster.render();

			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
		}
		cleanUp(true);
	}

	private static void updateFPS() {
	    if (DisplayManager.getCurrentTime() - lastFPS > 1000) {
	    	fpsCounter.updateText("FPS: " + fps);
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fps++;
	}

	private static void cleanUp(boolean close) {
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		loader.cleanUp();
		renderer.cleanUp();
		if(close) {
			DisplayManager.closeDisplay();
		}
	}
}