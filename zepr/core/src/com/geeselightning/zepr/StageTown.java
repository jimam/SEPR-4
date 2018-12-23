package com.geeselightning.zepr;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

public class StageTown extends Stage {

    private static final String mapLocation = "core/assets/maps/town.tmx";
    private static final Vector2 playerSpawn = new Vector2(0, 0);
    //private static final Vector2 testZombieSpawn = new Vector2(0, 50);
    // Defining possible zombie spawn locations on this map
    private static final ArrayList<Vector2> zombieSpawnPoints = new ArrayList<Vector2>(
            Arrays.asList(new Vector2(0,100), new Vector2(0,50),
                    new Vector2(50,50), new Vector2(50,0))
            );
    // Defining the number of zombies for each wave, and the number of waves
    private int waveCount = 3;
    private int wave1 = 5;
    private int wave2 = 10;
    private int wave3 = 15;

    public StageTown(Zepr zepr) {
        super(zepr, mapLocation, playerSpawn, zombieSpawnPoints);
    }

    public void complete() {
        // Update progress
        if (parent.progress == 0) {
            parent.progress = parent.TOWN;
        }
        // else the stage is being replayed
    }

}
