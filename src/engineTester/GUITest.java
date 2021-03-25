package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Light;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.ButtonUi;
import guis.GuiRenderer;
import guis.GuiTexture;
import particles.ParticleMaster;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

public class GUITest {

	static Loader loader = new Loader();
	static MasterRenderer renderer;
	static Camera camera = new Camera();
	static GuiRenderer guiRenderer;
	static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	static List<Light> lights = new ArrayList<Light>();
	static ButtonUi button;
	static GUIText text;
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		loadScene();
	}
	
	private static void loadScene() {
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		TextMaster.init(loader);
		var font = new FontType(loader.loadTexture("candara"), "candara");
		button = new ButtonUi(loader.loadTexture("blank"), new Vector2f(0,-.5f), new Vector2f(.1f,.1f), font, "This is awwwwwwwwwwwwwwwwwwwwwwwwwwwwn Test Button");
		text = new GUIText("Hello", 1, font, new Vector2f(.0f,.5f), .5f, false);
		text.setColour(1, 0, 0);
		guis.add(button);
		
		update();
	}
	
	private static void update() {
		while(!Display.isCloseRequested()) {
			
			//camera.move(1);
			if(button.isButtonDown(0)) {
				text.setColour((float) Math.random(), (float) Math.random(), (float) Math.random());
			}
			if(button.asLongButtonDown(1)) {
			}
			
			text.updateText(Mouse.getX() + " " + Mouse.getY());
			
			renderer.render(lights, camera, new Vector4f(0, 1, 0, 1));

			guiRenderer.render(guis);
			TextMaster.render();
			
			DisplayManager.updateDisplay();
		}
		cleanUp();
	}
	
	private static void cleanUp() {
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
}
