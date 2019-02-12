package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.geeselightning.zepr.game.Zepr;

public abstract class DefaultScreen implements Screen {
	
	public final Zepr parent;

	public DefaultScreen(Zepr parent) {
		this.parent = parent;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

}
