package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.geeselightning.zepr.KeyboardController;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.minigame.Goose;
import com.geeselightning.zepr.minigame.MiniGame;
import com.geeselightning.zepr.util.Constant;

public class MiniGameScreen extends DefaultScreen {
		
		private OrthographicCamera camera;
		private ExtendViewport gamePort;
		private SpriteBatch batch;
		private MiniGame miniGame;
		private Sprite backgroundSprite;
		public MiniGameScreen(Zepr parent){
			super(parent);			
			this.miniGame = new MiniGame();
			float width = Gdx.graphics.getWidth() / Constant.PPM;
			float height = Gdx.graphics.getHeight() / Constant.PPM;
			System.out.println(String.valueOf(Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight()));
			this.camera = new OrthographicCamera(width, height);
			this.gamePort = new ExtendViewport(width, height);
			this.camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
			this.backgroundSprite = new Sprite(new Texture("miniGameBG.png"));
			this.backgroundSprite.setCenter(0, 0);
			
			this.batch = new SpriteBatch();
			
		}
		
		public void show() {
			Gdx.input.setInputProcessor(this.miniGame.miniGameController);
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
			batch.end();
			
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

