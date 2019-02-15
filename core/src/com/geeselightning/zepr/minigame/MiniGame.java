package com.geeselightning.zepr.minigame;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	public ArrayList<Goose> geese;
	public KeyboardController miniGameController;
	public boolean active;
	private float timeSinceLastClick;
	
	
	
	
	public MiniGame() {
		this.numGeese = 3;
		this.maxAmmo = 5;
		this.ammo = this.maxAmmo;
		this.score = 0;
		timeSinceLastClick = 0;
		this.miniGameController = new KeyboardController();
		this.active = true;
		start();
		
		
	}
	
	protected void genGeese() {
		
		geese = new ArrayList<Goose>();
		for (int i = 0; i < this.numGeese; i++ ) {
			geese.add(new Goose());
			
		}
		
	}
	protected void start() {
		this.active = true;
		genGeese();
	}
	protected void lose() {
		//TODO: Implement lose message, switching back to main menu, clean up game.
		System.out.println("number of geese : " + String.valueOf(geese.size()));
		this.active = false;

	}
	public void update(float delta) {
		
		this.timeSinceLastClick = this.timeSinceLastClick + delta;
		System.out.println(String.valueOf(timeSinceLastClick));
		if (active){
			if (!geese.isEmpty()) {
				//if no remaining ammo and still geese to kill.
				if (ammo < 1) {
					lose();
				}
				
				for(Goose goose : geese ) {
					
					//if any goose has 'escaped'
					if ( goose.currentPos.y > 8) {
						lose();
						break;
					//shooting a goose
					}else {
						if (Gdx.input.justTouched() && timeSinceLastClick > 0.009) {
							if (goose.checkMouse(goose.currentPos) && !goose.isDead){
								goose.die();
//								geese.remove(goose);
								score = score + 100;
								System.out.println("HIT");
							}
								ammo = ammo - 1;	
								System.out.println("ammo: " + String.valueOf(this.ammo));
						}
						timeSinceLastClick = 0;
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
		this.timeSinceLastClick = 0;
		genGeese();
		active = true;
	}
	
}
