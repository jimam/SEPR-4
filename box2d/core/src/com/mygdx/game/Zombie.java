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

    /**
     * Finds the direction (in degrees) that an object is in relative to the zombie.
     * @param coordinate 2d vector representing the position of the object
     * @return bearing   value in radians of the bearing from the zombie to the coordinate
     */
    private double getDirection(Vector2 coordinate) {
        Vector2 zombieCenter = new Vector2(this.getX() + (getWidth()/ 2),
                this.getY()+ (getHeight() / 2));

        // atan2 is uses the signs of both variables the determine the correct quadrant (relative to this zombie) of the
        // result.
        // Modulus 2pi of the angle must be taken as the angle is negative for the -x quadrants.
        // The angle must first be displaced by 2pi because the Java modulus function can return a -ve value.

        return(Math.atan2((coordinate.x - zombieCenter.x), (coordinate.y - zombieCenter.y)) + (2 * Math.PI))
                % (2 * Math.PI);
    }

    /**
     * returns a normailised vector that points torwards given coordinate.
     * @param coordinate 2d vector representing the position of the object
     * @return normalised 2d vector that from this will point towards given coordinate
     */
    private Vector2 getDirNormVector(Vector2 coordinate) {
        Vector2 zombieCenter = new Vector2(this.getX() + (getWidth() / 2),
                this.getY() + (getHeight() / 2));

        Vector2 diffVector = new Vector2((coordinate.x - zombieCenter.x), (coordinate.y - zombieCenter.y));
        return diffVector.nor();
    }

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
