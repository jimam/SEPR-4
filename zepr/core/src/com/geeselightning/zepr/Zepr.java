package com.geeselightning.zepr;

import com.badlogic.gdx.Game;

public class Zepr extends Game {

	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private Stage stage;
	private AppPreferences preferences;


	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;

	public void changeScreen(int screen) {
		switch(screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;
			case APPLICATION:
				// create town stage for now
				if(stage == null) stage = new StageTown(this);
				this.setScreen(stage);
				break;
		}
	}

	@Override
	public void create() {
		preferences = new AppPreferences();
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	public AppPreferences getPreferences() {
		return this.preferences;
	}
}