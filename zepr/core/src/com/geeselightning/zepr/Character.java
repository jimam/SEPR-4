package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

public class Character extends Sprite {

    protected Vector2 velocity = new Vector2(); // 2D vector
    protected float speed;
    protected int health;
    protected double direction;
    protected Stage currentStage;

    public Character(Sprite sprite, Vector2 spawn, Stage currentStage) {
        super(sprite);
        setX(spawn.x);
        setY(spawn.y);
        this.currentStage = currentStage;
    }

    private boolean collidesWith(Character character) {
        double diameter = 10;
        double distanceBetweenCenters = (Math.pow(getCenter().x - character.getCenter().x, 2)
                + Math.pow(getCenter().y - character.getCenter().y, 2));
        return (0 <= distanceBetweenCenters && distanceBetweenCenters <= Math.pow(diameter, 2));
    }

    @Override
    public void draw(Batch batch) {
        // Calls the update method of the character. To update its properties, i.e. position.
        // The method is given the parameter delta so it can calculate the character new position.
        update(Gdx.graphics.getDeltaTime());

        super.draw(batch);

        setRotation((float) Math.toDegrees(- direction));
    }

    public Vector2 getCenter() {
        return new Vector2(getX() + (getHeight() / 2), getY() + (getWidth() / 2));
    }

    /**
     * Finds the direction (in radians) that an object is in relative to the character.
     * @param coordinate 2d vector representing the position of the object
     * @return bearing   double in radians of the bearing from the character to the coordinate
     */
    protected double getDirection(Vector2 coordinate) {
        Vector2 charCenter = new Vector2(this.getX() + (getWidth()/ 2),
                this.getY()+ (getHeight() / 2));

        // atan2 is uses the signs of both variables the determine the correct quadrant (relative to the character) of the
        // result.
        // Modulus 2pi of the angle must be taken as the angle is negative for the -x quadrants.
        // The angle must first be displaced by 2pi because the Java modulus function can return a -ve value.

        return(Math.atan2((coordinate.x - charCenter.x), (coordinate.y - charCenter.y)) + (2 * Math.PI))
                % (2 * Math.PI);
    }

    /**
     * returns a normailised vector that points torwards given coordinate.
     * @param coordinate 2d vector representing the position of the object
     * @return normalised 2d vector that from this will point towards given coordinate
     */
    protected Vector2 getDirNormVector(Vector2 coordinate) {
        Vector2 charCenter = new Vector2(this.getX() + (getWidth() / 2),
                this.getY() + (getHeight() / 2));
        // create vector that is the difference between character and the coordinate, and return it normalised
        Vector2 diffVector = new Vector2((coordinate.x - charCenter.x), (coordinate.y - charCenter.y));
        return diffVector.nor();
    }

    /**
     * This method updates all the characters properties.
     *
     * @param delta the time passed since the last frame (render() call)
     */
    public void update(float delta) {
        // Update x, y position of character.
        // New position is the old position plus the distance moved as a result of the velocity
        float oldX = getX();
        float oldY = getY();

        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);

        ArrayList<Character> otherCharacters = currentStage.getCharacters();
        otherCharacters.remove(this);

        for (Character character : otherCharacters) {
            if (collidesWith(character)) {
                setX(oldX);
                setY(oldY);
            }
        }
    }

    // Returns the value of health
    protected int getHealth(){
        return health;
    }

    // Decreases health by value of dmg
    protected void takeDamage(int dmg){
        health -= dmg;
    }

}
