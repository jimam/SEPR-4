package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Character extends Sprite {

    protected Vector2 velocity = new Vector2(); // 2D vector
    protected float speed = 120;

    public Character(Sprite sprite, Vector2 spawn) {
        super(sprite);
        setX(spawn.x);
        setY(spawn.y);
    }

    @Override
    public void draw(Batch batch) {
        // Calls the update method of the player. To update its properties, i.e. position.
        // The method is given the parameter delta so it can calculate the players new position.
        update(Gdx.graphics.getDeltaTime());

        super.draw(batch);
    }

    /**
     * Finds the direction (in degrees) that an object is in relative to the player.
     * @param coordinate 2d vector representing the position of the object
     * @return bearing   value in radians of the bearing from the player to the coordinate
     */
    protected double getDirection(Vector2 coordinate) {
        Vector2 playerCenter = new Vector2(this.getX() + (getWidth()/ 2),
                this.getY()+ (getHeight() / 2));

        // atan2 is uses the signs of both variables the determine the correct quadrant (relative to the player) of the
        // result.
        // Modulus 2pi of the angle must be taken as the angle is negative for the -x quadrants.
        // The angle must first be displaced by 2pi because the Java modulus function can return a -ve value.

        return(Math.atan2((coordinate.x - playerCenter.x), (coordinate.y - playerCenter.y)) + (2 * Math.PI))
                % (2 * Math.PI);
    }

    /**
     * returns a normailised vector that points torwards given coordinate.
     * @param coordinate 2d vector representing the position of the object
     * @return normalised 2d vector that from this will point towards given coordinate
     */
    protected Vector2 getDirNormVector(Vector2 coordinate) {
        Vector2 zombieCenter = new Vector2(this.getX() + (getWidth() / 2),
                this.getY() + (getHeight() / 2));

        Vector2 diffVector = new Vector2((coordinate.x - zombieCenter.x), (coordinate.y - zombieCenter.y));
        return diffVector.nor();
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

}
