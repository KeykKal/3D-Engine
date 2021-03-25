package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;


public class DisplayManager {
	
	public static final int WIDTH = 1800;
	public static final int HEIGHT = 900;
	private static final int FPS_CAP = 60;
	public static final float RATIO = (float) WIDTH/ (float) HEIGHT; // Does nothing rn
	
	public static int width = 1800;
	public static int height = 900;
	
	private static long lastFrameTime;
	private static float delta;
	
	private static final String NAME = "OUR ENGINE!!";
	
	
	static long lastFrame;
	public static int fps;
	static long lastFPS;
	
	public static  void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3,3)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
//			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			DisplayMode displayMode = new DisplayMode(WIDTH, HEIGHT);;
	        DisplayMode[] modes = Display.getAvailableDisplayModes();
	        for (int i = 0; i < modes.length; i++)
	        {
				if (modes[i].getWidth() == WIDTH
				&& modes[i].getHeight() == HEIGHT
				&& modes[i].isFullscreenCapable())
				{
					
				        displayMode = modes[i];
				}
	        }
			Display.setFullscreen(false);
			Display.create(new PixelFormat().withSamples(4), attribs);//change the 8 to a greater value for an better Antialiasing 
			Display.setTitle(NAME);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		
        //getDelta(); 
		getFrameTimeSeconds();
		lastFPS = getCurrentTime(); 
        
    }
	
	public static void updateDisplay() {
		
		updateFPS();
		Display.sync(FPS_CAP);
		Display.update();
		if(Keyboard.isKeyDown(Keyboard.KEY_F11)) {
			try {
				Display.setFullscreen(!Display.isFullscreen());
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				System.err.print("[ERR] can't change to fullscreen");
				e.printStackTrace();
			}
		}
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
		
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	
	public static long getCurrentTime() {
		return (Sys.getTime() * 1000)/Sys.getTimerResolution();
	}
	
	public static int getDelta() {
	    long time = getCurrentTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	         
	    return delta;
	}
	
	public static void updateFPS() {
	    if (getCurrentTime() - lastFPS > 1000) {
	        Display.setTitle(NAME+" | FPS: " + fps + " out of " + FPS_CAP); 
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fps++;
	}
	
}


