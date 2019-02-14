package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;

public abstract class Entity {
	
	protected Zepr parent;
	protected GameManager gameManager;
	
	protected World world;
	
	protected float bRadius;
	
	protected Body b2body;
	
	protected Sprite sprite;
	
	protected Vector2 initialPos;
	
	protected float initialRot;
	
	protected boolean alive = true;

	public Entity(Zepr parent, Sprite sprite, float bRadius, Vector2 initialPos, float initialRot) {
		this.parent = parent;
		this.sprite = sprite;
		this.bRadius = bRadius;
		this.initialPos = initialPos;
		this.initialRot = initialRot;
		sprite.setSize(bRadius * 2, bRadius * 2);
		sprite.setOrigin(bRadius, bRadius);
		gameManager = GameManager.getInstance(parent);
		world = gameManager.getWorld();
	}
	
	/* Accessor methods */
	public Body getBody() {
		return b2body;
	}
	
	public Vector2 getPos() {
		return b2body.getPosition();
	}
	
	public float getX() {
		return getPos().x;
	}
	
	public float getY() {
		return getPos().y;
	}
	
	public float getRot() {
		return (float) Math.toDegrees(b2body.getAngle());
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	/* Body transform methods */
	public void moveTo(float x, float y) {
		b2body.setTransform(x, y, b2body.getAngle());
	}
	
	public void setAngle(float angle) {
		b2body.setTransform(b2body.getPosition(), (float)Math.toRadians(angle));
	}
	
	public void setAngle(double angle) {
		b2body.setTransform(b2body.getPosition(), (float)Math.toRadians(angle));
	}
	
	public abstract void defineBody();
	
	public void draw(SpriteBatch batch) {
		float posX = b2body.getPosition().x - bRadius;
		float posY = b2body.getPosition().y - bRadius;
		float rotation = (float) Math.toDegrees(b2body.getAngle());
		sprite.setPosition(posX, posY);
		sprite.setRotation(rotation);
		sprite.draw(batch);
	}
	
	public abstract void update(float delta);
	
	public void delete() {
		world.destroyBody(b2body);
		b2body.setUserData(null);
		b2body = null;
	}
	
	public double distanceFrom(Entity entity) {
		return Math.sqrt(Math.pow(getX() - entity.getX(), 2) + Math.pow(getY() - entity.getY() ,2));
	}

}
