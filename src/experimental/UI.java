package experimental;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiTexture;
import renderEngine.Loader;

public class UI {
	
	static GuiTexture guil;
	
	public static void openMenu(boolean open, List<GuiTexture> guis, Loader loader) {
		if(open) {
			guil = new GuiTexture(loader.loadTexture("crate"), new Vector2f(0,0), new Vector2f(.2f,.2f));
			guis.add(guil);
		}else {
			guis.remove(guil);
			
		}
	}
	
}
