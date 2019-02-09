package com.geeselightning.zepr.minigame;
import java.util.ArrayList;

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
public class MiniGame {
		
	protected int ammo;
	protected int limit;
	protected int score;
	protected ArrayList<Goose> geese;
	
	protected MiniGame() {
		
		
		
	}
	protected void genGeese(int limit) {
		
		geese = new ArrayList<Goose>();
		for (int i = 0; i < limit; i++ ) {
			geese.add(new Goose());
			
		}
		
	}
	protected void lose() {
		
		
	}
	protected void update(float delta) {
		for(Goose goose : geese ) {
			if ( goose.currentPos.y == Gdx.graphics.getHeight() ) {
				lose();
				break;
			}
			goose.currentPos.x = goose.currentPos.x +  (goose.velocity.x * delta);
			goose.currentPos.y = goose.currentPos.y +  (goose.velocity.y * delta);
			
		}
	}
}
