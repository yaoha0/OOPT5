package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	
	private static int width = 800;
	private static int height = 600;
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Final_Project");
		
		config.setForegroundFPS(60);
        config.setWindowedMode(width, height);
		new Lwjgl3Application(GameMaster.getInstance(), config);
	}
	public static int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public static void setWidth(int width) {
		DesktopLauncher.width = width;
	}
	/**
	 * @return the height
	 */
	public static int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public static void setHeight(int height) {
		DesktopLauncher.height = height;
	}
}
