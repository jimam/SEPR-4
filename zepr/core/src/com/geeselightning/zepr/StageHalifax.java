package com.geeselightning.zepr;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

public class StageHalifax extends Stage {

    private static final String mapLocation = "core/assets/maps/halifax.tmx";
    private static final Vector2 playerSpawn = new Vector2(0, 0);
    //private static final Vector2 testZombieSpawn = new Vector2(0, 50);
    // Defining possible zombie spawn locations on this map
    private static final ArrayList<Vector2> zombieSpawnPoints = new ArrayList<Vector2>(
            Arrays.asList(new Vector2(0,0), new Vector2(0,50),
                    new Vector2(50,50), new Vector2(50,0))
    );

    public StageHalifax(Zepr zepr) {
        super(zepr, mapLocation, playerSpawn, zombieSpawnPoints);
    }

    public void complete() {
        // Update progress
        if (parent.progress == parent.TOWN) {
            parent.progress = parent.HALIFAX;
        }
        // else the stage is being replayed
    }
}
