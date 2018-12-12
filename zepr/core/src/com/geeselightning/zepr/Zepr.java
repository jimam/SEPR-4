package com.geeselightning.zepr;

import com.badlogic.gdx.Game;

public class Zepr extends Game {

	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private Stage stage;
	private AppPreferences preferences;
	private SelectStage selectStage;


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
				if(selectStage == null) selectStage = new  SelectStage(this);
				this.setScreen(selectStage);
				break;
			case TOWN:
				if(stage == null) stage = new StageTown(this);
				this.setScreen(stage);
				break;
			case HALIFAX:
				if(stage == null) stage = new StageHalifax(this);
				this.setScreen(stage);
				break;
			case COURTYARD:
				if(stage == null) stage = new StageCourtyard(this);
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