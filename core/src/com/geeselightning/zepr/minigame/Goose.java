package com.geeselightning.zepr.minigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.geeselightning.zepr.Zepr;
import com.geeselightning.zepr.ZeprInputProcessor;
import com.geeselightning.zepr.screens.TextScreen;
import java.util.Random;

public class Goose {
		
		
		float startX;
		float endX;
		int speed;
		Vector2 velocity;
		Vector2 direction;
		Vector2 currentPos;
		
		boolean isDead;
		Sprite sprite;
		
		protected Goose() {
			this.sprite = new Sprite(new Texture("goose.png"));
			Random rand = new Random();
			speed = rand.nextInt(5) + 1;
			startX = rand.nextInt(Gdx.graphics.getWidth()) + 1;
			
			if (startX >= Gdx.graphics.getWidth()) {
				
				endX = rand.nextInt((Gdx.graphics.getWidth() /2) + 1);
				
			} else {
				
				endX = rand.nextInt((Gdx.graphics.getWidth() /2)) + Gdx.graphics.getWidth() /2;
				
				this.sprite.flip(true,false);
				
			}
			
			direction.x = endX - startX;
			direction.y = Gdx.graphics.getHeight();
			velocity.x = (float) (direction.x * speed / Math.sqrt((direction.x * direction.x) + (direction.y *direction.y)));
			velocity.y = (float) (direction.y * speed / Math.sqrt((direction.x * direction.x) + (direction.y *direction.y)));
			currentPos = new Vector2(startX,0);
		}
		
		

		
}
