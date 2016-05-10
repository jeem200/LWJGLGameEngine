package renderEngine;

import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;



public class DisplayManager {
	
	public static final int WIDTH = 1440;
	public static final int HEIGHT = 900;
	public static final int FPS_CAP = 120; 
	
	private static long lastFrameTime;
	private static float delta;

	public static void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3,2)
				.withForwardCompatible(true).
				withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);		
			Display.setTitle("MineCraft Clone");
		
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		
	}
	
	public static void updateDisplay(){
		Display.sync(FPS_CAP);
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = getCurrentTime();
	}
	
	public static float getFrameTimeSeconds(){
		return delta;
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
	}
	
	private static long getCurrentTime(){
		return Sys.getTime()* 1000 / Sys.getTimerResolution();
	}
}
