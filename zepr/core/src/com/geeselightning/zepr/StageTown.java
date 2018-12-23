package com.geeselightning.zepr;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class StageTown extends Stage {

    private static final String mapLocation = "core/assets/maps/town.tmx";
    private static final Vector2 playerSpawn = new Vector2(0, 0);
    private static final Vector2 testZombieSpawn = new Vector2(0, 50);

    public StageTown(Zepr zepr) {
        super(zepr, mapLocation, playerSpawn, testZombieSpawn);
    }

    public void complete() {
        // Update progress
        if (parent.progress == 0) {
            parent.progress = parent.TOWN;
        }
        // else the stage is being replayed
    }

}
