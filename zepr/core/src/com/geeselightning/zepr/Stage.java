package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Stage implements Screen {

    private Zepr parent;
    protected TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    protected OrthographicCamera camera;
    private Player player;
    private Zombie testzombie;
    private String mapLocation;
    private Vector2 playerSpawn;
    private Vector2 testZombieSpawn;
    private ZeprInputProcessor inputProcessor = new ZeprInputProcessor();

    public Stage(Zepr zepr, String mapLocation, Vector2 playerSpawn, Vector2 testZombieSpawn) {
        parent = zepr;
        this.mapLocation = mapLocation;
        this.playerSpawn = playerSpawn;
        this.testZombieSpawn = testZombieSpawn;
    }


    public boolean isBlocked(float x, float y) {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("collisionLayer");
        Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));

        // have to include this in case this cell is transparent on the collisionLayer
        if (cell == null || cell.getTile() == null) {
            return false;
        }

        MapProperties properties = cell.getTile().getProperties();
        // we have to add the property now because the properties don't load from the map file
        properties.put("solid", null);

        if (properties.containsKey("solid")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Converts the mousePosition which is a Vector2 representing the coordinates of the mouse within the game window
     * to a Vector2 of the equivalent coordinates in the world.
     * @return Vector2 of the mouse position in the world.
     */
    public Vector2 getMouseWorldCoordinates() {
        // Must first convert to 3D vector as camera.unproject() does not take 2D vectors.
        Vector3 screenCoordinates = new Vector3(inputProcessor.mousePosition.x, inputProcessor.mousePosition.y, 0);
        Vector3 worldCoords3 = camera.unproject(screenCoordinates);

        return new Vector2(worldCoords3.x, worldCoords3.y);
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

        //retrieve player instance and reset it
        player = Player.getInstance();
        player.respawn(playerSpawn, this);

        // Single zombie
        testzombie = new Zombie(new Sprite(new Texture("core/assets/anime1.png")), testZombieSpawn);

        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        // Clears the screen to black.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Keep the player central in the screen.
        camera.position.set(player.getCenter().x, player.getCenter().y, 0);
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
