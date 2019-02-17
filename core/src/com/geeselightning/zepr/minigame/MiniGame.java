package com.geeselightning.zepr.minigame;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;

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

/**
 * 
 * @author ljd546
 *
 */
public class MiniGame {
		
	public int ammo;
	public int score;
	public KeyboardController miniGameController;
	public int wave;
	public boolean active;
	private float timeSinceLastClick;
	public int waveNumber;
	public long startTime;

	public Goose goose;
	public Random rand;
	
	public MiniGame() {
		
		this.miniGameController = new KeyboardController();
		//initialises game variables
		this.ammo = 3;
		this.score = 0;
		this.wave = 1;
		this.goose = new Goose(1/10);
		//initialises other necessary variables
		this.timeSinceLastClick = 0;
		this.rand = new Random();
		this.startTime = System.currentTimeMillis();
		start();
		
		
	}
	/*
	 * Activates the game
	 */
	protected void start() {
		this.active = true;
	}
	
	public void update(float delta) {
		
		//implements a delay on click
		this.timeSinceLastClick = this.timeSinceLastClick + delta;
		
		if (active){
			
			if (ammo > 0) {
				this.goose.update(delta);
				
			}else {
				nextWave();
			}
			if (rand.nextInt(100) > 90) {
				goose.changeDirection();
			}
			//if any goose has 'escaped'
			goose.update(delta);
			if ( goose.currentPos.y > 360) {
				nextWave();
			}else {
				//On Click
				if (Gdx.input.justTouched() && timeSinceLastClick > 0.009) {
					if (goose.checkMouse() && !goose.isDead){
						goose.die();
						
						//Score is added based on the time it took to click the goose.
						score = score +(100 - (int)(System.currentTimeMillis()- this.startTime)/100);
						System.out.println("HIT");
						nextWave();
					}
					ammo = ammo - 1;	
					System.out.println("ammo: " + String.valueOf(this.ammo));
				}
				timeSinceLastClick = 0;
			}	
		}						
					
		
	}
	/*
	 * updates the wave to make the geese move faster. Changes all other values to their initial states.
	 */
	private void nextWave() {
		this.startTime = System.currentTimeMillis();
		active = false;
		ammo = 3;
		wave = wave + 1;
		this.timeSinceLastClick = 0;
		active = true;
		if (wave < 8) {
			this.goose = new Goose(wave/10);
		} else {
			this.goose = new Goose(0.8f);
		}
	}
	
}
