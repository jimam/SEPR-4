package de.tomgrill.gdxtesting;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;
import com.geeselightning.zepr.Zombie;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

//@RunWith(GdxTestRunner.class)
public class PlayerTest {

    @Test
    public void respawn() {
        Player player = Player.getInstance();
        Vector2 originalPosition = new Vector2(player.getX(), player.getY());
        player.setPosition(10, 10);

        assertNotEquals("Position changes when moved.", originalPosition, new Vector2(player.getX(), player.getY()));

        player.respawn(new Vector2(0, 0), null);
        assertEquals("Position should reset when player respawned.", originalPosition, new Vector2(player.getX(), player.getY()));
    }

    @Test
    public void canHit() {
        Player player = Player.getInstance();
        // player direction is initially 0.0

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y + 30), null);
        double originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertEquals("Zombie on the edge of range should not take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);

        zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y + 20), null);
        originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertNotEquals("Zombie within range should take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);

        zombie = new Zombie(new Sprite(), new Vector2(100, 4), null);
        originalHealth = zombie.getHealth();
        player.attack(zombie, 0);

        assertEquals("Zombie outside of range should not take damage when the player attacks.",
                zombie.getHealth(), originalHealth, 0.1);
    }


}