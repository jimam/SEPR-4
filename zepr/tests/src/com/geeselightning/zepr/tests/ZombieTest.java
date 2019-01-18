package com.geeselightning.zepr.tests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Player;
import com.geeselightning.zepr.Zombie;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ZombieTest {

    @Test
    // Test 3.1.1
    public void zombieDoesDamageToPlayerWhenAtMaxRange() {
        Player player = Player.getInstance();

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y - Constant.ZOMBIERANGE), null);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertNotEquals("Player on the edge of range should not take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
    }

    @Test
    // Test 3.1.2
    public void zombieDoesDamageToPlayerWhenInRange() {
        Player player = Player.getInstance();

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y - Constant.ZOMBIERANGE + 10), null);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertNotEquals("Player within range should take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
    }

    @Test
    // Test 3.1.3
    public void zombieDoesNoDamageToPlayerOutOfRange() {
        Player player = Player.getInstance();

        Zombie zombie = new Zombie(new Sprite(), new Vector2(player.getCenter().x, player.getCenter().y - 100), null);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertEquals("Player outside of range should not take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
    }
}
