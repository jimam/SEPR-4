package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.game.Zepr;

public abstract class Character extends Entity {

    protected float speed;
    protected int health;
    // All characters start ready to hit.
    float hitRefresh = 2;

    public Character(Zepr parent, Sprite sprite, float bRadius, Vector2 initialPos, float initialRot) {
        super(parent, sprite, bRadius, initialPos, initialRot);
        this.health = 100;
    }

    /* Accessor methods */
    public int getHealth() {
        return health;
    }
    
    public float getSpeed() {
    	return speed;
    }
    
    @Override
    public void update(float delta) {
    	if (health <= 0) {
    		alive = false;
    		return;
    	}
    }

    /**
     * Finds the direction (in degrees) that an object is in relative to the character.
     * Assessment 3: updated to use box2d world position rather than sprite position, and now returns degrees
     *
     * @param coordinate 2d vector representing the position of the object
     * @return bearing   double in degrees of the bearing from the character to the coordinate
     */
    public double getDirectionTo(Vector2 coordinate) {
        Vector2 charCenter = this.b2body.getPosition();

        return(Math.toDegrees(Math.atan2((coordinate.x - getX()), (coordinate.y - getY()))));
    }

    /**
     * Calculates a normalised vector that points towards given coordinate.
     * Assessment 3: updated to use box2d world position rather than sprite position;
     *
     * @param coordinate Vector2 representing the position of the object
     * @return normalised Vector2 that from this will point towards given coordinate
     */
    public Vector2 getDirNormVector(Vector2 coordinate) {
        Vector2 charCenter = this.b2body.getPosition();
        // create vector that is the difference between character and the coordinate, and return it normalised
        Vector2 diffVector = new Vector2((coordinate.x - charCenter.x), (coordinate.y - charCenter.y));
        return diffVector.nor();
    }
    
    /* Convenience methods for setting velocity */
	public void setLinearVelocity(float vX, float vY) {
		this.b2body.setLinearVelocity(vX, vY);
	}
	
	public void setLinearVelocity(Vector2 velocity) {
		setLinearVelocity(velocity.x, velocity.y);
	}
	
	public void setLinearVelocityX(float vX) {
		setLinearVelocity(vX, this.b2body.getLinearVelocity().y);
	}
	
	public void setLinearVelocityY(float vY) {
		setLinearVelocity(this.b2body.getLinearVelocity().x, vY);
	}

    // Decreases health by value of dmg
    public abstract void takeDamage(int dmg);

}
