package de.tomgrill.gdxtesting;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.Character;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class CharacterTest {

    @Test
    public void collidesWith() {
        // Same character
        Character character = new Character(new Sprite(), new Vector2(0,0), null);
        assertTrue("Character should collide with itself.", character.collidesWith(character));

        // Touching characters
        Character anotherCharacter = new Character(new Sprite(), new Vector2(0,10), null);
        assertTrue("Touching Characters should collide.", character.collidesWith(anotherCharacter));

        // Far apart characters
        anotherCharacter = new Character(new Sprite(), new Vector2(20,20), null);
        assertFalse("Far apart characters should not collide.", character.collidesWith(anotherCharacter));
    }

    @Test
    public void getCenter() {
        Character character = new Character(new Sprite(new Texture("assets/player01.png")), new Vector2(1,1), null);

        // Character height should be 32
        assertEquals("Character height should be 32.", (float) 32.0, character.getHeight(), 0.0);

        // Character with positive center
        assertEquals("Testing center calculation with positive position.", new Vector2((float) 17.0, (float) 17.0), character.getCenter());

        // Character with negative center
        character = new Character(new Sprite(new Texture("assets/player01.png")), new Vector2(-50,-50), null);
        assertEquals("Testing center calculation with negative position.", new Vector2(-34, -34), character.getCenter());
    }

    @Test
    public void getDirection() {
        // Character center should be at origin.
        Character character = new Character(new Sprite(new Texture("assets/player01.png")), new Vector2(-16,-16), null);

        // Testing NE, SE, SW, and NW directions.
        assertEquals("North-East direction should be 0.785.", 0.785, character.getDirection(new Vector2(1,1)), 0.001);
        assertEquals("South-East direction should be 2.356.", 2.356, character.getDirection(new Vector2(1,-1)), 0.001);
        assertEquals("South-West direction should be 3.927.", 3.927, character.getDirection(new Vector2(-1,-1)), 0.001);
        assertEquals("North-West direction should be 5.498.", 5.498, character.getDirection(new Vector2(-1,1)), 0.001);
    }

    @Test
    public void getDirNormVector() {
        // TODO
    }
}