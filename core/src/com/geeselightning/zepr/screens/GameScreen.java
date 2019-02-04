package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;

public class GameScreen extends DefaultScreen {
	
	private OrthographicCamera camera;
	private ExtendViewport gamePort;
	
	private SpriteBatch batch;
	
	private GameManager gameManager = GameManager.getInstance(this.parent);
	

	public GameScreen(Zepr parent) {
		super(parent);
		
		float width = Gdx.graphics.getWidth() / Constant.PPM;
		float height = Gdx.graphics.getHeight() / Constant.PPM;
		
		camera = new OrthographicCamera(width, height);
		gamePort = new ExtendViewport(width, height);
		
		camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		
		batch = new SpriteBatch();
		
		gameManager.setGameCamera(camera);
		gameManager.setSpriteBatch(batch);
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(gameManager.getController());
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		gameManager.update(delta);
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
