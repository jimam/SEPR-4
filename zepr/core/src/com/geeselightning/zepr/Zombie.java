package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Zombie extends Character {

    public Zombie(Sprite sprite, Vector2 zombieSpawn) {
        super(sprite, zombieSpawn);
    }

    @Override
    public void update(float delta) {
        // Update x, y position of player.
        // New position is the old position plus the distance moved as a result of the velocity
        setX(getX()  + velocity.x * delta);
        setY(getY() + velocity.y * delta);

        // update velocity to move towards player (for now don't know how to retrieve player pos so will assume is at origin
        Vector2 testplayercoord= new Vector2(0f,0f);
        velocity = getDirNormVector(testplayercoord).scl(speed);

    }
}
