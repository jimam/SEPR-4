package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MapScreen implements Screen {

    private MyGdxGame parent;
    private Stage stage;
    private static Texture backgroundTexture;
    private SpriteBatch spriteBatch;

    public MapScreen(MyGdxGame myGDXgame) {
        parent = myGDXgame;

        // The stage is the controller which will react to inputs from the user.
        this.stage = new Stage(new ScreenViewport());

        backgroundTexture = new Texture("core/assets/map.jpg");
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        // Send any input from the user to the stage.
        Gdx.input.setInputProcessor(stage);

        // First clear the stage or it will keep the old layout of the screen
        stage.clear();

        // Create separate tables for the top and bottom of the screen
        Table tableTop = new Table();
        tableTop.left().top();
        tableTop.setFillParent(true);
        //tableTop.setDebug(true);
        stage.addActor(tableTop);

        Table tableBottom = new Table();
        tableBottom.center().bottom();
        tableBottom.setFillParent(true);
        //tableBottom.setDebug(true);
        stage.addActor(tableBottom);

        // Importing the necessary assets for the button textures.
        Skin skin = new Skin(Gdx.files.internal("core/assets/skin/glassy-ui.json"));

        // Creating buttons
        TextButton mainMenu = new TextButton("Main Menu", skin, "small");
        TextButton saveGame = new TextButton("Save Game", skin, "small");
        TextButton townStage = new TextButton("Town", skin, "small");
        TextButton previousStage = new TextButton("Back", skin, "small");
        TextButton nextStage = new TextButton("Next", skin, "small");


        // Adding buttons to the tables
        tableTop.add(mainMenu).padRight(10);
        tableTop.add(saveGame);
        tableBottom.add(previousStage).padRight(10);
        tableBottom.add(townStage).padRight(10);
        tableBottom.add(nextStage);

        // Defining action for main menu button
        mainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MyGdxGame.MENU);
            }
        });


    }

    @Override
    public void render(float delta) {
        // Clears the screen to black.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        // Draws the stage.
        this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        this.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        // Resize map texture with the size of the screen so it fills it
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        // Update the screen when the window resolution is changed.
        this.stage.getViewport().update(width, height, true);
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
        // TODO Auto-generated method stub
        spriteBatch.dispose();
    }
}
