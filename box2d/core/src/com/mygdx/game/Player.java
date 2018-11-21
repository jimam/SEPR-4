package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

    private Vector2 velocity = new Vector2(); // 2D vector
    private float speed = 120;

    public Player(Sprite sprite) {
        super(sprite);
    }

    @Override
    public void draw(Batch batch) {
        // Calls the update method of the player. To update its properties, i.e. position.
        // The method is given the parameter delta so it can calculate the players new position.
        update(Gdx.graphics.getDeltaTime());

        super.draw(batch);
    }

    /**
     * This method updates all the players properties.
     *
     * @param delta the time passed since the last frame (render() call)
     */
    public void update(float delta) {
        // Update x, y position of player.
        // New position is the old position plus the distance moved as a result of the velocity
        setX(getX()  + velocity.x * delta);
        setY(getY() + velocity.y * delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        // Adding inputs for WASD as movement in the x and y axis.
        if (keycode == Input.Keys.W) {
            velocity.y = speed;
        }
        if (keycode == Input.Keys.S) {
            velocity.y = -speed;
        }
        if (keycode == Input.Keys.D) {
            velocity.x = speed;
        }
        if (keycode == Input.Keys.A) {
            velocity.x = -speed;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // This causes the player to stop moving in a certain direction when the corresponding key is released.
        if (keycode == Input.Keys.W || keycode == Input.Keys.S) {
            velocity.y = 0;
        }
        if (keycode == Input.Keys.A || keycode == Input.Keys.D) {
            velocity.x = 0;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
