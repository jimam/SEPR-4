package com.geeselightning.zepr.game;

import com.badlogic.gdx.Game;
import com.geeselightning.zepr.screens.*;

public class Zepr extends Game {

	private MenuScreen menuScreen;
	private LoadingScreen loadingScreen;
	private SelectLevelScreen selectLevelScreen;
	private GameScreen gameScreen;
	
	public static boolean devMode;

	public final static int MENU = 0;
	public final static int LOADING = 1;
	public final static int SELECT = 2;
	public final static int GAME = 3;
	public final static int LEVEL_COMPLETE = 4;
	
	public Zepr(boolean devMode) {
		super();
		Zepr.devMode = devMode;
	}


	public void changeScreen(int screen) {
		switch(screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case LOADING:
				if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
				this.setScreen(loadingScreen);
				break;
			case SELECT:
				if (loadingScreen == null) selectLevelScreen = new SelectLevelScreen(this);
				this.setScreen(selectLevelScreen);
				break;
			case GAME:
				if (gameScreen == null) gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
			case LEVEL_COMPLETE:
				this.setScreen(new TextScreen(this, "Level complete", "You have successfully completed the level"));
				break;
		}
	}

	@Override
	public void create() {
		changeScreen(MENU);
	}
	
}