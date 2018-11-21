package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Zombie extends Sprite {

    private Vector2 velocity = new Vector2();
    private float speed = 120;

    public Zombie(Sprite sprite, Float X, Float Y) {
        super(sprite);
        setX(X);
        setY(Y);
    }

    @Override
    public void draw(Batch batch) {
        // Passing update with the time that passed since the last frame.
        update(Gdx.graphics.getDeltaTime());

        super.draw(batch);
    }

    public void update(float delta) {
        // Update x, y position of player.
        // New position is the old position plus the distance moved as a result of the velocity
        setX(getX()  + velocity.x * delta);
        setY(getY() + velocity.y * delta);
    }
}
