package de.tomgrill.gdxtesting;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class LevelTest {

    @Test
    public void complete() {
        // Set progress, call complete(), assert equals progress for each stage
    }

    @Test
    public void getCharacters() {
        // After creating a level should just be player
        // Add a zombie
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