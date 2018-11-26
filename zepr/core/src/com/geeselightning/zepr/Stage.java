package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class Stage implements Screen {

    private Zepr parent;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private Zombie testzombie;
    private String mapLocation;
    private Vector2 playerSpawn;
    private Vector2 testZombieSpawn;

    public Stage(Zepr zepr, String mapLocation, Vector2 playerSpawn, Vector2 testZombieSpawn) {
        parent = zepr;
        this.mapLocation = mapLocation;
        this.playerSpawn = playerSpawn;
        this.testZombieSpawn = testZombieSpawn;
    }

    @Override
    public void show() {
        // Loads the testmap.tmx file as map.
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load(mapLocation);

        // renderer renders the .tmx map as an orthogonal (top-down) map.
        renderer = new OrthogonalTiledMapRenderer(map);
        // It is only possible to view the render of the map through an orthographic camera.
        camera = new OrthographicCamera();

        player = new Player(new Sprite(new Texture("core/assets/player01.png")), playerSpawn);

        // Single zombie
        testzombie = new Zombie(new Sprite(new Texture("core/assets/anime1.png")), testZombieSpawn);

        Gdx.input.setInputProcessor(player);

    }

    @Override
    public void render(float delta) {
        // Clears the screen to black.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Keep the player central in the screen.
        camera.position.set(player.getX() + (player.getHeight() / 2), player.getY() + (player.getHeight() / 2), 0);
        camera.update();

        // Go to main menu if press ESC
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            parent.changeScreen(Zepr.MENU);
        }

        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        testzombie.draw(renderer.getBatch());
        renderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize the camera depending the size of the window.
        camera.viewportHeight = height;
        camera.viewportWidth = width;
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
        dispose();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        player.getTexture().dispose();
        testzombie.getTexture().dispose();
    }
}
