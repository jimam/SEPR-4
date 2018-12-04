package com.geeselightning.zepr;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class StageHalifax extends Stage {

    private static final String mapLocation = "core/assets/maps/halifax.tmx";
    private static final Vector2 playerSpawn = new Vector2(0, 0);
    private static final Vector2 testZombieSpawn = new Vector2(0, 50);

    public StageHalifax(Zepr zepr) {
        super(zepr, mapLocation, playerSpawn, testZombieSpawn);
    }
}
