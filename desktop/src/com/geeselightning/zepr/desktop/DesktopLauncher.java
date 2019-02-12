package com.geeselightning.zepr.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.geeselightning.zepr.game.Zepr;

public class DesktopLauncher {
	public static void main (String[] args) {
		boolean devMode = false;
		for(String arg : args) {
<<<<<<< HEAD
			if (arg.equals("-dev")) devMode = true;
=======
			if (arg.equals("--dev")) {
				devMode = true;
			}
>>>>>>> 502d325b24df704ed61c510aa9901ffe96b61bf1
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Zepr(devMode), config);
	}
}
