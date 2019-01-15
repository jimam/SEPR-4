package de.tomgrill.gdxtesting;

import com.geeselightning.zepr.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class LevelTest {

//    @Test
//    public void complete() {
//        Zepr zepr = new Zepr();
//        zepr.create();
//
//        assertTrue("Progress should be Town initially.",zepr.progress == 3);
//
//        TownLevel town = new TownLevel(zepr);
//        town.complete();
//        assertTrue("Progress should be Halifax after completing Town.",zepr.progress == 4);
//
//        TownLevel townRepeat = new TownLevel(zepr);
//        townRepeat.complete();
//        assertTrue("Progress should remain Halifax after completing Town if it is repeated",
//                zepr.progress == 4);
//
//        HalifaxLevel halifax = new HalifaxLevel(zepr);
//        halifax.complete();
//        assertTrue("Progress should be Courtyard after completing Halifax.", zepr.progress == 5);
//
//        CourtyardLevel courtyard = new CourtyardLevel(zepr);
//        courtyard.complete();
//        assertTrue("Progress should remain Courtyard after completing Courtyard as it is the final level.",
//                zepr.progress == 5);
//    }

    @Test
    public void getCharacters() {
        TownLevel level = new TownLevel(new Zepr());

        // After creating a level, there should only be the player in the level.
        assertEquals("Length of getCharacters() should be initially 1.", level.getCharacters().size(), 1);
        assertTrue("Only member of getCharacters() should be of type Player", level.getCharacters().get(0) instanceof Player);

        // Add a zombie
        level.spawnZombies(1, level.zombieSpawnPoints);
        assertEquals("After spawning a zombie the length of getCharacters() should be 2.", level.getCharacters().size(), 2);
        assertTrue("The next member of getCharacters() should be of type Zombie", level.getCharacters().get(1) instanceof Zombie);

        // Add a couple more zombies
    }

    @Test
    public void spawnZombies() {
        // Spawn a few zombies in different locations, should return 0
        // Spawn two zombies in the same location, should return 1
        // Spawn 0 zombies, should return 0
        // Give no spawn points, negative test
    }

    @Test
    public void isBlocked() {
        // Try a cell that should be blocked
        // Try a cell that shouldn't be blocked
        // Try a cell outside the map, should be blocked

    }
}