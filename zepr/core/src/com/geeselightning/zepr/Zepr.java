package com.geeselightning.zepr;

import com.badlogic.gdx.Game;

public class Zepr extends Game {

	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private Level level;
	private AppPreferences preferences;
	private SelectLevelScreen selectLevelScreen;

	// The progress is the integer representing the last level completed. i.e. 3 for Town
	public int progress = 4;

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int SELECT = 2;
	public final static int TOWN = 3;
	public final static int HALIFAX = 4;
	public final static int COURTYARD = 5;


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
			case SELECT:
				selectLevelScreen = new SelectLevelScreen(this);
				this.setScreen(selectLevelScreen);
				break;
			case TOWN:
				if(level == null) level = new TownLevel(this);
				this.setScreen(level);
				break;
			case HALIFAX:
				if(level == null) level = new HalifaxLevel(this);
				this.setScreen(level);
				break;
			case COURTYARD:
				if(level == null) level = new CourtyardLevel(this);
				this.setScreen(level);
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