package com.mygdx.game.desktop;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Main;

import Model.Solver;

public class DesktopLauncher {
	
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width=Main.WIDTH;
        config.height=Main.HEIGHT;
        config.resizable=false;
		config.foregroundFPS = (int)Solver.fps;
		config.backgroundFPS =(int) Solver.fps;

		new LwjglApplication(Main.getInstance(), config);
	}
}
