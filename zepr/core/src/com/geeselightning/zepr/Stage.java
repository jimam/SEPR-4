package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;

public class Stage implements Screen {

    protected Zepr parent;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private ArrayList<Zombie> aliveZombies = new ArrayList<Zombie>();
    private String mapLocation;
    private Vector2 playerSpawn;
    private ArrayList<Vector2> zombieSpawnPoints;
    private ZeprInputProcessor inputProcessor = new ZeprInputProcessor();
    protected boolean isPaused;

    public Stage(Zepr zepr, String mapLocation, Vector2 playerSpawn, ArrayList<Vector2> zombieSpawnPoints) {
        parent = zepr;
        this.mapLocation = mapLocation;
        this.playerSpawn = playerSpawn;
        this.zombieSpawnPoints = zombieSpawnPoints;
        this.isPaused = false;
    }

    /**
     * Used for collision detection between characters in Character.update()
     *
     * @return ArrayList containing all the characters currently in the stage
     */
    public ArrayList<Character> getCharacters() {
        ArrayList<Character> characters = new ArrayList<Character>();

        for (Zombie zombie : aliveZombies) {
            characters.add(zombie);
        }
        characters.add(player);

        return characters;
    }

    // Spawns multiple zombies cycling through the spawnPoints until the given amount have been spawned.

    /**
     * Spawns multiple zombies cycling through spawnPoints until the given amount have been spawned.
     *
     * @param spawnPoints locations where zombies should be spawned on this stage
     * @param amount number of zombies to spawn
     */
    private void spawnZombies(int amount, ArrayList<Vector2> spawnPoints) {
        for (int i = 0; i < amount; i++) {
            aliveZombies.add(new Zombie(new Sprite(new Texture("core/assets/anime1.png")),
                    spawnPoints.get(i % spawnPoints.size()), this));
        }
    }

    /**
     * Used for collision detection between the player and map
     *
     * @return boolean if the point (x, y) is in a blocked tile
     */
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

        return properties.containsKey("solid");
    }


    /**
     * Converts the mousePosition which is a Vector2 representing the coordinates of the mouse within the game window
     * to a Vector2 of the equivalent coordinates in the world.
     *
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
        // Start the stage unpaused.
        this.isPaused = false;

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
        //testzombie = new Zombie(new Sprite(new Texture("core/assets/anime1.png")), testZombieSpawn);

        spawnZombies(5, zombieSpawnPoints);

        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        if (isPaused) {
            // Clears the screen to black.
            Gdx.gl.glClearColor(0f, 0f, 0f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Need to refactor stage class so it doesn't overlap with the libgdx built in stage class
            // Creating a new libgdx stage to contain the pause menu
            com.badlogic.gdx.scenes.scene2d.Stage gdxstage = new com.badlogic.gdx.scenes.scene2d.Stage(new ScreenViewport());
            // Input processor has to be changed back once unpaused.
            Gdx.input.setInputProcessor(gdxstage);

            // Show pause menu.
            Table table = new Table();
            table.setFillParent(true);
            gdxstage.addActor(table);

            // Importing the necessary assets for the button textures.
            Skin skin = new Skin(Gdx.files.internal("core/assets/skin/pixthulhu-ui.json"));

            TextButton resume = new TextButton("Resume", skin);
            TextButton exit = new TextButton("Exit", skin);

            table.center();
            table.add(resume).pad(10);
            table.row();
            table.add(exit);

            // Defining actions for the resume button.
            resume.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    isPaused = false;
                    // Change input processor back
                    Gdx.input.setInputProcessor(inputProcessor);
                }
            });

            // Defining actions for the exit button.
            exit.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parent.changeScreen(Zepr.SELECT);
                }
            });

            gdxstage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            gdxstage.draw();
        } else {
            // Clears the screen to black.
            Gdx.gl.glClearColor(0f, 0f, 0f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Keep the player central in the screen.
            camera.position.set(player.getCenter().x, player.getCenter().y, 0);
            camera.update();

            renderer.setView(camera);
            renderer.render();

            renderer.getBatch().begin();

            player.draw(renderer.getBatch());

            for (Zombie zombie : aliveZombies) {
                zombie.draw(renderer.getBatch());
            }
            renderer.getBatch().end();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Resize the camera depending the size of the window.
        camera.viewportHeight = height;
        camera.viewportWidth = width;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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
        for (Zombie zombie : aliveZombies) {
            zombie.getTexture().dispose();
        }
    }
}
