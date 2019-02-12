package com.geeselightning.zepr.minigame;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.geeselightning.zepr.KeyboardController;
import com.geeselightning.zepr.screens.TextScreen;
public class MiniGame {
		
	protected int ammo;
	protected int maxAmmo;
	protected int numGeese;
	protected int score;
	protected ArrayList<Goose> geese;
	public KeyboardController miniGameController;
	public SpriteBatch sprites;
	public boolean active;
	protected MiniGame() {
		this.numGeese = 3;
		this.maxAmmo = 5;
		this.ammo = this.maxAmmo;
		this.score = 0;
		active = false;
		start();
		genGeese();
		
	}
	
	protected void genGeese() {
		
		geese = new ArrayList<Goose>();
		for (int i = 0; i < this.numGeese; i++ ) {
			geese.add(new Goose());
			
		}
		
	}
	protected void start() {
		//DISPLAY STARTING MESSAGE
	}
	protected void lose() {
		//TODO: Implement lose message, switching back to main menu.
		active = false;
	}
	//doesnt actually implement time yet
	protected void update(float delta) {
		if (active == true){
			if (!geese.isEmpty()) {
				//if no remaining ammo and still geese to kill.
				if (ammo < 1) {
					lose();
				}
				
				for(Goose goose : geese ) {
					//if any goose has 'escaped'
					if ( goose.currentPos.y == Gdx.graphics.getHeight() ) {
						lose();
						break;
						//shooting a goose
					}else {
						if ((miniGameController.isMouse1Down) 
								&& (goose.checkMouse(miniGameController.mousePosition))){
	
							ammo = ammo - 1;
							geese.remove(goose);
	
						}else {
							ammo = ammo - 1;
						}
					}
					goose.update(delta);
						
				}
			}else {
				nextWave();
				
			}
		}
	}
	private void nextWave() {
		//TODO: Add text for next wave, wait in between waves
		active = false;
		if (maxAmmo < 10) {
			maxAmmo = maxAmmo + 1;
		}
		ammo = maxAmmo;
		numGeese = numGeese + 1;
		
		genGeese();
		active = true;
	}
	
}
