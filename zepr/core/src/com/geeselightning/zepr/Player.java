package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character implements InputProcessor {

    private static final Player instance = new Player(new Sprite(new Texture("core/assets/player01.png")), new Vector2(0, 0));
    private Vector2 mouseCoordinate = new Vector2(0, 0);

    private Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn);
    }

    public static Player getInstance(){
        return instance;
    }

    public void respawn(Vector2 playerSpawn){
        setX(playerSpawn.x);
        setY(playerSpawn.y);
        this.speed = Constant.PLAYERSPEED;
        this.health = Constant.PLAYERMAXHP;
        this.setTexture(new Texture("core/assets/player01.png"));
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
        mouseCoordinate.set(Gdx.input.getX(), Gdx.input.getY());
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
