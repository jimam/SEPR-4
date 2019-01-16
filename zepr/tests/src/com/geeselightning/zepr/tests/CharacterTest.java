package com.geeselightning.zepr.tests;

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
    public void charactersWithSamePositionShouldCollide() {
        Character character = new Character(new Sprite(), new Vector2(0,0), null);
        assertTrue("A Character should collide with itself.", character.collidesWith(character));
    }

    @Test
    public void touchingCharactersShouldCollide() {
            Character anotherCharacter = new Character(new Sprite(), new Vector2(0,10), null);
            Character character = new Character(new Sprite(), new Vector2(0,0), null);
            assertTrue("Characters that touch should collide.", character.collidesWith(anotherCharacter));
    }

    @Test
    public void nonTouchingCharactersShouldNotCollide() {
        Character anotherCharacter = new Character(new Sprite(), new Vector2(20,20), null);
        Character character = new Character(new Sprite(), new Vector2(0,0), null);
        assertFalse("Characters that don't touch should not collide.", character.collidesWith(anotherCharacter));
    }

    @Test
    public void characterSpritesShouldHaveHeight32() {
        Character character = new Character(new Sprite(new Texture("core/assets/player01.png")),
                new Vector2(1,1), null);
        assertEquals("Character height should be 32.", (float) 32.0, character.getHeight(), 0.0);
    }

    @Test
    public void getCenterOnCharacterWithPositivePosition() {
        Character character = new Character(new Sprite(new Texture("core/assets/player01.png")),
                new Vector2(1,1), null);
        assertEquals("Testing center calculation with positive position.", new Vector2((float) 17.0, (float) 17.0),
                character.getCenter());
    }

    @Test
    public void getCenterOnCharacterWithNegativePosition() {
        Character character = new Character(new Sprite(new Texture("core/assets/player01.png")),
                new Vector2(-50,-50), null);
        assertEquals("Testing center calculation with negative position.", new Vector2(-34, -34),
                character.getCenter());
    }

    @Test
    public void getDirectionInTopRightQuadrant() {
        Character character = new Character(new Sprite(new Texture("core/assets/player01.png")), new Vector2(-16,-16), null);
        assertEquals("North-East direction should be 0.785.", 0.785, character.getDirectionTo(new Vector2(1,1)), 0.001);

    }

    @Test
    public void getDirectionInBottomRightQuadrant() {
        Character character = new Character(new Sprite(new Texture("core/assets/player01.png")), new Vector2(-16,-16), null);
        assertEquals("South-East direction should be 2.356.", 2.356, character.getDirectionTo(new Vector2(1,-1)), 0.001);

    }

    @Test
    public void getDirectionInBottomLeftQuadrant() {
        Character character = new Character(new Sprite(new Texture("core/assets/player01.png")), new Vector2(-16,-16), null);
        assertEquals("South-West direction should be 3.927.", 3.927, character.getDirectionTo(new Vector2(-1,-1)), 0.001);
    }

    @Test
    public void getDirectionInTopLeftQuadrant() {
        Character character = new Character(new Sprite(new Texture("core/assets/player01.png")), new Vector2(-16,-16), null);
        assertEquals("North-West direction should be 5.498.", 5.498, character.getDirectionTo(new Vector2(-1,1)), 0.001);
    }


    @Test
    public void getDirNormVector() {
        // TODO
    }
}