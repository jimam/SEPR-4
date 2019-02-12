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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.geeselightning.zepr.KeyboardController;
import com.geeselightning.zepr.screens.TextScreen;
import java.util.Random;

public class Goose {
		
		float startX;
		float endX;
		int speed;
		Vector2 velocity;
		Vector2 direction;
		Vector2 currentPos;
		public ClickListener clicklistener;
		boolean isDead;
		boolean flapping;
		//TODO: Feels like i should somehow use a sprite batch but i have no idea how to implement one.
		Sprite sprite;
		
		protected Goose() {
			this.sprite = new Sprite(new Texture("gooseflap1.png"));
			Random rand = new Random();
			speed = rand.nextInt(5) + 1;
			//Generates a random start location for the goose
			startX = rand.nextInt(Gdx.graphics.getWidth()) + 1;
			//generates a random but appropriate end location for the goose
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
		
		public boolean checkMouse(Vector2 mousePos) {
			
			if ((currentPos.x - 10 < mousePos.x) && (mousePos.x < currentPos.x + 10)) {
				if ((currentPos.y - 10 < mousePos.y) && (mousePos.y < currentPos.y + 10)) {
					
					return true;
				}
				
			}
			return false;
		}
		
		//TODO: maybe add a proper dispose method.
		public void update(float delta) {
			this.currentPos.x = this.currentPos.x +  (this.velocity.x * delta);
			this.currentPos.y = this.currentPos.y +  (this.velocity.y * delta);
			if ((int)delta % 3 == 0) {
				flap();
			}
		}
		public void flap() {
			if (flapping) {
				this.sprite =  new Sprite(new Texture("gooseflap1.png"));
				flapping = false;
			} else {
				this.sprite = new Sprite(new Texture("gooseflap2.png"));
			}
		}
		
		

		
}
