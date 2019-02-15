package com.geeselightning.zepr.minigame;

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
import com.badlogic.gdx.math.Rectangle;
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
		float speed;
		Vector2 velocity;
		Vector2 direction;
		public Vector2 currentPos;
		public boolean isDead;
		boolean flapping;
		public Rectangle bounds;
		//TODO: Feels like i should somehow use a sprite batch but i have no idea how to implement one.
		Sprite sprite;
		
		protected Goose() {
			this.sprite = new Sprite(new Texture("goose.png"));
			this.sprite.setSize(1f,1f);
			this.velocity = new Vector2();
			this.direction = new Vector2();
			this.currentPos = new Vector2();
			this.isDead = false;
			this.flapping = false;
			
			
			//TODO: Changing speed changes trajectory of the geese.
			speed = 1;
			//Generates a random start location for the goose
			Random rand = new Random();
			startX = rand.nextInt((Gdx.graphics.getWidth()/50) + 1) - Gdx.graphics.getWidth()/100;
			this.sprite.setFlip(true,false);
			//generates a random but appropriate end location for the goose
			
			endX = rand.nextInt(Gdx.graphics.getWidth()/100);
			if (startX > 0) {
				endX = endX * -1;
				this.sprite.setFlip(false,false);
			}
			
			direction.x = endX;
			direction.y = 8;
			velocity.x = (float) (endX * speed / Math.sqrt((direction.x * direction.x) + 1));
			velocity.y = direction.y;
			currentPos = new Vector2(startX,-1 * Gdx.graphics.getHeight()/100);
			this.bounds = new Rectangle(currentPos.x - 0.5f ,currentPos.y - 0.5f, 2, 2);
			System.out.println("Goose outs at: " + String.valueOf(endX));
		}
		
		public boolean checkMouse(Vector2 mousePos) {
			
			
			if (bounds.contains(mousePos)) {
				return true;
			}
			
			return false;
		}
		
		//TODO: maybe add a proper dispose method.
		public void update(float delta) {
			this.currentPos.x = this.currentPos.x +  (this.velocity.x * delta);
			this.currentPos.y = this.currentPos.y + (this.velocity.y * delta);
			this.bounds = new Rectangle(currentPos.x - 0.5f ,currentPos.y - 0.5f, 2, 2);
			if ((int)this.currentPos.y % 4 == 0) {
				flap();
			}
		}
		public Sprite getSprite() {
			return this.sprite;
		}
		public void draw(SpriteBatch batch) {
			if (flapping) {
				this.sprite.setTexture(new Texture("gooseflap.png"));
			} else {
				this.sprite.setTexture(new Texture("goose.png"));
			}
			sprite.setPosition(this.currentPos.x, this.currentPos.y);
			sprite.draw(batch);
		}
		//animates the sprite.
		public void flap() {
			if (flapping) {	
				flapping = false;
			} else {
				flapping = true;
			}
		}
		
		public void die() {
			this.isDead = true;
		}
		
		

		
}
