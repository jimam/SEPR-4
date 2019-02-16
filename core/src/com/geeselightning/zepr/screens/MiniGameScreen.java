package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.geeselightning.zepr.KeyboardController;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.minigame.Goose;
import com.geeselightning.zepr.minigame.MiniGame;
//import com.geeselightning.zepr.minigame.ShapeRenderer;
import com.geeselightning.zepr.util.Constant;

public class MiniGameScreen extends DefaultScreen {
		
		private OrthographicCamera camera;
		private ExtendViewport gamePort;
		private SpriteBatch batch;
		private MiniGame miniGame;
		private Sprite backgroundSprite;
		private Label scoreLabel;
		private Label ammoLabel;
		//DEBUG
		private com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRenderer;
		
		public MiniGameScreen(Zepr parent){
			super(parent);			
			this.miniGame = new MiniGame();
			float width = Gdx.graphics.getWidth();
			float height = Gdx.graphics.getHeight();
			System.out.println(String.valueOf(Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight()));
			this.camera = new OrthographicCamera(width, height);
			this.gamePort = new ExtendViewport(width, height);
			this.camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
			this.backgroundSprite = new Sprite(new Texture("miniGameBG.png"));
			this.backgroundSprite.setCenter(0, 0);
			shapeRenderer = new com.badlogic.gdx.graphics.glutils.ShapeRenderer();
			this.batch = new SpriteBatch();
			
			
		}
		
		public void show() {
			Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
			Gdx.input.setInputProcessor(this.miniGame.miniGameController);
			scoreLabel = new Label("Score : ", skin);
			scoreLabel.setPosition(-600, 300);
			scoreLabel.toFront();
			ammoLabel = new Label("Ammo : ", skin);
			ammoLabel.setPosition(-600, 250);
			ammoLabel.toFront();
		}

		@Override
		public void render(float delta) {
			super.render(delta);
			if (this.miniGame.active == false) {
				parent.changeScreen(Zepr.SELECT);
			}
			this.miniGame.update(delta);
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			backgroundSprite.draw(batch);
			for(Goose goose : this.miniGame.geese) {
				if (!goose.isDead) {
					goose.draw(this.batch);
				}
				
				
			}
			this.scoreLabel.draw(batch, 1f);
			this.ammoLabel.draw(batch, 1f);
			batch.end();
			//DEBUG BEGINS
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Line);
			
		       shapeRenderer.setColor(Color.RED);

		       for (Goose goose : this.miniGame.geese) {
		    	   shapeRenderer.rect(goose.getSprite().getX(),goose.getSprite().getY(),goose.getSprite().getWidth(),goose.getSprite().getHeight());
		    	   
		       }
		       shapeRenderer.end();
		       shapeRenderer.begin(ShapeType.Line);
		       shapeRenderer.setColor(Color.RED);
		       for (Goose goose : this.miniGame.geese) {
		    	   shapeRenderer.line(new Vector2(0,0),goose.currentPos);
		       }
		       
		       shapeRenderer.end();
		    //DEBUG ENDS
			this.scoreLabel.setText("Score:   " + String.valueOf(miniGame.score));
			this.ammoLabel.setText("Ammo: " + String.valueOf(miniGame.ammo));
		}
		
		@Override
		public void resize(int width, int height) {
			gamePort.update(width, height);
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

		}

		@Override
		public void resume() {
			// TODO Auto-generated method stub

		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			batch.dispose();
		}
}

