package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {

    private static final Player instance = new Player(new Sprite(new Texture("core/assets/player01.png")), new Vector2(0, 0));

    private Player(Sprite sprite, Vector2 playerSpawn) {
        super(sprite, playerSpawn, null);
    }

    public static Player getInstance(){
        return instance;
    }

    public void respawn(Vector2 playerSpawn, Level level){
        setX(playerSpawn.x);
        setY(playerSpawn.y);
        this.speed = Constant.PLAYERSPEED;
        this.health = Constant.PLAYERMAXHP;
        this.currentLevel = level;
        this.setTexture(new Texture("core/assets/player01.png"));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // update the direction the player is facing
        direction = getDirection(currentLevel.getMouseWorldCoordinates());

        if (health <= 0) {
            // GAME OVER
        }

        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY= false;
        // Update x position of player.
        // New position is the old position plus the distance moved as a result of the velocity
        setX(getX()  + velocity.x * delta);

        boolean collisionWithMap;
        // check right side middle
        collisionWithMap = currentLevel.isBlocked(getX(), getY());
        // //////////////// React to Collision
        if (collisionWithMap) {
            collisionX = true;
        }

        //reacting to X collision.
        if(collisionX){
            setX(oldX);
            velocity.x = 0;
        }

        // Update y position of player.
        // New position is the old position plus the distance moved as a result of the velocity
        setY(getY() + velocity.y * delta);

        boolean collisionWithMap2;
        // check right side middle
        collisionWithMap2 = currentLevel.isBlocked(getX(), getY());
        // //////////////// React to Collision
        if (collisionWithMap2) {
            collisionY = true;
        }

        //reacting to y collision.
        if(collisionY){
            setY(oldY);
            velocity.y = 0;
        }
    }
}
